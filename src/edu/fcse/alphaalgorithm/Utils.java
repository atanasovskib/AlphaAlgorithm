package edu.fcse.alphaalgorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
	public static <E> Set<Set<E>> powerSet(Set<E> originalSet) {
		Set<Set<E>> sets = new HashSet<Set<E>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<E>());
			return sets;
		}
		List<E> list = new ArrayList<>(originalSet);
		E head = list.get(0);
		Set<E> rest = new HashSet<>(list.subList(1, list.size()));
		for (Set<E> set : powerSet(rest)) {
			Set<E> newSet = new HashSet<E>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}
}
