package domain.pet;

import java.util.Objects;
import java.util.UUID;

public final class PetId {
    private final UUID value;

    private PetId(UUID value) {
        this.value = Objects.requireNonNull(value, "Pet id is required");
    }

    public static PetId newId() {
        return new PetId(UUID.randomUUID());
    }

    public static PetId from(String value) {
        return new PetId(UUID.fromString(value));
    }

    public String value() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PetId petId)) {
            return false;
        }
        return value.equals(petId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
