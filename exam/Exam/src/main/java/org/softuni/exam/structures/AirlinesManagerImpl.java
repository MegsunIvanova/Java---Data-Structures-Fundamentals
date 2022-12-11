package org.softuni.exam.structures;

import org.softuni.exam.entities.Airline;
import org.softuni.exam.entities.Flight;

import java.util.*;
import java.util.stream.Collectors;

public class AirlinesManagerImpl implements AirlinesManager {

    private Map<String, Airline> airlinesById;
    private Map<String, Flight> flightsById;
    private Map<String, List<String>> airlinesSystem;
//    private List<Flight> completedFlights;

    public AirlinesManagerImpl() {
        this.airlinesById = new LinkedHashMap<>();
        this.flightsById = new LinkedHashMap<>();
        this.airlinesSystem = new LinkedHashMap<>();
    }

    @Override
    public void addAirline(Airline airline) {
        airlinesById.putIfAbsent(airline.getId(), airline);
        airlinesSystem.putIfAbsent(airline.getId(), new ArrayList<>());
    }

    @Override
    public void addFlight(Airline airline, Flight flight) {
        if (!this.contains(airline)) {
            throw new IllegalArgumentException();
        }

        flightsById.put(flight.getId(), flight);

        airlinesSystem.get(airline.getId()).add(flight.getId());
    }

    @Override
    public boolean contains(Airline airline) {
        return airlinesById.containsKey(airline.getId());
    }

    @Override
    public boolean contains(Flight flight) {
        return flightsById.containsKey(flight.getId());
    }

    @Override
    public void deleteAirline(Airline airline) throws IllegalArgumentException {
        if (!this.contains(airline)) {
            throw new IllegalArgumentException();
        }

        airlinesById.remove(airline.getId());

        List<String> flightsForRemove = airlinesSystem.remove(airline.getId());

        flightsForRemove.forEach(flightsById::remove);
    }

    @Override
    public Iterable<Flight> getAllFlights() {
        return flightsById.values();
    }

    @Override
    public Flight performFlight(Airline airline, Flight flight) throws IllegalArgumentException {
        if (!this.contains(airline) || !this.contains(flight)
                || !airlinesSystem.get(airline.getId()).contains(flight.getId())) {
            throw new IllegalArgumentException();
        }

        Flight flightForCompleting = flightsById.get(flight.getId());

        flightForCompleting.setCompleted(true);

        return flightForCompleting;
    }

    @Override
    public Iterable<Flight> getCompletedFlights() {
        //bad performance
        return flightsById.values().parallelStream()
                .filter(Flight::isCompleted)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Flight> getFlightsOrderedByNumberThenByCompletion() {
        //ok
        return flightsById.values().stream()
                .sorted(Comparator.comparing(Flight::isCompleted)
                        .thenComparing(Flight::getNumber))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Airline> getAirlinesOrderedByRatingThenByCountOfFlightsThenByName() {
        //ok
        return airlinesById.values().stream()
                .sorted((a1, a2) -> {

                    int result = Double.compare(a2.getRating(), a1.getRating());

                    if (result == 0) {
                        int a1CountFlights = airlinesSystem.get(a1.getId()).size();
                        int a2CountFlights = airlinesSystem.get(a2.getId()).size();
                        result = Integer.compare(a2CountFlights, a1CountFlights);

                        if (result == 0) {
                            result = a1.getName().compareTo(a2.getName());
                        }
                    }

                    return result;

                }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Airline> getAirlinesWithFlightsFromOriginToDestination(String origin, String destination) {
        //ok
        return airlinesById.values().stream()
                .filter(airline -> {

                    List<String> flights = airlinesSystem.get(airline.getId());

                    for (String id : flights) {
                        Flight flight = flightsById.get(id);

                        if (!flight.isCompleted() && flight.getOrigin().equals(origin)
                                && flight.getDestination().equals(destination))
                            return true;
                    }

                    return false;

                }).collect(Collectors.toList());
    }
}
