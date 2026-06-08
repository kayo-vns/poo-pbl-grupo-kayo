package application;

import domain.pet.Pet;
import domain.pet.PetId;

import java.util.List;
import java.util.Optional;

public interface PetRepository {
    void save(Pet pet);

    Optional<Pet> findById(PetId id);

    List<Pet> findAll();
}
