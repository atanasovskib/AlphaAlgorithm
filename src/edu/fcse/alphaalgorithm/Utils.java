package edu.fcse.alphaalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fcse.alphaalgorithm.tools.Trace;

public class Utils {
	/**
	 * @param originalSet
	 * @return a set of all the subsets of originalSet
	 */
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

	public static Set<Trace> demoL7eventLog() {
		Set<Trace> eventLog = new HashSet<Trace>();
		eventLog.add(new Trace(new String[] { "a", "c" }));
		eventLog.add(new Trace(new String[] { "a", "b", "c" }));
		eventLog.add(new Trace(new String[] { "a", "b", "b", "c" }));
		eventLog.add(new Trace(new String[] { "a", "b", "b", "b", "c" }));
		return eventLog;
	}

	public static Set<Trace> demoLLTeventLog() {
		Set<Trace> eventLog = new HashSet<Trace>();
		eventLog.add(new Trace(new String[] { "x", "a", "y" }));
		eventLog.add(new Trace(new String[] { "x", "a", "b", "y" }));
		eventLog.add(new Trace(new String[] { "x", "w", }));
		eventLog.add(new Trace(new String[] { "z", "b", "w" }));
		eventLog.add(new Trace(new String[] { "z", "b", "a", "y" }));
		eventLog.add(new Trace(new String[] { "z", "y" }));
		eventLog.add(new Trace(new String[] { "z", "a", "b", "a", "y" }));

		return eventLog;
	}

	public static Set<Trace> chapter7EventLog() {
		Set<Trace> eventLog = new HashSet<>();
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "b", "d", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "b", "d", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "b", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "f", "d",
				"b", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "d", "b", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "f", "d",
				"b", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "c",
				"d", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "b",
				"d", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "f", "d", "b", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "b",
				"d", "e", "f", "b", "d", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "f", "b", "d", "e", "h" }));
		eventLog.add(new Trace(new String[] { "a", "d", "b", "e", "f", "b",
				"d", "e", "f", "d", "b", "e", "g" }));
		eventLog.add(new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "f", "c", "d", "e", "f", "d", "b", "e", "g" }));
		return eventLog;
	}

	/**
	 * Parses an XES file and returns an event log as a Set of Traces.
	 * 
	 * @param fileName
	 *            location of XES file
	 * @return a set of Traces, an event log
	 * @throws IOException
	 *             if errors occur while reading the XES file
	 */
	public static Set<Trace> parseXESFile(String fileName) throws IOException {
		Document doc = Jsoup.parse(new File(fileName), "UTF-8");
		Elements traces = doc.getElementsByTag("trace");
		Set<Trace> eventLog = new HashSet<>();
		for (int i = 0; i < traces.size(); i++) {
			Element currentTrace = traces.get(i);
			Elements events = currentTrace.getElementsByTag("event");
			Trace newTrace = new Trace();
			for (int j = 0; j < events.size(); j++) {
				Element currentEvent = events.get(j);
				Elements name = currentEvent.getElementsByAttributeValue("key",
						"concept:name");
				Element nameN = name.get(0);
				Elements lifec = currentEvent.getElementsByAttributeValue(
						"key", "lifecycle:transition");
				Element lifc = lifec.get(0);
				newTrace.addEvent(nameN.attr("value") + " "
						+ lifc.attr("value"));
			}
			eventLog.add(newTrace);
		}
		return eventLog;
	}
}
