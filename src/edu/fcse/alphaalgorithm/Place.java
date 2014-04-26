package edu.fcse.alphaalgorithm;

import java.util.Set;

public class Place {
	Pair<Set<String>, Set<String>> placePair;

	public Place(Set<String> in, Set<String> out) {
		placePair = new Pair<>(in, out);
	}

	public Set<String> getInPlaces() {
		return placePair.getFirst();
	}

	public Set<String> getOutPlaces() {
		return placePair.getSecond();
	}

	@Override
	public String toString() {
		return placePair.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((placePair == null) ? 0 : placePair.hashCode());
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
		if (placePair == null) {
			if (other.placePair != null)
				return false;
		} else if (!placePair.equals(other.placePair))
			return false;
		return true;
	}

	public boolean isSuperPlace(Place potentialSubplace) {
		if (getInPlaces().containsAll(potentialSubplace.getInPlaces())) {
			if (getOutPlaces().containsAll(potentialSubplace.getOutPlaces())) {
				return true;
			}
		}
		return false;
	}
}
