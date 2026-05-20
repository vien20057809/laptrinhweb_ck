package com.example.scholl.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "training_programs")
public class TrainingProgram {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "name_en", length = 255)
    private String nameEn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "degree_level", length = 50)
    private String degreeLevel;

    @Column(name = "education_type", length = 50)
    private String educationType;

    @Column(name = "total_credits", precision = 5, scale = 1)
    private BigDecimal totalCredits;

    @Column(name = "required_credits", precision = 5, scale = 1)
    private BigDecimal requiredCredits;

    @Column(name = "elective_credits", precision = 5, scale = 1)
    private BigDecimal electiveCredits;

    @Column(name = "internship_credits", precision = 5, scale = 1)
    private BigDecimal internshipCredits;

    @Column(name = "thesis_credits", precision = 5, scale = 1)
    private BigDecimal thesisCredits;

    @Column(name = "admission_year")
    private LocalDate admissionYear;

    @Column(name = "duration_years", precision = 5, scale = 1)
    private BigDecimal durationYears;

    @Column(name = "max_duration_years", precision = 5, scale = 1)
    private BigDecimal maxDurationYears;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String objectives;

    @Column(name = "learning_outcomes", columnDefinition = "NVARCHAR(MAX)")
    private String learningOutcomes;

    @Column(length = 20)
    private String version;

    @Column(length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @JsonIgnore
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JsonIgnore
    @Column(name = "deleted_by", length = 255)
    private String deletedBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public TrainingProgram() {
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public BigDecimal getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits) {
        this.totalCredits = totalCredits;
    }

    public BigDecimal getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(BigDecimal requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public BigDecimal getElectiveCredits() {
        return electiveCredits;
    }

    public void setElectiveCredits(BigDecimal electiveCredits) {
        this.electiveCredits = electiveCredits;
    }

    public BigDecimal getInternshipCredits() {
        return internshipCredits;
    }

    public void setInternshipCredits(BigDecimal internshipCredits) {
        this.internshipCredits = internshipCredits;
    }

    public BigDecimal getThesisCredits() {
        return thesisCredits;
    }

    public void setThesisCredits(BigDecimal thesisCredits) {
        this.thesisCredits = thesisCredits;
    }

    public LocalDate getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(LocalDate admissionYear) {
        this.admissionYear = admissionYear;
    }

    public BigDecimal getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(BigDecimal durationYears) {
        this.durationYears = durationYears;
    }

    public BigDecimal getMaxDurationYears() {
        return maxDurationYears;
    }

    public void setMaxDurationYears(BigDecimal maxDurationYears) {
        this.maxDurationYears = maxDurationYears;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(String learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
