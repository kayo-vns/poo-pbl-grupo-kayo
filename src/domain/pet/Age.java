package domain.pet;

public final class Age {
    private final int months;

    public Age(int months) {
        if (months < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (months > 360) {
            throw new IllegalArgumentException("Age is unrealistic for a pet");
        }
        this.months = months;
    }

    public int months() {
        return months;
    }
}
