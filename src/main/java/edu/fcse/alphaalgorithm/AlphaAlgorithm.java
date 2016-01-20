package edu.fcse.alphaalgorithm;

import edu.fcse.alphaalgorithm.tools.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates a Workflow Net Model from an eventLog.
 *
 * @author blagoj atanasovski
 */
public class AlphaAlgorithm {
    public static boolean takeInAccountLoopsLengthTwo = true;

    public static WorkflowNetwork discoverWorkflowNetwork(Set<Trace> eventsLogArg) {
        Set<LoopLengthOne> recordedLLOs = new HashSet<>();
        for (Trace trace : eventsLogArg) {
            preProcessLLOs(trace, recordedLLOs);
        }

        Set<Event> eventList = new HashSet<>();
        HashSet<Event> startingEvents = new HashSet<>();
        HashSet<Event> endingEvents = new HashSet<>();

        // ProcessMining book page 133
        // Steps 1,2,3
        AlphaAlgorithm.extractEvents(eventsLogArg, eventList,
                startingEvents, endingEvents);

        // Generate footprint matrix from eventsLog
        Footprint footprint = new Footprint(eventList, eventsLogArg,
                takeInAccountLoopsLengthTwo);
        System.out.println("------------------------");
        System.out.println("Footprint matrix:");
        System.out.println(footprint);
        System.out.println("------------------------");

        // Step 4 generate places
        Set<Place> XL = AlphaAlgorithm.getPlacesFromFootprint(footprint, eventList);

        // Step 5 reduce places, no places that are subsets of other places
        // workflowPlaces = YL = all the places except start and sink
        Set<Place> workflowPlaces = AlphaAlgorithm.reducePlaces(XL); //PL
        AlphaAlgorithm.postProcessWF(recordedLLOs, workflowPlaces, eventList);

        // Step 7 create transitions
        Set<Pair<Event, Place>> eventToPlaceTransitions = new HashSet<>();
        Map<Event, Set<Place>> eventToPlaceTransitionsMap = new HashMap<>();
        Set<Pair<Place, Event>> placeToEventTransitions = new HashSet<>();
        AlphaAlgorithm.createEventToPlaceTransitions(eventList, workflowPlaces, eventToPlaceTransitions, eventToPlaceTransitionsMap);
        AlphaAlgorithm.createPlaceToEventTransitions(eventList, workflowPlaces, placeToEventTransitions);

        // Step 6
        // Source and sink place
        Place in = new Place("in", new HashSet<>(), new HashSet<>());
        Place out = new Place("out", new HashSet<>(), new HashSet<>());
        AlphaAlgorithm.connectSourceAndSink(in, out, startingEvents, endingEvents, workflowPlaces, placeToEventTransitions, eventToPlaceTransitions);
        WorkflowNetwork network = new WorkflowNetwork(workflowPlaces, eventList, eventToPlaceTransitions, eventToPlaceTransitionsMap, placeToEventTransitions, in, out);
        System.out.println(network);
        return network;
    }


    /**
     * @param eventLog       a set of traces from where the event names are extracted
     * @param allEvents      a set that after this method will contain the names of the
     *                       events present in the eventsLog (Xl)
     * @param startingEvents a set of the starting events, events with which the traces
     *                       start with (Xi)
     * @param endingEvents   a set of ending events, events that the traces end with (Xo)
     */
    private static void extractEvents(Set<Trace> eventLog, Set<Event> allEvents,
                                      Set<Event> startingEvents, Set<Event> endingEvents) {
        allEvents.clear();
        startingEvents.clear();
        endingEvents.clear();
        for (Trace singleTrace : eventLog) {
            startingEvents.add(singleTrace.getFirstEvent());
            endingEvents.add(singleTrace.getLastEvent());
            allEvents.addAll(singleTrace.getEventsList());
        }
    }

    /**
     * @return XL, a set of places, each place has input and output events, this
     * set can be reduced to YL by the method {@link #reducePlaces(Set)}
     */
    private static Set<Place> getPlacesFromFootprint(Footprint footprint, Set<Event> eventList) {
        Set<Place> xl = new HashSet<>();
        System.out.println("Getting places from footprint:");
        Set<Set<Event>> powerSet = Utils.powerSet(eventList);
        System.out.println("Got powerSet: " + powerSet.size());
        powerSet.remove(new HashSet<Event>());
        @SuppressWarnings("unchecked")
        Set<Event>[] powerSetArray = powerSet.toArray(new Set[powerSet.size()]);
        System.out.println("Power set cast to array");
        for (int i = 0; i < powerSetArray.length; i++) {
            Set<Event> first = powerSetArray[i];
            for (int j = 0; j < powerSetArray.length; j++) {
                if (i == j) {
                    continue;
                }

                Set<Event> second = powerSetArray[j];
                if (footprint.areEventsConnected(first, second)) {
                    xl.add(new Place(first, second));
                }
            }
        }

        System.out.println("Places (XL) created from footprint. # of places in XL: " + xl.size());
        return xl;
    }

