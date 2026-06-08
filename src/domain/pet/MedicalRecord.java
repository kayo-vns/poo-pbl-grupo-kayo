package domain.pet;

public final class MedicalRecord {
    private final boolean vaccinated;
    private final boolean neutered;
    private final boolean underTreatment;

    public MedicalRecord(boolean vaccinated, boolean neutered, boolean underTreatment) {
        this.vaccinated = vaccinated;
        this.neutered = neutered;
        this.underTreatment = underTreatment;
    }

    public static MedicalRecord initialTriage() {
        return new MedicalRecord(false, false, true);
    }

    public MedicalRecord vaccinate() {
        return new MedicalRecord(true, neutered, underTreatment);
    }

    public MedicalRecord neuter() {
        return new MedicalRecord(vaccinated, true, underTreatment);
    }

    public MedicalRecord finishTreatment() {
        return new MedicalRecord(vaccinated, neutered, false);
    }

    public boolean isReadyForAdoption() {
        return vaccinated && neutered && !underTreatment;
    }

    public boolean vaccinated() {
        return vaccinated;
    }

    public boolean neutered() {
        return neutered;
    }

    public boolean underTreatment() {
        return underTreatment;
    }
}
