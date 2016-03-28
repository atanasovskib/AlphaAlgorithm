package edu.fcse.alphaalgorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.fcse.alphaalgorithm.tools.Place;
import edu.fcse.alphaalgorithm.tools.Trace;

public class Validate {
	private static Map<Integer, Trace> logMap;
	private static int[] rangeMapper = new int[21];
	private static final int TOTAL_CASES = 1391;
	private static final int FOLDS = 5;
	private static int[] amountMapper = new int[121];

	private static void createLog() {
		int i = 0;
		logMap = new HashMap<>();
		logMap.put(0, new Trace(new String[] { "a", "c", "d", "e", "h" }));
		rangeMapper[i] = 455;
		amountMapper[i] = 455;
		i++;
		logMap.put(1, new Trace(new String[] { "a", "b", "d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 191;
		amountMapper[i] = 191;
		i++;
		logMap.put(2, new Trace(new String[] { "a", "d", "c", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 177;
		amountMapper[i] = 177;
		i++;
		logMap.put(3, new Trace(new String[] { "a", "b", "d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 144;
		amountMapper[i] = 144;
		i++;
		logMap.put(4, new Trace(new String[] { "a", "c", "d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 111;
		amountMapper[i] = 111;
		i++;
		logMap.put(5, new Trace(new String[] { "a", "d", "c", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 82;
		amountMapper[i] = 82;
		i++;
		logMap.put(6, new Trace(new String[] { "a", "d", "b", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 56;
		amountMapper[i] = 56;
		i++;
		logMap.put(7, new Trace(new String[] { "a", "c", "d", "e", "f", "d",
				"b", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 47;
		amountMapper[i] = 47;
		i++;
		logMap.put(8, new Trace(new String[] { "a", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 38;
		amountMapper[i] = 38;
		i++;
		logMap.put(9, new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 33;
		amountMapper[i] = 33;
		i++;
		logMap.put(10, new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 14;
		amountMapper[i] = 14;
		i++;
		logMap.put(11, new Trace(new String[] { "a", "c", "d", "e", "f", "d",
				"b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 11;
		amountMapper[i] = 11;
		i++;
		logMap.put(12, new Trace(new String[] { "a", "d", "c", "e", "f", "c",
				"d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 9;
		amountMapper[i] = 9;
		i++;
		logMap.put(13, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 8;
		amountMapper[i] = 8;
		i++;
		logMap.put(14, new Trace(new String[] { "a", "d", "c", "e", "f", "b",
				"d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 5;
		amountMapper[i] = 5;
		i++;
		logMap.put(15, new Trace(new String[] { "a", "c", "d", "e", "f", "b",
				"d", "e", "f", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 3;
		amountMapper[i] = 3;
		i++;
		logMap.put(16, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 2;
		amountMapper[i] = 2;
		i++;
		logMap.put(17, new Trace(new String[] { "a", "d", "c", "e", "f", "b",
				"d", "e", "f", "b", "d", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 2;
		amountMapper[i] = 2;
		i++;
		logMap.put(18, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "f", "b", "d", "e", "h" }));
		rangeMapper[i] = rangeMapper[i - 1] + 1;
		amountMapper[i] = 1;
		i++;
		logMap.put(19, new Trace(new String[] { "a", "d", "b", "e", "f", "b",
				"d", "e", "f", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 1;
		amountMapper[i] = 1;
		i++;
		logMap.put(20, new Trace(new String[] { "a", "d", "c", "e", "f", "d",
				"b", "e", "f", "c", "d", "e", "f", "d", "b", "e", "g" }));
		rangeMapper[i] = rangeMapper[i - 1] + 1;
		amountMapper[i] = 1;
		i++;
	}

	private static Set<Trace>[] getFolds() {
		// Actually I checked, double checked
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

	public static void validateAlgorithmPercentage() {
		createLog();
		Set<Trace> validationSet = new HashSet<>();
		Map<Trace,Integer> tmpMapa=new HashMap<>();
		for (Integer key : logMap.keySet()) {
			validationSet.add(logMap.get(key));
			tmpMapa.put(logMap.get(key), amountMapper[key]);
		}

		Set<Trace> constructionSet = new HashSet<>();
		for (int percent = 80; percent <= 95; percent += 5f) {
			System.out.println(percent);
			constructionSet.clear();
			int amount = (int) (Float.parseFloat("" + percent) / 100.0 * TOTAL_CASES);
			for (int i = 0; i < rangeMapper.length && amount >= rangeMapper[i]; i++) {
				constructionSet.add(logMap.get(i));
			}

			WorkflowNetwork wfNet = AlphaAlgorithm.discoverWorkflowNetwork(constructionSet);
			System.out.println("Construction set count: "
					+ constructionSet.size());
			int failedTraces = 0;
			int failedCases=0;
			boolean passes;
			for (Trace t : validationSet) {
				passes = wfNet.runTrace(t);
				if (!passes) {
					failedTraces++;
					failedCases+=tmpMapa.get(t);
				}
			}

			System.out.println("Failed validation traces: " + failedTraces);
			System.out.println("Failed validation cases: "+failedCases);
		}
	}

	public static void validateAlgorithmCrossFold() {
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

			WorkflowNetwork wfC =  AlphaAlgorithm.discoverWorkflowNetwork(constructionSet);
			Place.nameGenerator = 'A';
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

			System.out.println("Failed validation traces: " + fails);
		}
	}
}
