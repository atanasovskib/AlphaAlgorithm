package edu.fcse.alphaalgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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

	public static Set<Trace> readInputFromCSV(String fileName) {
		Charset charset = Charset.forName("US-ASCII");
		Path file = FileSystems.getDefault().getPath(fileName);
		Set<Trace> toReturn = new HashSet<>();
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] events = line.split(",");
				if (events == null || events.length == 0) {
					throw new IOException(
							"Input file not in correct format, empty line read");
				}
				toReturn.add(new Trace(events));
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
			return new HashSet<Trace>();
		}
		return toReturn;
	}

	public static Set<Trace> demoL1eventLog() {
		Set<Trace> eventLog = new HashSet<Trace>();
		eventLog.add(new Trace(new String[] { "a", "b", "c", "d" }));
		eventLog.add(new Trace(new String[] { "a", "c", "b", "d" }));
		eventLog.add(new Trace(new String[] { "a", "e", "d" }));
		return eventLog;
	}

	public static Set<Trace> demoL2eventLog() {
		Set<Trace> eventLog = new HashSet<Trace>();
		eventLog.add(new Trace(new String[] { "a", "b", "c", "d" }));
		eventLog.add(new Trace(new String[] { "a", "c", "b", "d" }));
		eventLog.add(new Trace(new String[] { "a", "b", "c", "e", "f", "b",
				"c", "d" }));
		eventLog.add(new Trace(new String[] { "a", "b", "c", "e", "f", "c",
				"b", "d" }));
		eventLog.add(new Trace(new String[] { "a", "c", "b", "e", "f", "b",
				"c", "d" }));
		eventLog.add(new Trace(new String[] { "a", "c", "b", "e", "f", "b",
				"c", "e", "f", "c", "b", "d" }));
		return eventLog;
	}

}
