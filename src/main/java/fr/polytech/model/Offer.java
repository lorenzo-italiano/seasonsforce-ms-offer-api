package fr.polytech.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "offer", schema = "public")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String job_title;
    private String job_description;
    // CDI or CDD
    private String contract_type;
    private UUID companyId;
    private float hours_per_week;
    // Location (City / Entire Country)
    private String location;
    private double salary;
    // On Site/Hybrid/Remote
    private String modality;
    // accommodation, meals, transport
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "benefits", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "benefit", nullable = false)
    private List<String> benefits;
    private String offer_language;
    private Date publication_date;
    // ENUM : IN PROGRESS / COMPLETED / CANCELED
    private String offer_status;
    // Contact email, linkedin or phone number
    private String contact_information;
    private String required_degree;
    private String required_experience;
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "skills", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "skill", nullable = false)
    private List<String> required_skills;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public float getHours_per_week() {
        return hours_per_week;
    }

    public void setHours_per_week(float hours_per_week) {
        this.hours_per_week = hours_per_week;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    public String getOffer_language() {
        return offer_language;
    }

    public void setOffer_language(String offer_language) {
        this.offer_language = offer_language;
    }

    public Date getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(Date publication_date) {
        this.publication_date = publication_date;
    }

    public String getOffer_status() {
        return offer_status;
    }

    public void setOffer_status(String offer_status) {
        this.offer_status = offer_status;
    }

    public String getContact_information() {
        return contact_information;
    }

    public void setContact_information(String contact_information) {
        this.contact_information = contact_information;
    }

    public String getRequired_degree() {
        return required_degree;
    }

    public void setRequired_degree(String required_degree) {
        this.required_degree = required_degree;
    }

    public String getRequired_experience() {
        return required_experience;
    }

    public void setRequired_experience(String required_experience) {
        this.required_experience = required_experience;
    }

    public List<String> getRequired_skills() {
        return required_skills;
    }

    public void setRequired_skills(List<String> required_skills) {
        this.required_skills = required_skills;
    }
}
