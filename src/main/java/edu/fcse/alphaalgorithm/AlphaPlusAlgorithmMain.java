package edu.fcse.alphaalgorithm;

import edu.fcse.alphaalgorithm.tools.Trace;

import java.util.HashSet;
import java.util.Set;

public class AlphaPlusAlgorithmMain {
	public static void main(String args[]) {
		Validate.validateAlgorithmPercentage();
		Set<Trace> eventLog = checkArgs(args);
		//eventLog = Utils.parseXESFile("reviewing.xes");
		WorkflowNetCreator.takeInAccountLoopsLengthTwo = true;
		new WorkflowNetCreator(eventLog);
	}

	private static Set<Trace> checkArgs(String[] args) {
		boolean error = false;
		if (args.length != 2) {
			error = true;
		} else {
			if (args[0].equals("d")) {
				switch (args[1]) {
				case "L1":
					return Utils.demoL1eventLog();
				case "L2":
					return Utils.demoL2eventLog();
				case "L7":
					return Utils.demoL7eventLog();
				case "LLT":
					return Utils.demoLLTeventLog();
				case "chap7":
					return Utils.chapter7EventLog();

				default:
					error = true;
					break;
				}
			} else if (args[0].equals("f")) {
				return Utils.readInputFromCSV(args[1]);
			} else {
				error = true;
			}
		}
		if (error) {
			usage();
			System.exit(1);
		}
		return new HashSet<>();
	}

	private static void usage() {
		System.out.println("Error reading input parameters!\n Usage:");
		System.out
				.println("AlphaPlusAlgorithm.jar demoOrInputFile demoName/fileName");
		System.out
				.println("demo - execute the algorithm with one of the predefined demo cases from the ProcessMining book chapters");
		System.out.println("inputFile - specify a CSV file for the event log");
		System.out.println("demoName - available are: L1, L2, L7, LLT, chap7");
		System.out.println("Example: AlphaPlusAlgorithm.jar d chap7");
		System.out.println("Example: AlphaPlusAlgorithm.jar f ~/inputLog.csv");
	}
}
