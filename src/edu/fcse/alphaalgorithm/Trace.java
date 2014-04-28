package edu.fcse.alphaalgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper class for a list of events. Each event is represented by it's name.
 * 
 * @author blagoj atanasovski
 * 
 */
public class Trace implements Iterable<String>{
	private List<String> eventsList;
	public Trace(){
		eventsList=new LinkedList<>();
	}
	public Trace(String[] eventsArray){
		eventsList=new ArrayList<>(eventsArray.length);
		for(String event:eventsArray){
			eventsList.add(event);
		}
	}
	public Trace(Collection<String> events){
		eventsList=new ArrayList<>(events);
	}
	
	public void addEvent(String eventName){
		eventsList.add(eventName);
	}

	@Override
	public Iterator<String> iterator() {
		return eventsList.iterator();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Trace)){
			return false;
		}
		Trace other=(Trace)obj;
		return this.eventsList.equals(other.eventsList);
	}
	@Override
	public int hashCode() {
		return eventsList.hashCode();
	}
	public List<String> getEventsList(){
		return eventsList;
	}
	public String getFirstEvent() {
		return eventsList.get(0);
	}
	public String getLastEvent() {
		return eventsList.get(eventsList.size()-1);
	}
}
