package domain;

import domain.pet.Pet;
import domain.pet.PetStatus;
import domain.pet.Species;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PetTest {
    @Test
    void rescuedPetStartsInTriageWithPendingMedicalCare() {
        Pet pet = Pet.rescue("Luna", Species.DOG, 18);

        assertEquals(PetStatus.IN_TRIAGE, pet.status());
        assertTrue(pet.medicalRecord().underTreatment());
    }

    @Test
    void petCannotBeAvailableBeforeMedicalRequirements() {
        Pet pet = Pet.rescue("Mia", Species.CAT, 8);

        assertThrows(DomainException.class, pet::makeAvailableForAdoption);
    }

    @Test
    void petBecomesAvailableAfterMedicalRequirements() {
        Pet pet = Pet.rescue("Thor", Species.DOG, 24);

        pet.vaccinate();
        pet.neuter();
        pet.finishTreatment();
        pet.makeAvailableForAdoption();

        assertEquals(PetStatus.AVAILABLE_FOR_ADOPTION, pet.status());
    }

    @Test
    void adoptedPetCannotBecomeAvailableAgain() {
        Pet pet = Pet.rescue("Nina", Species.CAT, 12);
        pet.vaccinate();
        pet.neuter();
        pet.finishTreatment();
        pet.makeAvailableForAdoption();
        pet.markAsAdopted();

        assertThrows(DomainException.class, pet::makeAvailableForAdoption);
    }

    @Test
    void fosterHomeNameIsRequired() {
        Pet pet = Pet.rescue("Bob", Species.DOG, 36);

        assertThrows(DomainException.class, () -> pet.moveToFosterHome(" "));
    }
}
