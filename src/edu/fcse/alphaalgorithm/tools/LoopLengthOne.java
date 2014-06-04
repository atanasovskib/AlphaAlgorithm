package edu.fcse.alphaalgorithm.tools;

public class LoopLengthOne {
	String prevAction;
	String loopedAction;
	String nextAction;
	public LoopLengthOne(String prevAction, String loopedAction,
			String nextAction) {
		this.prevAction = prevAction;
		this.loopedAction = loopedAction;
		this.nextAction = nextAction;
	}
	public String getPrevAction() {
		return prevAction;
	}
	public void setPrevAction(String prevAction) {
		this.prevAction = prevAction;
	}
	public String getLoopedAction() {
		return loopedAction;
	}
	public void setLoopedAction(String loopedAction) {
		this.loopedAction = loopedAction;
	}
	public String getNextAction() {
		return nextAction;
	}
	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}
	@Override
	public String toString() {
		return "LLO [prev=" + prevAction + ", looped="
				+ loopedAction + ", next=" + nextAction + "]";
	}
	
}
