package edu.fcse.alphaalgorithm.tools;

public enum RelationType {
	// ->
	PRECEDES('>'),
	// <-
	FOLLOWS('<'),
	// ||
	PARALLEL('|'),
	// #
	NOT_CONNECTED('#');
	RelationType(char symbol){
		this.sym=symbol;
	}
	private final char sym;
	public char symbol(){return sym;}
}
