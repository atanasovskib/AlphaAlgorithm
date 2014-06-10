package edu.fcse.alphaalgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.fcse.alphaalgorithm.tools.Footprint;
import edu.fcse.alphaalgorithm.tools.LoopLengthOne;
import edu.fcse.alphaalgorithm.tools.Pair;
import edu.fcse.alphaalgorithm.tools.Place;
import edu.fcse.alphaalgorithm.tools.Trace;

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
	public static boolean takeInAccountLoopsLengthTwo = true;

	public Set<LoopLengthOne> recordedLLOs;

	Footprint footprint;
	Set<Trace> eventsLog;
	Set<String> activityList;// Xl
	Set<String> startingActivities;// Xi
	Set<String> endingActivities;// Xo
	Set<Place> workflowPlaces;// Pl
	Set<Pair<String, Place>> eventToPlaceTransitions;
	Map<String, Set<Place>> eventToPlaceTransitionsMap;
	Set<Pair<Place, String>> placeToEventTransitions;
	Map<Place, Set<String>> placeToEventTransitionsMap;

	Map<String, Pair<Set<Place>, Set<Place>>> activityPrePostMap;
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
		this.activityList = new HashSet<>();
		this.startingActivities = new HashSet<>();
		this.endingActivities = new HashSet<>();
		// ProcessMining book page 133
		// Steps 1,2,3
		extractActivities(eventsLog, this.activityList,
				this.startingActivities, this.endingActivities);
		// Generate footprint matrix from eventsLog
		footprint = new Footprint(activityList, eventsLog,
				takeInAccountLoopsLengthTwo);
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
		postProcessWF();
		// Step 7 create transitions
		createEventToPlaceTransitions();
		createPlaceToEventTransitions();
		// Step 6
		connectSourceAndSink();
		createActivityPrePostMap();
		System.out.println(prepareWFforPrint());
	}

	private void createActivityPrePostMap() {
		activityPrePostMap=new HashMap<>();
		for(String activity:activityList){
			Set<Place> first=new HashSet<Place>();
			Set<Place> second=new HashSet<Place>();
			activityPrePostMap.put(activity, new Pair<Set<Place>, Set<Place>>(first, second));
		}
		for(Place p:workflowPlaces){
			Set<String> inA=p.getInActivities();
			for(String activity:inA){
				Pair<Set<Place>,Set<Place>> pair=activityPrePostMap.get(activity);
				pair.getSecond().add(p);
			}
			Set<String> outA=p.getOutActivities();
			for(String activity:outA){
				Pair<Set<Place>,Set<Place>> pair=activityPrePostMap.get(activity);
				pair.getFirst().add(p);
			}
		}
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
	private void extractActivities(Set<Trace> eventLog, Set<String> allEvents,
			Set<String> startingEvents, Set<String> endingEvents) {
		allEvents.clear();
		startingEvents.clear();
		endingEvents.clear();
		for (Trace singleTrace : eventLog) {
			startingEvents.add(singleTrace.getFirstEvent());
			endingEvents.add(singleTrace.getLastEvent());
			allEvents.addAll(singleTrace.getActivitiesList());
		}
	}

	/**
	 * 
	 * @return XL, a set of places, each place has input and output events, this
	 *         set can be reduced to YL by the method {@link #reducePlaces(Set)}
	 */
	private Set<Place> getPlacesFromFootprint() {
		Set<Place> xl = new HashSet<>();
		System.out.println("Getting places from footprint:");
		Set<Set<String>> powerSet = Utils.powerSet(activityList);
		System.out.println("Got powerSet: "+powerSet.size());
		powerSet.remove(new HashSet<>());
		@SuppressWarnings("unchecked")
		Set<String>[] array = powerSet.toArray(new Set[] {});
		System.out.println("Power set cast to array");
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
		System.out.println("Places (XL) created from footprint. # of places in XL: "+xl.size());
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
				if (potentialPlace1.getInActivities().containsAll(
						potentialPlaces[j].getInActivities())) {
					if (potentialPlaces[i].getOutActivities().containsAll(
							potentialPlaces[j].getOutActivities())) {
						toRemove.add(potentialPlaces[j]);
						continue;
					}
				}
				if (potentialPlaces[j].getInActivities().containsAll(
						potentialPlaces[i].getInActivities())) {
					if (potentialPlaces[j].getOutActivities().containsAll(
							potentialPlaces[i].getOutActivities())) {
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
		eventToPlaceTransitions = new HashSet<>();
		eventToPlaceTransitionsMap = new HashMap<>();
		for (String event : activityList) {
			Set<Place> eventToPlace = new HashSet<>();
			eventToPlaceTransitionsMap.put(event, eventToPlace);
			for (Place place : this.workflowPlaces) {
				if (place.getInActivities().contains(event)) {
					eventToPlaceTransitions.add(new Pair<>(event, place));
					eventToPlace.add(place);
				}
			}
		}
	}

	private void createPlaceToEventTransitions() {
		placeToEventTransitions = new HashSet<>();
		for (String event : activityList) {
			for (Place place : this.workflowPlaces) {
				if (place.getOutActivities().contains(event)) {
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
		for (String startEvent : startingActivities) {
			in.addOutEvent(startEvent);
			placeToEventTransitions
					.add(new Pair<Place, String>(in, startEvent));
		}
		for (String endEvent : endingActivities) {
			out.addInEvent(endEvent);
			eventToPlaceTransitions.add(new Pair<String, Place>(endEvent, out));
		}
		workflowPlaces.add(in);
		workflowPlaces.add(out);
	}

	private String prepareWFforPrint() {
		StringBuilder sb = new StringBuilder(
				40
						* (placeToEventTransitions.size() + eventToPlaceTransitions
								.size()) + activityList.size() + 15
						* workflowPlaces.size());
		sb.append("Events:\n");
		for (String event : activityList) {
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
		List<String> events = singleTrace.getActivitiesList();
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
			List<String> eventsList = singleTrace.getActivitiesList();
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

	private void postProcessWF() {
		Queue<LoopLengthOne> lloQueue = new LinkedList<>(recordedLLOs);
		while (!lloQueue.isEmpty()) {
			LoopLengthOne llo = lloQueue.poll();
			String in = llo.getPrevAction();
			String out = llo.getNextAction();
			boolean used = false;
			for (Place place : workflowPlaces) {
				if (place.getInActivities().contains(in)
						&& place.getOutActivities().contains(out)) {
					place.addInEvent(llo.getLoopedAction());
					place.addOutEvent(llo.getLoopedAction());
					activityList.add(llo.getLoopedAction());
					used = true;
					// NOT SURE ABOUT THIS
					break;
				}
			}
			if (!used) {
				lloQueue.add(llo);
			}
		}
	}

	public boolean runTrace(Trace trace) {
		for(Place p:workflowPlaces){
			p.takeToken();
		}
		List<String> currentTrace = trace.getActivitiesList();
		in.putToken();
		for (String currentActivity : currentTrace) {
			// if out place has a token but trace is not finished
			if (out.hasToken()) {
				return false;
			}
			Pair<Set<Place>, Set<Place>> actPrePost = activityPrePostMap
					.get(currentActivity);
			//activity not found in construction set event log
			if(actPrePost==null){
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
				for (Place p : inPlaces) {
					p.takeToken();
				}
				for (Place p : actPrePost.getSecond()) {
					p.putToken();
				}
			}
		}
		if (out.hasToken()) {
			int numTokens = 0;
			for (Place p : workflowPlaces) {
				if (p.hasToken()) {
					numTokens++;
				}
			}
			if (numTokens != 1) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
