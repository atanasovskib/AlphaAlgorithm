package edu.fcse.alphaalgorithm;

import java.util.Set;

public class Main {
	public static void main(String args[]) {
		//String fileName = "input";
//		Set<Trace> eventLog = Utils.readInputFromCSV(fileName);
		Set<Trace> eventLog = Utils.demoL1eventLog();
		WorkflowNetContainer wfContainer = new WorkflowNetContainer(eventLog);
	}
	
}
