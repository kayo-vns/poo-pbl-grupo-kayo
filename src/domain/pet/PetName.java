package domain.pet;

public final class PetName {
    private final String value;

    public PetName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pet name is required");
        }
        if (value.trim().length() < 2) {
            throw new IllegalArgumentException("Pet name must have at least 2 characters");
        }
        this.value = value.trim();
    }

    public String value() {
        return value;
    }
}
