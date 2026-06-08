package domain.adopter;

import java.util.Objects;
import java.util.UUID;

public final class AdopterId {
    private final UUID value;

    private AdopterId(UUID value) {
        this.value = Objects.requireNonNull(value, "Adopter id is required");
    }

    public static AdopterId newId() {
        return new AdopterId(UUID.randomUUID());
    }

    public String value() {
        return value.toString();
    }
}
