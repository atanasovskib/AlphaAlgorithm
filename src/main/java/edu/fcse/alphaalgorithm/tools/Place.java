package edu.fcse.alphaalgorithm.tools;

import java.util.Set;

public class Place {
	// (Input Activities, Output Activities)
	Pair<Set<String>, Set<String>> activitiesPair;
	/**
	 * The name of the place is merely symbolic, plays no part in the
	 * identification of the Place. See {@link #equals(Object)}
	 */
	String name;
	public static char nameGenerator = 'A';

	private boolean token;

	public Place(Set<String> in, Set<String> out) {
		activitiesPair = new Pair<>(in, out);
		name = "" + nameGenerator++;
	}

	public Place(String name, Set<String> in, Set<String> out) {
		activitiesPair = new Pair<>(in, out);
		this.name = name;
	}

	public Set<String> getInActivities() {
		return activitiesPair.getFirst();
	}

	public Set<String> getOutActivities() {
		return activitiesPair.getSecond();
	}

	@Override
	public String toString() {
		String toReturn = name + ": " + activitiesPair;
		return toReturn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activitiesPair == null) ? 0 : activitiesPair.hashCode());
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
		if (activitiesPair == null) {
			if (other.activitiesPair != null)
				return false;
		} else if (!activitiesPair.equals(other.activitiesPair))
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
		if (getInActivities().containsAll(potentialSubplace.getInActivities())) {
			if (getOutActivities().containsAll(
					potentialSubplace.getOutActivities())) {
				return true;
			}
		}
		return false;
	}

	public void addInEvent(String activityName) {
		activitiesPair.getFirst().add(activityName);
	}

	public void addOutEvent(String activityName) {
		activitiesPair.getSecond().add(activityName);
	}

	public String getName() {
		return name;
	}

	public boolean hasToken() {
		return token;
	}

	public void putToken() {
		token = true;
	}

	public void takeToken() {
		token = false;
	}
}