    /**
     * @param xl the result from step 4 of the algorithm
     * @return Yl the result from step 5 of the algorithm, all subset Places
     * removed from Xl
     */
    private static Set<Place> reducePlaces(Set<Place> xl) {
        Set<Place> toRemove = new HashSet<>();
        Place[] potentialPlaces = xl.toArray(new Place[xl.size()]);
        for (int i = 0; i < potentialPlaces.length - 1; i++) {
            Place potentialPlace1 = potentialPlaces[i];
            for (int j = i + 1; j < potentialPlaces.length; j++) {
                if (potentialPlace1.getInEvents().containsAll(
                        potentialPlaces[j].getInEvents())) {
                    if (potentialPlaces[i].getOutEvents().containsAll(
                            potentialPlaces[j].getOutEvents())) {
                        toRemove.add(potentialPlaces[j]);
                        continue;
                    }
                }

                if (potentialPlaces[j].getInEvents().containsAll(
                        potentialPlaces[i].getInEvents())) {
                    if (potentialPlaces[j].getOutEvents().containsAll(
                            potentialPlaces[i].getOutEvents())) {
                        toRemove.add(potentialPlaces[i]);
                    }
                }
            }
        }

        Set<Place> yl = new HashSet<>(xl);
        yl.removeAll(toRemove);
        return yl;
    }

    private static void createEventToPlaceTransitions(final Set<Event> eventList,
                                                      final Set<Place> workflowPlaces,
                                                      final Set<Pair<Event, Place>> eventToPlaceTransitions,
                                                      final Map<Event, Set<Place>> eventToPlaceTransitionsMap) {
        eventToPlaceTransitions.clear();
        eventToPlaceTransitionsMap.clear();
        for (Event event : eventList) {
            Set<Place> eventToPlace = new HashSet<>();
            eventToPlaceTransitionsMap.put(event, eventToPlace);
            workflowPlaces.stream()
                    .filter(place -> place.getInEvents().contains(event))
                    .forEach(place -> {
                        eventToPlaceTransitions.add(new Pair<>(event, place));
                        eventToPlace.add(place);
                    });
        }
    }

    private static void createPlaceToEventTransitions(final Set<Event> eventList,
                                                      final Set<Place> workflowPlaces,
                                                      final Set<Pair<Place, Event>> placeToEventTransitions) {
        placeToEventTransitions.clear();
        for (Event event : eventList) {
            placeToEventTransitions.addAll(
                    workflowPlaces.stream()
                            .filter(place -> place.getOutEvents().contains(event))
                            .map(place -> new Pair<>(place, event))
                            .collect(Collectors.toList())
            );
        }
    }

    /**
     * Source and Sink places are not connected after the transitions are
     * created between the other events.
     */
    private static void connectSourceAndSink(final Place in,
                                             final Place out,
                                             final Set<Event> startingEvents,
                                             final Set<Event> endingEvents,
                                             final Set<Place> workflowPlaces,
                                             final Set<Pair<Place, Event>> placeToEventTransitions,
                                             final Set<Pair<Event, Place>> eventToPlaceTransitions) {
        for (Event startEvent : startingEvents) {
            in.addOutEvent(startEvent);
            placeToEventTransitions
                    .add(new Pair<>(in, startEvent));
        }

        for (Event endEvent : endingEvents) {
            out.addInEvent(endEvent);
            eventToPlaceTransitions.add(new Pair<>(endEvent, out));
        }

        workflowPlaces.add(in);
        workflowPlaces.add(out);
    }


    private static int checkForCycleLengthOne(List<Event> eventsInTrace) {
        for (int i = 0; i < eventsInTrace.size() - 1; i++) {
            if (eventsInTrace.get(i).equals(eventsInTrace.get(i + 1))) {
                return i;
            }
        }

        return -1;
    }

    //TODO test this
    private static void preProcessLLOs(Trace singleTrace, Set<LoopLengthOne> recordedLLOs) {
        int start;
        List<Event> eventsInTrace = singleTrace.getEventsList();
        while ((start = checkForCycleLengthOne(eventsInTrace)) != -1) {
            int prev = start - 1;
            int currentEvent = start;
            while (currentEvent < eventsInTrace.size() - 1 && eventsInTrace.get(currentEvent).equals(eventsInTrace.get(currentEvent + 1))) {
                currentEvent++;
            }

            currentEvent++;
            LoopLengthOne llo = new LoopLengthOne(eventsInTrace.get(prev),
                    eventsInTrace.get(start), eventsInTrace.get(currentEvent));
            int numberOfOccurrencesOfRepeatingEvent = currentEvent - start;
            for (int i = 0; i < numberOfOccurrencesOfRepeatingEvent; i++) {
                eventsInTrace.remove(start);
            }

            recordedLLOs.add(llo);
        }

    }

    private static void postProcessWF(Set<LoopLengthOne> recordedLLOs, Set<Place> workflowPlaces, Set<Event> eventList) {
        Queue<LoopLengthOne> lloQueue = new LinkedList<>(recordedLLOs);
        while (!lloQueue.isEmpty()) {
            LoopLengthOne llo = lloQueue.poll();
            Event in = llo.getPrevEvent();
            Event out = llo.getNextEvent();
            boolean used = false;
            for (Place place : workflowPlaces) {
                if (place.getInEvents().contains(in)
                        && place.getOutEvents().contains(out)) {
                    place.addInEvent(llo.getLoopedEvent());
                    place.addOutEvent(llo.getLoopedEvent());
                    eventList.add(llo.getLoopedEvent());
                    used = true;

                    // TODO: BlagojA NOT SURE ABOUT THIS
                    break;
                }
            }

            if (!used) {
                lloQueue.add(llo);
            }
        }
    }
}
