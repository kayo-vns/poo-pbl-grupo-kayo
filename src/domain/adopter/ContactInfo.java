package domain.adopter;

public final class ContactInfo {
    private final String email;
    private final String phone;

    public ContactInfo(String email, String phone) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (phone == null || phone.replaceAll("\\D", "").length() < 10) {
            throw new IllegalArgumentException("Valid phone is required");
        }
        this.email = email.trim().toLowerCase();
        this.phone = phone.trim();
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }
}
