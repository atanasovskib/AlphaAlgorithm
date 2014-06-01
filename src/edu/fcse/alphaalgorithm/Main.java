package edu.fcse.alphaalgorithm;

import java.util.Set;

public class Main {
	public static void main(String args[]) {
		// String fileName = "input";
		// Set<Trace> eventLog = Utils.readInputFromCSV(fileName);
		Set<Trace> eventLog = Utils.chapter7EventLog();
		WorkflowNetCreator.takeInAccountLoopsLengthTwo=true;
		WorkflowNetCreator wfContainer = new WorkflowNetCreator(eventLog);
	}

}
