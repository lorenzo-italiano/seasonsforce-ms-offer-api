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
}
