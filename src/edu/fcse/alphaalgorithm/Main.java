package edu.fcse.alphaalgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Main {
	public static void main(String args[]) {
		String fileName = "input";
		// Set<Trace> eventLog = readInputFromCSV(fileName);
		Set<Trace> eventLog = demoL1eventLog();
		WorkflowNetContainer wfContainer = new WorkflowNetContainer(eventLog);
		System.out.println("--------");
		for (Place p : wfContainer.getWorkflowPlaces()) {
			System.out.println(p);
		}
	}
	private static Set<Trace> demoL1eventLog(){
		Set<Trace> eventLog = new HashSet<Trace>();
		eventLog.add(new Trace(new String[] { "a", "b", "c", "d" }));
		eventLog.add(new Trace(new String[] { "a", "c", "b", "d" }));
		eventLog.add(new Trace(new String[] { "a", "e", "d" }));
		return eventLog;
	}
	private static Set<Trace> readInputFromCSV(String fileName) {
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
}
