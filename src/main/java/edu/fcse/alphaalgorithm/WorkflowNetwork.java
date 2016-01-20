package edu.fcse.alphaalgorithm;

import edu.fcse.alphaalgorithm.tools.Event;
import edu.fcse.alphaalgorithm.tools.Pair;
import edu.fcse.alphaalgorithm.tools.Place;
import edu.fcse.alphaalgorithm.tools.Trace;

import java.util.*;

/**
 * Created by Blagoj Atanasovski
 */
public class WorkflowNetwork {
    private final Set<Event> eventsList;
    private final Set<Place> workflowPlaces;
    private final Set<Pair<Event, Place>> eventToPlaceTransitions;
    private final Map<Event, Set<Place>> eventToPlaceTransitionsMap;
    private final Set<Pair<Place, Event>> placeToEventTransitions;
    private final Place in;
    private final Place out;
    private final Map<Event, Pair<Set<Place>, Set<Place>>> eventPrePostMap;

    public WorkflowNetwork(Set<Place> workflowPlaces,
                           Set<Event> eventsList,
                           Set<Pair<Event, Place>> eventToPlaceTransitions,
                           Map<Event, Set<Place>> eventToPlaceTransitionsMap,
                           Set<Pair<Place, Event>> placeToEventTransitions,
                           Place in,
                           Place out) {
        this.workflowPlaces = workflowPlaces;
        this.eventsList = eventsList;
        this.eventToPlaceTransitions = eventToPlaceTransitions;
        this.eventToPlaceTransitionsMap = eventToPlaceTransitionsMap;
        this.placeToEventTransitions = placeToEventTransitions;
        this.in = in;
        this.out = out;
        this.eventPrePostMap = createActivityPrePostMap(this.workflowPlaces, this.eventsList);
    }

    public Set<Place> getWorkflowPlaces() {
        return workflowPlaces;
    }

    public Set<Event> getEventsList() {
        return eventsList;
    }

    public Set<Pair<Event, Place>> getEventToPlaceTransitions() {
        return eventToPlaceTransitions;
    }

    public Map<Event, Set<Place>> getEventToPlaceTransitionsMap() {
        return eventToPlaceTransitionsMap;
    }

    public Set<Pair<Place, Event>> getPlaceToEventTransitions() {
        return placeToEventTransitions;
    }

    public Place getIn() {
        return in;
    }

    public Place getOut() {
        return out;
    }

    @Override
    public String toString() {
        Set<Event> eventList = this.getEventsList();
        Set<Place> workflowPlaces = this.getWorkflowPlaces();
        StringBuilder sb = new StringBuilder(
                40
                        * (this.getPlaceToEventTransitions().size() + this.getEventToPlaceTransitions()
                        .size()) + eventList.size() + 15
                        * workflowPlaces.size());
        sb.append("Events:\n");
        for (Event event : eventList) {
            sb.append(event).append(", ");
        }

        sb.append("\nPlaces:\n");
        sb.append(in).append("\n");
        for (Place place : workflowPlaces) {
            sb.append(place).append("\n");
        }

        sb.append(out);
        sb.append("\n");
        sb.append("Transitions:\n");
        for (Pair<Place, Event> transition : this.getPlaceToEventTransitions()) {
            sb.append(String.format("From place (%s) to event [%s]\n",
                    transition.getFirst(), transition.getSecond()));
        }

        for (Pair<Event, Place> transition : this.getEventToPlaceTransitions()) {
            sb.append(String.format("From event [%s] to place (%s)\n",
                    transition.getFirst(), transition.getSecond()));
        }

        return sb.toString();
    }

    private Map<Event, Pair<Set<Place>, Set<Place>>> createActivityPrePostMap(Set<Place> workflowPlaces, Set<Event> eventList) {
        Map<Event, Pair<Set<Place>, Set<Place>>> eventPrePostMap = new HashMap<>();
        for (Event event : eventList) {
            Set<Place> first = new HashSet<>();
            Set<Place> second = new HashSet<>();
            eventPrePostMap.put(event, new Pair<>(first, second));
        }

        for (Place p : workflowPlaces) {
            Set<Event> inA = p.getInEvents();
            for (Event event : inA) {
                Pair<Set<Place>, Set<Place>> pair = eventPrePostMap.get(event);
                pair.getSecond().add(p);
            }

            Set<Event> outA = p.getOutEvents();
            for (Event activity : outA) {
                Pair<Set<Place>, Set<Place>> pair = eventPrePostMap.get(activity);
                pair.getFirst().add(p);
            }
        }

        return eventPrePostMap;
    }

    public boolean runTrace(Trace trace) {
        this.getWorkflowPlaces().forEach(Place::clearToken);
        List<Event> currentTrace = trace.getEventsList();
        in.putToken();
        for (Event currentActivity : currentTrace) {
            // if out place has a token but trace is not finished
            if (out.hasToken()) {
                return false;
            }

            Pair<Set<Place>, Set<Place>> actPrePost = eventPrePostMap
                    .get(currentActivity);

            //activity not found in construction set event log
            if (actPrePost == null) {
                return false;
            }

            Set<Place> inPlaces = actPrePost.getFirst();
            boolean enabled = true;
            for (Place p : inPlaces) {
                if (!p.hasToken()) {
                    enabled = false;
                    break;
                }
            }

            if (enabled) {
                inPlaces.forEach(Place::takeToken);
                actPrePost.getSecond().forEach(Place::putToken);
            }
        }

        if (out.hasToken()) {
            long numTokens = workflowPlaces.stream().filter(place -> place.hasToken()).count();
            return numTokens == 1;
        } else {
            return false;
        }
    }
}
