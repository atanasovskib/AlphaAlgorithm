package edu.fcse.alphaalgorithm.tools;

import java.util.Set;

public class Place {
	// (Input Events, Output Events)
	Pair<Set<String>, Set<String>> eventsPair;
	/**
	 * The name of the place is merely symbolic, plays no part in the
	 * identification of the Place. See {@link #equals(Object)}
	 */
	String name;
	private static char nameGenerator = 'A';

	public Place(Set<String> in, Set<String> out) {
		eventsPair = new Pair<>(in, out);
		name = "" + nameGenerator++;
	}

	public Place(String name, Set<String> in, Set<String> out) {
		eventsPair = new Pair<>(in, out);
		this.name = name;
	}

	public Set<String> getInEvents() {
		return eventsPair.getFirst();
	}

	public Set<String> getOutEvents() {
		return eventsPair.getSecond();
	}

	@Override
	public String toString() {
		String toReturn = name + ": " + eventsPair;
		return toReturn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventsPair == null) ? 0 : eventsPair.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (eventsPair == null) {
			if (other.eventsPair != null)
				return false;
		} else if (!eventsPair.equals(other.eventsPair))
			return false;
		return true;
	}

	/**
	 * Required for step 5 in the Alpha Algorithm (Generating Yl). Check if a
	 * the input and output places are supersets to the input and output places
	 * of another place. Place A with input events {a} and output events {b,e}
	 * is a superplace of Place B with input {a} and output {b} => Place B can
	 * be discarded.
	 * 
	 * @param potentialSubplace
	 * @return true if potentialSubplace.inEvents ⊆ this.inEvents &&
	 *         potentialSubplace.outEvents ⊆ this.outEvents
	 */
	public boolean isSuperPlace(Place potentialSubplace) {
		if (getInEvents().containsAll(potentialSubplace.getInEvents())) {
			if (getOutEvents().containsAll(potentialSubplace.getOutEvents())) {
				return true;
			}
		}
		return false;
	}

	public void addInEvent(String eventName) {
		eventsPair.getFirst().add(eventName);
	}

	public void addOutEvent(String eventName) {
		eventsPair.getSecond().add(eventName);
	}
	public String getName(){
		return name;
	}
}
