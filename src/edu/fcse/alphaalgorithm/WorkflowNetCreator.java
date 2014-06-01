package edu.fcse.alphaalgorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Creates a Workflow Net Model from an eventLog.
 * @author blagoj atanasovski
 * 
 */
/**
 * @author blagoj
 * 
 */
public class WorkflowNetCreator {
	public static boolean takeInAccountLoopsLengthTwo=true;

	public Set<LoopLengthOne> recordedLLOs;

	Footprint footprint;
	Set<Trace> eventsLog;
	Set<String> eventsList;// Xl
	Set<String> startingEvents;// Xi
	Set<String> endingEvents;// Xo
	Set<Place> workflowPlaces;// Pl
	Set<Pair<String, Place>> eventToPlaceTransitions;
	Set<Pair<Place, String>> placeToEventTransitions;

	// Source place
	Place in = new Place("in", new HashSet<String>(), new HashSet<String>());

	// Sink place
	Place out = new Place("out", new HashSet<String>(), new HashSet<String>());

	public WorkflowNetCreator(Set<Trace> eventsLog) {
		Set<Trace> preprocessedEventsLog = new HashSet<>();
		for (Trace trace : eventsLog) {
			preprocessedEventsLog.add(preprocessOLLs(trace));
		}
		this.eventsLog = preprocessedEventsLog;
		this.eventsList = new HashSet<>();
		this.startingEvents = new HashSet<>();
		this.endingEvents = new HashSet<>();
		// ProcessMining book page 133
		// Steps 1,2,3
		extractEvents(eventsLog, this.eventsList, this.startingEvents,
				this.endingEvents);
		// Generate footprint matrix from eventsLog
		footprint = new Footprint(eventsList, eventsLog, takeInAccountLoopsLengthTwo);
		System.out.println("------------------------");
		System.out.println("Footprint matrix:");
		System.out.println(footprint);
		System.out.println("------------------------");
		// Step 4 generate places
		Set<Place> XL = getPlacesFromFootprint();
		// Step 5 reduce places, no places that are subsets of other places
		Set<Place> YL = reducePlaces(XL);
		// all the places except start and sink
		workflowPlaces = YL;
		// Step 7 create transitions
		postProcessWF();
		createEventToPlaceTransitions();
		createPlaceToEventTransitions();
		// Step 6
		connectSourceAndSink();
		System.out.println(prepareWFforPrint());
	}

