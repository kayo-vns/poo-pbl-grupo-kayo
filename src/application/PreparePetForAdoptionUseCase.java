package application;

import domain.DomainException;
import domain.pet.Pet;
import domain.pet.PetId;

import java.util.Objects;

public class PreparePetForAdoptionUseCase {
    private final PetRepository petRepository;

    public PreparePetForAdoptionUseCase(PetRepository petRepository) {
        this.petRepository = Objects.requireNonNull(petRepository, "Pet repository is required");
    }

    public Pet execute(String petId) {
        Pet pet = petRepository.findById(PetId.from(petId))
                .orElseThrow(() -> new DomainException("Pet not found"));
        pet.vaccinate();
        pet.neuter();
        pet.finishTreatment();
        pet.makeAvailableForAdoption();
        petRepository.save(pet);
        return pet;
    }
}
