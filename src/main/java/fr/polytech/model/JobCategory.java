package fr.polytech.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "job_category", schema = "public")
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
