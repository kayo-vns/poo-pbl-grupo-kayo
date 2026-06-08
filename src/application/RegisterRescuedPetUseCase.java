package application;

import domain.pet.Pet;
import domain.pet.Species;

import java.util.Objects;

public class RegisterRescuedPetUseCase {
    private final PetRepository petRepository;

    public RegisterRescuedPetUseCase(PetRepository petRepository) {
        this.petRepository = Objects.requireNonNull(petRepository, "Pet repository is required");
    }

    public Pet execute(String name, Species species, int ageInMonths) {
        Pet pet = Pet.rescue(name, species, ageInMonths);
        petRepository.save(pet);
        return pet;
    }
}
