package edu.fcse.alphaalgorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author blagoj atanasovski
 * 
 */
public class WorkflowNetContainer {
	Footprint footprint;
	Set<Place> places;
	Set<Trace> eventsLog;
	Set<String> events;// Xl
	Set<String> startingEvents;// Xi
	Set<String> endingEvents;// Xo
	Set<Place> workflowPlaces;// Yl

	public WorkflowNetContainer(Set<Trace> eventsLog) {
		this.eventsLog = eventsLog;
		this.events = new HashSet<>();
		this.startingEvents = new HashSet<>();
		this.endingEvents = new HashSet<>();
		extractEvents(eventsLog, this.events, this.startingEvents,
				this.endingEvents);
		System.out.println("Events:" + events);
		footprint = new Footprint(events, eventsLog);
		System.out.println(footprint);
		Set<Place> XL = getPlacesFromFootprint();
		Set<Place> YL = reducePlaces(XL);
		workflowPlaces = YL;
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
		Set<Set<String>> powerSet = Utils.powerSet(events);
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
			Place potentialPlace1=potentialPlaces[i];
			for (int j = i + 1; j < potentialPlaces.length; j++) {
				if (potentialPlace1.getInPlaces().containsAll(potentialPlaces[j].getInPlaces())) {
					if (potentialPlaces[i].getOutPlaces().containsAll(
							potentialPlaces[j].getOutPlaces())) {
						toRemove.add(potentialPlaces[j]);
						continue;
					}
				}
				if (potentialPlaces[j].getInPlaces().containsAll(potentialPlaces[i].getInPlaces())) {
					if (potentialPlaces[j].getOutPlaces().containsAll(
							potentialPlaces[i].getOutPlaces())) {
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
}
