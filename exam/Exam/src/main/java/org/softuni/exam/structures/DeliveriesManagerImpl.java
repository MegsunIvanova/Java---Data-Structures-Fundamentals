package org.softuni.exam.structures;

import org.softuni.exam.entities.Deliverer;
import org.softuni.exam.entities.Package;

import java.util.*;
import java.util.stream.Collectors;

public class DeliveriesManagerImpl implements DeliveriesManager {
    private Map<String, Deliverer> deliverersById;
    private Map<String, Package> packagesById;
    private Map<String, List<String>> deliverySystem;
    private Map<String, Package> unassignedPackages;

    public DeliveriesManagerImpl() {
        this.deliverersById = new LinkedHashMap<>();
        this.packagesById = new LinkedHashMap<>();
        this.deliverySystem = new LinkedHashMap<>();
        this.unassignedPackages = new LinkedHashMap<>();
    }

    @Override
    public void addDeliverer(Deliverer deliverer) {
        deliverersById.putIfAbsent(deliverer.getId(), deliverer);

        deliverySystem.putIfAbsent(deliverer.getId(), new ArrayList<>());
    }

    @Override
    public void addPackage(Package _package) {
        packagesById.putIfAbsent(_package.getId(), _package);
        unassignedPackages.putIfAbsent(_package.getId(), _package);
    }

    @Override
    public boolean contains(Deliverer deliverer) {
        return deliverersById.containsKey(deliverer.getId());
    }

    @Override
    public boolean contains(Package _package) {
        return packagesById.containsKey(_package.getId());
    }

    @Override
    public Iterable<Deliverer> getDeliverers() {
        return deliverersById.values();
    }

    @Override
    public Iterable<Package> getPackages() {
        return packagesById.values();
    }

    @Override
    public void assignPackage(Deliverer deliverer, Package _package) throws IllegalArgumentException {
        if (!this.contains(deliverer) || !this.contains(_package)) {
            throw new IllegalArgumentException();
        }

        deliverySystem.get(deliverer.getId()).add(_package.getId());
        unassignedPackages.remove(_package.getId());
    }

    @Override
    public Iterable<Package> getUnassignedPackages() {
        return unassignedPackages.values();
    }

    @Override
    public Iterable<Package> getPackagesOrderedByWeightThenByReceiver() {
        return packagesById.values().stream()
                .sorted(Comparator.comparingDouble(Package::getWeight)
                        .reversed()
                        .thenComparing(Package::getReceiver))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Deliverer> getDeliverersOrderedByCountOfPackagesThenByName() {
        return deliverersById.values().stream()
                .sorted((d1, d2) -> {
                    int d1NumberOfPackages = deliverySystem.get(d1.getId()).size();
                    int d2NumberOfPackages = deliverySystem.get(d2.getId()).size();

                    int result = Integer.compare(d2NumberOfPackages, d1NumberOfPackages);

                    if (result == 0) {
                        result = d1.getName().compareTo(d2.getName());
                    }
                    return result;
                }).collect(Collectors.toList());
    }
}
