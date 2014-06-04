package edu.fcse.alphaalgorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.fcse.alphaalgorithm.tools.Footprint;
import edu.fcse.alphaalgorithm.tools.Place;
import edu.fcse.alphaalgorithm.tools.Trace;

public class Validate {
	private static Map<Integer, Trace> logMap;
	private static int[] rangeMapper = new int[21];
	private static final int TOTAL_CASES = 1391;
	private static final int FOLDS = 5;

	private static void createLog() {
		int i = 0;
		logMap = new HashMap<>();
		logMap.put(0, new Trace(new String[] { "a", "c", "d", "e", "h" }));
		rangeMapper[i++] = 455;
		logMap.put(1, new Trace(new String[] { "a", "b", "d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 191;
		i++;
		logMap.put(2, new Trace(new String[] { "a", "d", "c", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 177;
		i++;
		logMap.put(3, new Trace(new String[] { "a", "b", "d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 144;
		i++;
		logMap.put(4, new Trace(new String[] { "a", "c", "d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 111;
		i++;
		logMap.put(5, new Trace(new String[] { "a", "d", "c", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 82;
		i++;
		logMap.put(6, new Trace(new String[] { "a", "d", "b", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 56;
		i++;
		logMap.put(7, new Trace(new String[] { "a", "c", "d", "e", "f", "d",
				"b", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 47;
		i++;
		logMap.put(8, new Trace(new String[] { "a", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 38;
		i++;
		logMap.put(9, new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 33;
		i++;
		logMap.put(10, new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 14;
		i++;
		logMap.put(11, new Trace(new String[] { "a", "c", "d", "e", "f", "d",
				"b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 11;
		i++;
		logMap.put(12, new Trace(new String[] { "a", "d", "c", "e", "f", "c",
				"d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 9;
		i++;
		logMap.put(13, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 8;
		i++;
		logMap.put(14, new Trace(new String[] { "a", "d", "c", "e", "f", "b",
				"d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 5;
		i++;
		logMap.put(15, new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "f", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 3;
		i++;
		logMap.put(16, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 2;
		i++;
		logMap.put(17, new Trace(new String[] { "a", "d", "c", "e", "f", "b",
				"d", "e", "f", "b", "d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 2;
		i++;
		logMap.put(18, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "f", "b", "d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 1;
		i++;
		logMap.put(19, new Trace(new String[] { "a", "d", "b", "e", "f", "b",
				"d", "e", "f", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 1;
		i++;
		logMap.put(20, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "f", "c", "d", "e", "f", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 1;
		i++;
	}

	private static Set<Trace>[] getFolds() {
		//Actually I checked, double checked
		@SuppressWarnings("unchecked")
		Set<Trace>[] folds = new Set[FOLDS];
		Set<Integer> pickedCases = new HashSet<>();
		Random r = new Random();
		for (int i = 0; i < FOLDS; i++) {
			folds[i] = new HashSet<>();
			for (int j = 0; j < TOTAL_CASES / FOLDS; j++) {
				int index = -1;
				do {
					index = r.nextInt(TOTAL_CASES);
				} while (pickedCases.contains(index));
				pickedCases.add(index);
				int k = 0;
				while (k < rangeMapper.length && index >= rangeMapper[k]) {
					k++;
				}
				folds[i].add(logMap.get(k));
			}
		}
		return folds;
	}

	public static void validateAlgorithm() {
		createLog();
		Set<Trace>[] folds = getFolds();
		for (int i = 0; i < folds.length; i++) {
			Set<Trace> validationSet = folds[i];
			Set<Trace> constructionSet = new HashSet<Trace>();
			for (int j = 0; j < folds.length; j++) {
				if (j == i) {
					continue;
				}
				constructionSet.addAll(folds[j]);
			}
			WorkflowNetCreator wfC = new WorkflowNetCreator(constructionSet);
			Place.nameGenerator='A';
			boolean passes = true;
			System.out.println("Fold: " + i);
			System.out.println("Construction set count: "
					+ constructionSet.size());
			System.out.println("Validation set count: " + validationSet.size());
			int fails = 0;
			for (Trace t : validationSet) {
				passes = wfC.runTrace(t);
				if (!passes) {
					fails++;
				}
			}
			System.out.println("Failed validation traces: "+fails);
			
		}
	}
}
