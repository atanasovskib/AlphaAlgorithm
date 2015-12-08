package edu.fcse.alphaalgorithm.tools;

import java.util.Set;

public class Place {
    // (Input Activities, Output Activities)
    Pair<Set<Event>, Set<Event>> eventsPair;
    /**
     * The name of the place is merely symbolic, plays no part in the
     * identification of the Place. See {@link #equals(Object)}
     */
    String name;
    public static char nameGenerator = 'A';

    private boolean token;

    public Place(Set<Event> in, Set<Event> out) {
        eventsPair = new Pair<>(in, out);
        name = "" + nameGenerator++;
    }

    public Place(String name, Set<Event> in, Set<Event> out) {
        eventsPair = new Pair<>(in, out);
        this.name = name;
    }

    public Set<Event> getInEvents() {
        return eventsPair.getFirst();
    }

    public Set<Event> getOutEvents() {
        return eventsPair.getSecond();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, eventsPair);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((eventsPair == null) ? 0 : eventsPair.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Place other = (Place) obj;
        if (eventsPair == null) {
            if (other.eventsPair != null)
                return false;
        } else if (!eventsPair.equals(other.eventsPair))
            return false;
        return true;
    }

    /**
     * Required for step 5 in the Alpha Algorithm (Generating Yl). Check if a
     * the input and output places are supersets to the input and output places
     * of another place. Place A with input events {a} and output events {b,e}
     * is a superplace of Place B with input {a} and output {b} => Place B can
     * be discarded.
     *
     * @param potentialSubPlace
     * @return true if potentialSubPlace.inEvents ⊆ this.inEvents &&
     * potentialSubPlace.outEvents ⊆ this.outEvents
     */
    public boolean isSuperPlace(Place potentialSubPlace) {
        if (getInEvents().containsAll(potentialSubPlace.getInEvents())) {
            if (getOutEvents().containsAll(
                    potentialSubPlace.getOutEvents())) {
                return true;
            }
        }
        return false;
    }

    public void addInEvent(Event event) {
        eventsPair.getFirst().add(event);
    }

    public void addOutEvent(Event eventName) {
        eventsPair.getSecond().add(eventName);
    }

    public String getName() {
        return name;
    }

    public boolean hasToken() {
        return token;
    }

    public void putToken() {
        token = true;
    }

    public void takeToken() {
        token = false;
    }
}
