package fr.polytech.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OfferDetailDTO {

    private UUID id;
    private String job_title;
    private String job_description;
    // CDI or CDD
    private String contract_type;
    private CompanyDTO company;
    private double salary;
    private AddressDTO address;
    private float hours_per_week;
    // accommodation, meals, transport
    private List<String> benefits;
    private String offer_language;
    private Date publication_date;
    // ENUM : IN PROGRESS / COMPLETED / CANCELED
    private String offer_status;
    // Contact email, linkedin or phone number
    private String contact_information;
    private String required_degree;
    private String required_experience;
    private List<String> required_skills;
    private JobCategory jobCategory;
    private UUID creatorId;
    private UUID recruitedId;
    private Date startDate;
    private Date endDate;

    public static OfferDetailDTO getOfferDetailDTO(Offer offer, CompanyDTO companyDTO, AddressDTO addressDTO, JobCategory jobCategory) {
        OfferDetailDTO offerDetailDTO = new OfferDetailDTO();

        offerDetailDTO.setId(offer.getId());
        offerDetailDTO.setJob_title(offer.getJob_title());
        offerDetailDTO.setJob_description(offer.getJob_description());
        offerDetailDTO.setContract_type(offer.getContract_type());
        offerDetailDTO.setHours_per_week(offer.getHours_per_week());
        offerDetailDTO.setSalary(offer.getSalary());
        offerDetailDTO.setBenefits(offer.getBenefits());
        offerDetailDTO.setOffer_language(offer.getOffer_language());
        offerDetailDTO.setPublication_date(offer.getPublication_date());
        offerDetailDTO.setOffer_status(offer.getOffer_status());
        offerDetailDTO.setContact_information(offer.getContact_information());
        offerDetailDTO.setRequired_degree(offer.getRequired_degree());
        offerDetailDTO.setRequired_experience(offer.getRequired_experience());
        offerDetailDTO.setRequired_skills(offer.getRequired_skills());
        offerDetailDTO.setCompany(companyDTO);
        offerDetailDTO.setAddress(addressDTO);
        offerDetailDTO.setJobCategory(jobCategory);
        offerDetailDTO.setCreatorId(offer.getCreatorId());
        offerDetailDTO.setRecruitedId(offer.getRecruitedId());
        offerDetailDTO.setStartDate(offer.getStartDate());
        offerDetailDTO.setEndDate(offer.getEndDate());

        return offerDetailDTO;
    }

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

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public float getHours_per_week() {
        return hours_per_week;
    }

    public void setHours_per_week(float hours_per_week) {
        this.hours_per_week = hours_per_week;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public JobCategory getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(JobCategory jobCategory) {
        this.jobCategory = jobCategory;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public UUID getRecruitedId() {
        return recruitedId;
    }

    public void setRecruitedId(UUID recruitedId) {
        this.recruitedId = recruitedId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
