package domain.adopter;

import domain.DomainException;

import java.util.Objects;

public class Adopter {
    private final AdopterId id;
    private final String name;
    private final ContactInfo contactInfo;
    private boolean interviewApproved;

    public Adopter(String name, ContactInfo contactInfo) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Adopter name is required");
        }
        this.id = AdopterId.newId();
        this.name = name.trim();
        this.contactInfo = Objects.requireNonNull(contactInfo, "Contact info is required");
        this.interviewApproved = false;
    }

    public void approveInterview() {
        interviewApproved = true;
    }

    public void rejectInterview() {
        if (interviewApproved) {
            throw new DomainException("Approved interview cannot be rejected");
        }
    }

    public AdopterId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public ContactInfo contactInfo() {
        return contactInfo;
    }

    public boolean interviewApproved() {
        return interviewApproved;
    }
}
