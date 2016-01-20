package edu.fcse.alphaalgorithm.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper class for a list of events. Each event is represented by it's name.
 *
 * @author blagoj atanasovski
 */
public class Trace implements Iterable<Event> {
    private List<Event> eventsList;

    public Trace() {
        eventsList = new LinkedList<>();
    }

    public Trace(String[] eventsArray) {
        eventsList = new ArrayList<>(eventsArray.length);
        for (String event : eventsArray) {
            eventsList.add(new Event(event));
        }
    }

    public Trace(Collection<Event> events) {
        eventsList = new ArrayList<>(events);
    }

    public void addEvent(Event event) {
        eventsList.add(event);
    }

    @Override
    public Iterator<Event> iterator() {
        return eventsList.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Trace)) {
            return false;
        }
        Trace other = (Trace) obj;
        return this.eventsList.equals(other.eventsList);
    }

    @Override
    public int hashCode() {
        return eventsList.hashCode();
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public Event getFirstEvent() {
        return eventsList.get(0);
    }

    public Event getLastEvent() {
        return eventsList.get(eventsList.size() - 1);
    }

    @Override
    public String toString() {
        return "Trace [eventsList=" + eventsList + "]";
    }

    public void clear() {
        eventsList.clear();
    }

    public int size() {
        return eventsList.size();
    }

}
