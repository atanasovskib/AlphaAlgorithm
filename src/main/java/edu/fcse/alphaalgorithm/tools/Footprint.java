package edu.fcse.alphaalgorithm.tools;

import java.util.*;

/**
 * A wrapper for the footprint matrix of an event log. Relations between events
 * are represented by the {@code edu.fcse.alphaalgorithm.RelationType} enum. For
 * footprint[a][b] the result can be: # = events a and b are not connected > =
 * event a precedes b < = event a follows b | = a>b and a<b
 *
 * @author blagoj atanasovski
 */
public class Footprint {
    RelationType[][] footprint;
    Map<String, Integer> eventNameToMatrixIndex = new HashMap<>();

    public Footprint(Set<Event> allEvents, Set<Trace> eventLog,
                     boolean lookForLoopsOfLengthTwo) {
        int index = 0;
        // assign each of the events an index in the matrix
        for (Event event : allEvents) {
            eventNameToMatrixIndex.put(event.getName(), index++);
        }

        int numberOfEvents = allEvents.size();
        // In the beginning there were no connections
        footprint = new RelationType[numberOfEvents][numberOfEvents];
        for(RelationType[] row: footprint){
            Arrays.fill(row,RelationType.NOT_CONNECTED);
        }

        // And then God said, let there be a Trace
        for (Trace singleTrace : eventLog) {
            List<Event> eventsList = singleTrace.getEventsList();
            for (int i = 0; i < eventsList.size() - 1; i++) {
                // currentEventNumber is followed by nextEventNumber in some
                // trace
                int currentEventNumber = eventNameToMatrixIndex
                        .get(eventsList.get(i).getName());
                int nextEventNumber = eventNameToMatrixIndex
                        .get(eventsList.get(i + 1).getName());
                // if this is the first time these two have been
                // found next to each other
                if (footprint[currentEventNumber][nextEventNumber] == RelationType.NOT_CONNECTED) {
                    footprint[currentEventNumber][nextEventNumber] = RelationType.PRECEDES;
                    footprint[nextEventNumber][currentEventNumber] = RelationType.FOLLOWS;
                } else if (footprint[currentEventNumber][nextEventNumber] == RelationType.FOLLOWS) {
                    // if nextEventNumber was before currEventNumber
                    // in some trace
                    footprint[currentEventNumber][nextEventNumber] = RelationType.PARALLEL;
                    footprint[nextEventNumber][currentEventNumber] = RelationType.PARALLEL;
                }

                // if some of the other relation types are at this position,
                // they are not changed
            }
        }

        if (lookForLoopsOfLengthTwo) {
            for (Trace singleTrace : eventLog) {
                List<Event> eventsList = singleTrace.getEventsList();
                for (int i = 0; i < eventsList.size() - 2; i++) {
                    if (eventsList.get(i).equals(eventsList.get(i + 2))) {
                        int currentEventNumber = eventNameToMatrixIndex
                                .get(eventsList.get(i).getName());
                        int nextEventNumber = eventNameToMatrixIndex
                                .get(eventsList.get(i + 1).getName());
                        footprint[currentEventNumber][nextEventNumber] = RelationType.PRECEDES;
                        footprint[nextEventNumber][currentEventNumber] = RelationType.PRECEDES;
                    }
                }
            }
        }
    }

    public RelationType getRelationType(Event firstEvent, Event secondEvent) {
        int rowIndex = eventNameToMatrixIndex.get(firstEvent.getName());
        int colIndex = eventNameToMatrixIndex.get(secondEvent.getName());
        return footprint[rowIndex][colIndex];
    }

    /**
     * @param firstEvent  name of one event (i.e. a)
     * @param secondEvent name of another event (i.e. b)
     * @return false if a # b, true otherwise
     */
    public boolean areConnected(Event firstEvent, Event secondEvent) {
        return getRelationType(firstEvent, secondEvent) != RelationType.NOT_CONNECTED;
    }

    /**
     * @param firstEvent  name of one event (i.e. a)
     * @param secondEvent name of another event (i.e. b)
     * @return true if a>b, false otherwise
     */
    public boolean isFirstFollowedBySecond(Event firstEvent, Event secondEvent) {
        return getRelationType(firstEvent, secondEvent) == RelationType.PRECEDES;
    }

    public boolean areEventsConnected(Set<Event> inputEvents,
                                      Set<Event> outputEvents) {
        /*
         * (A,B), A = first, B = second
		 */
        // For every a1,a2 in A => a1#a2
        boolean areInputEventsConnectedBetweenThemselves = inputEvents.stream()
                .anyMatch(inputEvent1 ->
                        inputEvents.stream()
                                .anyMatch(inputEvent2 -> areConnected(inputEvent1, inputEvent2)));

        if (areInputEventsConnectedBetweenThemselves) {
            return false;
        }

        // For every b1, b2 in B => b1#b2
        boolean areOutputEventsConnectedBetweenThemselves = outputEvents.stream()
                .anyMatch(outputEvent ->
                        outputEvents.stream()
                                .anyMatch(outputEvent2 -> areConnected(outputEvent, outputEvent2)));
        if (areOutputEventsConnectedBetweenThemselves) {
            return false;
        }

        // For every a in A and b in B => a > b in f
        boolean allFromBFollowAllFromA = inputEvents.stream()
                .allMatch(inputEvent ->
                        outputEvents.stream()
                                .allMatch(outputEvent -> isFirstFollowedBySecond(inputEvent, outputEvent)));
        return allFromBFollowAllFromA;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder("  ");
        String[] tmp = new String[footprint.length];
        for (String key : eventNameToMatrixIndex.keySet()) {
            tmp[eventNameToMatrixIndex.get(key)] = key;
        }

        for (int i = 0; i < footprint.length; i++) {
            toReturn.append(tmp[i]).append(' ');
        }

        toReturn.append('\n');
        for (int i = 0; i < footprint.length; i++) {
            toReturn.append(tmp[i]).append(' ');
            for (int j = 0; j < footprint.length; j++) {
                toReturn.append(footprint[i][j].symbol()).append(' ');
            }

            toReturn.append('\n');
        }

        return toReturn.toString();
    }

}
