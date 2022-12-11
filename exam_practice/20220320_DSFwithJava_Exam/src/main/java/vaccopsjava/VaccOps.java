package vaccopsjava;

import java.util.*;
import java.util.stream.Collectors;

public class VaccOps implements IVaccOps {

    private Map<Doctor, List<Patient>> doctors;
    private Map<Patient, Doctor> patients;

    public VaccOps() {
        this.doctors = new HashMap<>();
        this.patients = new HashMap<>();
    }

    public void addDoctor(Doctor d) {
        if (exist(d)) {
            throw new IllegalArgumentException();
        }

        this.doctors.put(d, new ArrayList<>());
    }

    public void addPatient(Doctor d, Patient p) {
        if (!exist(d) || exist(p)) {
            throw new IllegalArgumentException();
        }

        List<Patient> patientsOfDoctor = doctors.get(d);
        patientsOfDoctor.add(p);
        patients.put(p, d);
    }

    public Collection<Doctor> getDoctors() {
        return this.doctors.keySet();
    }

    public Collection<Patient> getPatients() {
        return this.patients.keySet();
    }

    public boolean exist(Doctor d) {
        return doctors.containsKey(d);
    }

    public boolean exist(Patient p) {
        return patients.containsKey(p);
    }

    public Doctor removeDoctor(String name) {
        Doctor doctorForRemove = doctors.keySet().stream()
                .filter(d -> d.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        List<Patient> patientForRemove = doctors.remove(doctorForRemove);

        patientForRemove.forEach(patients::remove);

        return doctorForRemove;
    }

    public void changeDoctor(Doctor from, Doctor to, Patient p) {
        if (!exist(from) || !exist(to) || !exist(p)) {
            throw new IllegalArgumentException();
        }

        List<Patient> patientsOfDoctorFrom = doctors.get(from);

        boolean isRemoved = patientsOfDoctorFrom.remove(p);

        if (!isRemoved) {
            throw new IllegalArgumentException();
        }


        List<Patient> patientsOfDoctorTo = doctors.get(to);

        patientsOfDoctorTo.add(p);

        patients.put(p, to);

    }

    public Collection<Doctor> getDoctorsByPopularity(int populariry) {
        return doctors.keySet()
                .stream()
                .filter(d -> d.getPopularity() == populariry)
                .collect(Collectors.toList());
    }

    public Collection<Patient> getPatientsByTown(String town) {
        return patients.keySet().stream()
                .filter(p -> p.getTown().equals(town))
                .collect(Collectors.toList());
    }

    public Collection<Patient> getPatientsInAgeRange(int lo, int hi) {
        return patients.keySet().stream()
                .filter(p -> p.getAge() >= lo && p.getAge() <= hi)
                .collect(Collectors.toList());
    }

    public Collection<Doctor> getDoctorsSortedByPatientsCountDescAndNameAsc() {
        return doctors.keySet()
                .stream()
                .sorted(doctorComparatorByNumberOfPatientsDescThanByNamesAsc())
                .collect(Collectors.toList());
    }

    private Comparator<Doctor> doctorComparatorByNumberOfPatientsDescThanByNamesAsc() {
        return (d1, d2) -> {
            int countPatientsD1 = doctors.get(d1).size();
            int countPatientsD2 = doctors.get(d2).size();

            if (countPatientsD1 == countPatientsD2) {
                return d1.getName().compareTo(d2.getName());
            }

            return Integer.compare(countPatientsD2, countPatientsD1);
        };
    }

    public Collection<Patient> getPatientsSortedByDoctorsPopularityAscThenByHeightDescThenByAge() {
//        return patients.entrySet().stream().
//                sorted((entry1, entry2) -> {
//                    int result = entry1.getValue().popularity - entry2.getValue().popularity;
//
//                    if (result == 0) {
//                        result = entry2.getKey().getHeight() - entry1.getKey().getHeight();
//
//                        if (result == 0) {
//                            result = entry1.getKey().getAge() - entry2.getKey().getAge();
//                        }
//                    }
//                    return result;
//                })
//                .map(entry -> entry.getKey())
//                .collect(Collectors.toList());

        Map<Integer, List<Patient>> patientByDoctorPopularity = new TreeMap<>();

        doctors.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().getPopularity()))
                .forEach(entry -> {
                    int popularity = entry.getKey().getPopularity();
                    List<Patient> patients = entry.getValue();
                    patientByDoctorPopularity.putIfAbsent(popularity, new ArrayList<>());
                    patientByDoctorPopularity.get(popularity).addAll(patients);
                });

        return patientByDoctorPopularity.values().stream().
                flatMap(l -> l.stream()
                        .sorted(Comparator.comparingInt(Patient::getHeight)
                        .reversed()
                        .thenComparingInt(Patient::getAge)))
                .collect(Collectors.toList());


    }


}
