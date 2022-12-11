package tripadministratorjava;

import java.util.*;
import java.util.stream.Collectors;

public class TripAdministratorImpl implements TripAdministrator {
    private Map<String, Company> companiesByNames;
    private Map<String, Trip> tripsByIds;
    private Map<String, List<Trip>> companiesWithTrips;

    public TripAdministratorImpl() {
        this.companiesByNames = new LinkedHashMap<>();
        this.tripsByIds = new LinkedHashMap<>();
        this.companiesWithTrips = new LinkedHashMap<>();
    }

    @Override//ok
    public void addCompany(Company c) {
        if (this.exist(c)) {
            throw new IllegalArgumentException("There is a company with the same name added before");
        }

        companiesWithTrips.put(c.name, new ArrayList<>());
        companiesByNames.put(c.name, c);
    }

    @Override//ok
    public void addTrip(Company c, Trip t) {
        if (!this.exist(c) || this.exist(t)) {
            throw new IllegalArgumentException();
        }

        tripsByIds.put(t.id, t);

        if (c.tripOrganizationLimit == companiesWithTrips.get(c.name).size()) {
            throw new IllegalArgumentException();
        }

        companiesWithTrips.get(c.name).add(t);

    }

    @Override//ok
    public boolean exist(Company c) {
        return this.companiesByNames.containsKey(c.name);
    }

    @Override//ok
    public boolean exist(Trip t) {
        return this.tripsByIds.containsKey(t.id);
    }

    @Override//ok
    public void removeCompany(Company c) {
        if (!companiesByNames.containsKey(c.name)) {
            throw new IllegalArgumentException("The company does not exist");
        }

        companiesByNames.remove(c.name);
        List<Trip> trips = companiesWithTrips.remove(c.name);
        trips.forEach(t -> tripsByIds.remove(t.id));
    }

    @Override//ok
    public Collection<Company> getCompanies() {
        return companiesByNames.values();
    }

    @Override//ok
    public Collection<Trip> getTrips() {
        return tripsByIds.values();
    }

    @Override//ok
    public void executeTrip(Company c, Trip t) {
        if (!this.exist(c) || !this.exist(t)) {
            throw new IllegalArgumentException();
        }

        List<Trip> companyTrips = companiesWithTrips.get(c.name);

        boolean removed = companyTrips.removeIf(tr -> tr.id.equals(t.id));

        if (!removed) {
            throw new IllegalArgumentException();
        }

        tripsByIds.remove(t.id);
    }

    @Override
    public Collection<Company> getCompaniesWithMoreThatNTrips(int n) {
        return getCompanies().stream()
                .filter(c -> companiesWithTrips.get(c.name).size() > n)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Trip> getTripsWithTransportationType(Transportation t) {
        return getTrips().stream()
                .filter(trip -> trip.transportation.equals(t))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Trip> getAllTripsInPriceRange(int lo, int hi) {
        return getTrips().stream()
                .filter(trip -> trip.price >= lo && trip.price <= hi)
                .collect(Collectors.toList());
    }

}