	/**
	 * @param eventLog
	 *            a set of traces from where the event names are extracted
	 * @param allEvents
	 *            a set that after this method will contain the names of the
	 *            events present in the eventsLog (Xl)
	 * @param startingEvents
	 *            a set of the starting events, events with which the traces
	 *            start with (Xi)
	 * @param endingEvents
	 *            a set of ending events, events that the traces end with (Xo)
	 */
	private void extractEvents(Set<Trace> eventLog, Set<String> allEvents,
			Set<String> startingEvents, Set<String> endingEvents) {
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
	 * 
	 * @return XL, a set of places, each place has input and output events, this
	 *         set can be reduced to YL by the method {@link #reducePlaces(Set)}
	 */
	private Set<Place> getPlacesFromFootprint() {
		Set<Place> xl = new HashSet<>();
		Set<Set<String>> powerSet = Utils.powerSet(eventsList);
		powerSet.remove(new HashSet<>());
		@SuppressWarnings("unchecked")
		Set<String>[] array = powerSet.toArray(new Set[] {});
		for (int i = 0; i < array.length; i++) {
			Set<String> first = array[i];
			for (int j = 0; j < array.length; j++) {
				if (i == j) {
					continue;
				}
				Set<String> second = array[j];
				if (footprint.areEventsConnected(first, second)) {
					xl.add(new Place(first, second));
				}
			}
		}
		return xl;
	}

	/**
	 * @param Xl
	 *            the result from step 4 of the algorithm
	 * @return Yl the result from step 5 of the algorithm, all subset Places
	 *         removed from Xl
	 */
	private Set<Place> reducePlaces(Set<Place> xl) {
		Set<Place> toRemove = new HashSet<Place>();
		Place[] potentialPlaces = xl.toArray(new Place[] {});
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

	public Set<Place> getWorkflowPlaces() {
		return workflowPlaces;
	}

	public void createEventToPlaceTransitions() {
		eventToPlaceTransitions = new HashSet<Pair<String, Place>>();
		for (String event : eventsList) {
			for (Place place : this.workflowPlaces) {
				if (place.getInEvents().contains(event)) {
					eventToPlaceTransitions.add(new Pair<>(event, place));
				}
			}
		}
	}

	private void createPlaceToEventTransitions() {
		placeToEventTransitions = new HashSet<>();
		for (String event : eventsList) {
			for (Place place : this.workflowPlaces) {
				if (place.getOutEvents().contains(event)) {
					placeToEventTransitions.add(new Pair<>(place, event));
				}
			}
		}
	}

	/**
	 * Source and Sink places are not connected after the transitions are
	 * created between the other events.
	 */
	private void connectSourceAndSink() {
		for (String startEvent : startingEvents) {
			in.addOutEvent(startEvent);
			placeToEventTransitions
					.add(new Pair<Place, String>(in, startEvent));
		}
		for (String endEvent : endingEvents) {
			out.addInEvent(endEvent);
			eventToPlaceTransitions.add(new Pair<String, Place>(endEvent, out));
		}
	}

	private String prepareWFforPrint() {
		StringBuilder sb = new StringBuilder(
				40
						* (placeToEventTransitions.size() + eventToPlaceTransitions
								.size()) + eventsList.size() + 15
						* workflowPlaces.size());
		sb.append("Events:\n");
		for (String event : eventsList) {
			sb.append(event + ", ");
		}
		sb.append("\nPlaces:\n");
		sb.append(in).append("\n");
		for (Place place : workflowPlaces) {
			sb.append(place).append("\n");
		}
		sb.append(out);
		sb.append("\n");
		sb.append("Transitions:\n");
		for (Pair<Place, String> transition : placeToEventTransitions) {
			sb.append(String.format("From place (%s) to event [%s]\n",
					transition.getFirst(), transition.getSecond()));
		}
		for (Pair<String, Place> transition : eventToPlaceTransitions) {
			sb.append(String.format("From event [%s] to place (%s)\n",
					transition.getFirst(), transition.getSecond()));
		}
		return sb.toString();
	}

	private int checkForCycleLengthOne(Trace singleTrace) {
		List<String> events = singleTrace.getEventsList();
		for (int i = 0; i < events.size() - 1; i++) {
			if (events.get(i).equals(events.get(i + 1))) {
				return i;
			}
		}
		return -1;
	}

	private Trace preprocessOLLs(Trace singleTrace) {
		if (recordedLLOs == null) {
			recordedLLOs = new HashSet<>();
		}
		int start = -1;
		if ((start = checkForCycleLengthOne(singleTrace)) != -1) {
			List<String> eventsList = singleTrace.getEventsList();
			int prev = start - 1;
			int i = start;
			for (; i < eventsList.size() - 1
					&& eventsList.get(i).equals(eventsList.get(i + 1)); i++)
				;
			i++;
			LoopLengthOne llo = new LoopLengthOne(eventsList.get(prev),
					eventsList.get(start), eventsList.get(i));
			recordedLLOs.add(llo);
		}
		return singleTrace;
	}
	private Trace pruneOLLSBackup(Trace singleTrace) {
		if (recordedLLOs == null) {
			recordedLLOs = new HashSet<>();
		}
		Trace newTrace;
		int start = -1;
		while ((start = checkForCycleLengthOne(singleTrace)) != -1) {

			newTrace = new Trace();
			List<String> eventsList = singleTrace.getEventsList();
			int prev = start - 1;
			int i = 0;
			for (; i < start; i++) {
				newTrace.addEvent(eventsList.get(i));
			}
			for (; i < eventsList.size() - 1
					&& eventsList.get(i).equals(eventsList.get(i + 1)); i++)
				;
			i++;
			LoopLengthOne llo = new LoopLengthOne(eventsList.get(prev),
					eventsList.get(start), eventsList.get(i));
			recordedLLOs.add(llo);
			for (; i < eventsList.size(); i++) {
				newTrace.addEvent(eventsList.get(i));
			}
			singleTrace = newTrace;
		}
		return singleTrace;
	}
	private void postProcessWF() {
		Queue<LoopLengthOne> lloQueue = new LinkedList<>(recordedLLOs);
		while (!lloQueue.isEmpty()) {
			LoopLengthOne llo = lloQueue.poll();
			String in = llo.getPrevAction();
			String out = llo.getNextAction();
			boolean used = false;
			for (Place place : workflowPlaces) {
				if (place.getInEvents().contains(in)
						&& place.getOutEvents().contains(out)) {
					place.addInEvent(llo.getLoopedAction());
					place.addOutEvent(llo.getLoopedAction());
					eventsList.add(llo.getLoopedAction());
					used = true;
					//NOT SURE ABOUT THIS
					break;
				}
			}
			if (!used) {
				lloQueue.add(llo);
			}
		}
	}

}
