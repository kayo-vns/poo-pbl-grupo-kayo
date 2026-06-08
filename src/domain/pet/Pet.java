package domain.pet;

import domain.DomainException;

import java.util.Objects;

public class Pet {
    private final PetId id;
    private final PetName name;
    private final Species species;
    private final Age age;
    private MedicalRecord medicalRecord;
    private PetStatus status;
    private String fosterHomeName;

    private Pet(PetId id, PetName name, Species species, Age age, MedicalRecord medicalRecord, PetStatus status, String fosterHomeName) {
        this.id = Objects.requireNonNull(id, "Pet id is required");
        this.name = Objects.requireNonNull(name, "Pet name is required");
        this.species = Objects.requireNonNull(species, "Species is required");
        this.age = Objects.requireNonNull(age, "Age is required");
        this.medicalRecord = Objects.requireNonNull(medicalRecord, "Medical record is required");
        this.status = Objects.requireNonNull(status, "Status is required");
        this.fosterHomeName = fosterHomeName;
    }

    public static Pet rescue(String name, Species species, int ageInMonths) {
        return new Pet(PetId.newId(), new PetName(name), species, new Age(ageInMonths), MedicalRecord.initialTriage(), PetStatus.IN_TRIAGE, null);
    }

    public static Pet restore(PetId id, String name, Species species, int ageInMonths, MedicalRecord medicalRecord, PetStatus status, String fosterHomeName) {
        return new Pet(id, new PetName(name), species, new Age(ageInMonths), medicalRecord, status, fosterHomeName);
    }

    public void moveToFosterHome(String fosterHomeName) {
        if (fosterHomeName == null || fosterHomeName.isBlank()) {
            throw new DomainException("Foster home name is required");
        }
        if (status == PetStatus.ADOPTED) {
            throw new DomainException("Adopted pets cannot move to foster homes");
        }
        this.fosterHomeName = fosterHomeName.trim();
        this.status = PetStatus.IN_FOSTER_HOME;
    }

    public void vaccinate() {
        ensureNotAdopted("Adopted pets cannot receive new triage updates");
        medicalRecord = medicalRecord.vaccinate();
    }

    public void neuter() {
        ensureNotAdopted("Adopted pets cannot receive new triage updates");
        medicalRecord = medicalRecord.neuter();
    }

    public void finishTreatment() {
        ensureNotAdopted("Adopted pets cannot receive new triage updates");
        medicalRecord = medicalRecord.finishTreatment();
    }

    public void makeAvailableForAdoption() {
        ensureNotAdopted("Adopted pets cannot become available again");
        if (!medicalRecord.isReadyForAdoption()) {
            throw new DomainException("Pet must be vaccinated, neutered and without treatment before adoption");
        }
        status = PetStatus.AVAILABLE_FOR_ADOPTION;
    }

    public void markAsAdopted() {
        if (status != PetStatus.AVAILABLE_FOR_ADOPTION) {
            throw new DomainException("Only available pets can be adopted");
        }
        status = PetStatus.ADOPTED;
    }

    private void ensureNotAdopted(String message) {
        if (status == PetStatus.ADOPTED) {
            throw new DomainException(message);
        }
    }

    public PetId id() {
        return id;
    }

    public String name() {
        return name.value();
    }

    public Species species() {
        return species;
    }

    public int ageInMonths() {
        return age.months();
    }

    public MedicalRecord medicalRecord() {
        return medicalRecord;
    }

    public PetStatus status() {
        return status;
    }

    public String fosterHomeName() {
        return fosterHomeName;
    }
}
