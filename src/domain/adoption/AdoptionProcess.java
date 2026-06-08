package domain.adoption;

import domain.DomainException;
import domain.adopter.Adopter;
import domain.pet.Pet;
import domain.pet.PetStatus;

import java.util.Objects;

public class AdoptionProcess {
    private final AdoptionProcessId id;
    private final Pet pet;
    private final Adopter adopter;
    private AdoptionStatus status;

    public AdoptionProcess(Pet pet, Adopter adopter) {
        this.id = AdoptionProcessId.newId();
        this.pet = Objects.requireNonNull(pet, "Pet is required");
        this.adopter = Objects.requireNonNull(adopter, "Adopter is required");
        this.status = AdoptionStatus.REQUESTED;
    }

    public void approve() {
        if (status != AdoptionStatus.REQUESTED) {
            throw new DomainException("Only requested adoption processes can be approved");
        }
        if (pet.status() != PetStatus.AVAILABLE_FOR_ADOPTION) {
            throw new DomainException("Pet must be available for adoption");
        }
        if (!adopter.interviewApproved()) {
            throw new DomainException("Adopter interview must be approved");
        }
        status = AdoptionStatus.APPROVED;
    }

    public void reject(String reason) {
        if (status != AdoptionStatus.REQUESTED) {
            throw new DomainException("Only requested adoption processes can be rejected");
        }
        if (reason == null || reason.isBlank()) {
            throw new DomainException("Rejection reason is required");
        }
        status = AdoptionStatus.REJECTED;
    }

    public void complete() {
        if (status != AdoptionStatus.APPROVED) {
            throw new DomainException("Only approved adoption processes can be completed");
        }
        pet.markAsAdopted();
        status = AdoptionStatus.COMPLETED;
    }

    public AdoptionProcessId id() {
        return id;
    }

    public Pet pet() {
        return pet;
    }

    public Adopter adopter() {
        return adopter;
    }

    public AdoptionStatus status() {
        return status;
    }
}
