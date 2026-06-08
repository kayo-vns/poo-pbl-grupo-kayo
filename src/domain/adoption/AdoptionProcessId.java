package domain.adoption;

import java.util.Objects;
import java.util.UUID;

public final class AdoptionProcessId {
    private final UUID value;

    private AdoptionProcessId(UUID value) {
        this.value = Objects.requireNonNull(value, "Adoption process id is required");
    }

    public static AdoptionProcessId newId() {
        return new AdoptionProcessId(UUID.randomUUID());
    }

    public String value() {
        return value.toString();
    }
}
