package domain;

import domain.adopter.Adopter;
import domain.adopter.ContactInfo;
import domain.adoption.AdoptionProcess;
import domain.adoption.AdoptionStatus;
import domain.pet.Pet;
import domain.pet.PetStatus;
import domain.pet.Species;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdoptionProcessTest {
    @Test
    void adoptionCannotBeApprovedWhenPetIsNotAvailable() {
        Pet pet = Pet.rescue("Sol", Species.CAT, 6);
        Adopter adopter = approvedAdopter();
        AdoptionProcess process = new AdoptionProcess(pet, adopter);

        assertThrows(DomainException.class, process::approve);
    }

    @Test
    void adoptionCannotBeApprovedWithoutApprovedInterview() {
        Pet pet = availablePet();
        Adopter adopter = new Adopter("Kayo", new ContactInfo("kayo@email.com", "11999999999"));
        AdoptionProcess process = new AdoptionProcess(pet, adopter);

        assertThrows(DomainException.class, process::approve);
    }

    @Test
    void adoptionCanBeApprovedWithAvailablePetAndApprovedInterview() {
        Pet pet = availablePet();
        Adopter adopter = approvedAdopter();
        AdoptionProcess process = new AdoptionProcess(pet, adopter);

        process.approve();

        assertEquals(AdoptionStatus.APPROVED, process.status());
    }

    @Test
    void completingAdoptionMarksPetAsAdopted() {
        Pet pet = availablePet();
        Adopter adopter = approvedAdopter();
        AdoptionProcess process = new AdoptionProcess(pet, adopter);

        process.approve();
        process.complete();

        assertEquals(AdoptionStatus.COMPLETED, process.status());
        assertEquals(PetStatus.ADOPTED, pet.status());
    }

    @Test
    void rejectedProcessRequiresReason() {
        AdoptionProcess process = new AdoptionProcess(availablePet(), approvedAdopter());

        assertThrows(DomainException.class, () -> process.reject(""));
    }

    private Pet availablePet() {
        Pet pet = Pet.rescue("Mel", Species.DOG, 15);
        pet.vaccinate();
        pet.neuter();
        pet.finishTreatment();
        pet.makeAvailableForAdoption();
        return pet;
    }

    private Adopter approvedAdopter() {
        Adopter adopter = new Adopter("Kayo", new ContactInfo("kayo@email.com", "11999999999"));
        adopter.approveInterview();
        return adopter;
    }
}
