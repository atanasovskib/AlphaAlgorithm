package edu.fcse.alphaalgorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author blagoj atanasovski
 * 
 */
public class WorkflowNetContainer {
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

	public WorkflowNetContainer(Set<Trace> eventsLog) {
		this.eventsLog = eventsLog;
		this.eventsList = new HashSet<>();
		this.startingEvents = new HashSet<>();
		this.endingEvents = new HashSet<>();
		// ProcessMining book page 133
		// Steps 1,2,3
		extractEvents(eventsLog, this.eventsList, this.startingEvents,
				this.endingEvents);

		System.out.println("Events in workflow net:" + eventsList);
		// Generate footprint matrix from eventsLog
		footprint = new Footprint(eventsList, eventsLog);
		System.out.println("------------------------");
		System.out.println("Footprint matrix:");
		System.out.println(footprint);

		// Step 4
		Set<Place> XL = getPlacesFromFootprint();
		// Step 5
		Set<Place> YL = reducePlaces(XL);
		// all the places except start and sink
		workflowPlaces = YL;
		// Step 7
		createEventToPlaceTransitions();
		createPlaceToEventTransitions();
		// Step 6
//		workflowPlaces.add(in);
//		workflowPlaces.add(out);
		connectSourceAndSink();

		System.out.println("Workflow net places:");
		System.out.println(workflowPlaces);
		System.out.println(in+""+out);
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
	 *         set can be reduced to YL by the method
	 *         WorkflowNetContainer.reducePlaces
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
}
