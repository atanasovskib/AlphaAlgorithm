package edu.fcse.alphaalgorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper for the footprint matrix of an event log. Relations between events
 * are represented by the {@code edu.fcse.alphaalgorithm.RelationType} enum. 
 * For footprint[a][b] the result can be: 
 * # = events a and b are not connected 
 * > = event a precedes b 
 * < = event a follows b 
 * | = a>b and a<b
 * 
 * @author blagoj atanasovski
 * 
 */
public class Footprint {
    RelationType[][] footprint;
    Map<String, Integer> eventNameToMatrixIndex = new HashMap<>();

    public Footprint(Set<String> eventNames, Set<Trace> eventLog) {
	int index = 0;
	// assign each of the events an index in the matrix
	for (String name : eventNames) {
	    eventNameToMatrixIndex.put(name, index++);
	}
	int numberOfEvents = eventNames.size();
	// In the beginning there were no connections
	footprint = new RelationType[numberOfEvents][numberOfEvents];
	for (int i = 0; i < numberOfEvents; i++) {
	    for (int j = 0; j < numberOfEvents; j++) {
		footprint[i][j] = RelationType.NOT_CONNECTED;
	    }
	}
	// And then God said, let there be a Trace
	for (Trace singleTrace : eventLog) {
	    List<String> eventsList = singleTrace.getEventsList();
	    for (int i = 0; i < eventsList.size() - 1; i++) {
		// currentEventNumber is followed by nextEventNumber in some
		// trace
		int currentEventNumber = eventNameToMatrixIndex.get(eventsList
			.get(i));
		int nextEventNumber = eventNameToMatrixIndex.get(eventsList
			.get(i + 1));
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
		// if some of the other reлаtion types are at this position,
		// they are not changed
	    }
	}
    }

    public RelationType getRelationType(String firstEvent, String secondEvent) {
	int rowIndex = eventNameToMatrixIndex.get(firstEvent);
	int colIndex = eventNameToMatrixIndex.get(secondEvent);
	return footprint[rowIndex][colIndex];
    }

    /**
     * @param firstEvent
     *            name of one event (i.e. a)
     * @param secondEvent
     *            name of another event (i.e. b)
     * @return false if a # b, true otherwise
     */
    public boolean areConnected(String firstEvent, String secondEvent) {
	return getRelationType(firstEvent, secondEvent) != RelationType.NOT_CONNECTED;
    }

    /**
     * @param firstEvent
     *            name of one event (i.e. a)
     * @param secondEvent
     *            name of another event (i.e. b)
     * @return true if a>b, false otherwise
     */
    public boolean isFirstFollowedBySecond(String firstEvent, String secondEvent) {
	return getRelationType(firstEvent, secondEvent) == RelationType.PRECEDES;
    }

    public boolean areEventsConnected(Set<String> inputEvents,
	    Set<String> outputEvents) {
	/*
	 * (A,B), A = first, B = second
	 */
	// For every a1,a2 in A => a1#a2
	String arrayFirst[] = inputEvents.toArray(new String[] {});
	for (int i = 0; i < arrayFirst.length - 1; i++) {
	    for (int j = i + 1; j < arrayFirst.length; j++) {
		if (areConnected(arrayFirst[i], arrayFirst[j])) {
		    return false;
		}
	    }
	}
	// For every b1, b2 in B => b1#b2
	String[] arraySecond = outputEvents.toArray(new String[] {});
	for (int i = 0; i < arraySecond.length - 1; i++) {
	    for (int j = i + 1; j < arraySecond.length; j++) {
		if (areConnected(arraySecond[i], arraySecond[j])) {
		    return false;
		}
	    }
	}
	// For every a in A and b in B => a > b in f
	for (int i = 0; i < arrayFirst.length; i++) {
	    for (int j = 0; j < arraySecond.length; j++) {
		if (!isFirstFollowedBySecond(arrayFirst[i], arraySecond[j])) {
		    return false;
		}
	    }
	}
	return true;
    }

    @Override
    public String toString() {
	String toReturn = "  ";
	String[] tmp = new String[footprint.length];
	for (String key : eventNameToMatrixIndex.keySet()) {
	    tmp[eventNameToMatrixIndex.get(key)] = key;
	}
	for (int i = 0; i < footprint.length; i++) {
	    toReturn += tmp[i] + " ";
	}
	toReturn += "\n";
	for (int i = 0; i < footprint.length; i++) {
	    toReturn += tmp[i] + " ";
	    for (int j = 0; j < footprint.length; j++) {
		toReturn += footprint[i][j].symbol() + " ";
	    }
	    toReturn += "\n";
	}
	return toReturn;
    }

}
