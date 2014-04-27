package edu.fcse.alphaalgorithm;

import java.util.Set;

public class Place {
	Pair<Set<String>, Set<String>> eventsPair;
	String name;
	private static char nameGenerator='A';
	public Place(Set<String> in, Set<String> out) {
		eventsPair = new Pair<>(in, out);
		name=""+nameGenerator++;
	}
	public Place(String name,Set<String> in,Set<String> out){
		eventsPair=new Pair<>(in, out);
		this.name=name;
	}
	public Set<String> getInEvents() {
		return eventsPair.getFirst();
	}

	public Set<String> getOutEvents() {
		return eventsPair.getSecond();
	}

	@Override
	public String toString() {
		String toReturn =name+": "+eventsPair+"\n";
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

	public boolean isSuperPlace(Place potentialSubplace) {
		if (getInEvents().containsAll(potentialSubplace.getInEvents())) {
			if (getOutEvents().containsAll(potentialSubplace.getOutEvents())) {
				return true;
			}
		}
		return false;
	}
	public void addInEvent(String eventName){
		eventsPair.getFirst().add(eventName);
	}
	public void addOutEvent(String eventName){
		eventsPair.getSecond().add(eventName);
	}
}
