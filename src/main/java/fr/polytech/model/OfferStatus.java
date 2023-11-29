package fr.polytech.model;

public enum OfferStatus {
    IN_PROGRESS("IN_PROGRESS"),
    RECRUITED("RECRUITED"),
    COMPLETED("COMPLETED"),
    REVIEWED("REVIEWED");

    private final String status;

    OfferStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return status;
    }
}
