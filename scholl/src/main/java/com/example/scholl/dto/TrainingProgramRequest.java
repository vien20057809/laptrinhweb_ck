package com.example.scholl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TrainingProgramRequest {

    @NotBlank(message = "Mã CTĐT không được để trống")
    @Size(max = 100, message = "Mã CTĐT tối đa 100 ký tự")
    private String code;

    @NotBlank(message = "Tên CTĐT không được để trống")
    @Size(max = 255, message = "Tên CTĐT tối đa 255 ký tự")
    private String name;

    @Size(max = 255, message = "Tên tiếng Anh tối đa 255 ký tự")
    private String nameEn;

    @NotNull(message = "Phải chọn ngành")
    private UUID majorId;

    @NotNull(message = "Phải chọn khoa")
    private UUID departmentId;

    @Size(max = 50)
    private String degreeLevel;

    @Size(max = 50)
    private String educationType;

    private BigDecimal totalCredits;
    private BigDecimal requiredCredits;
    private BigDecimal electiveCredits;
    private BigDecimal internshipCredits;
    private BigDecimal thesisCredits;
    private LocalDate admissionYear;
    private BigDecimal durationYears;
    private BigDecimal maxDurationYears;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String description;
    private String objectives;
    private String learningOutcomes;

    @Size(max = 20)
    private String version;

    @Size(max = 20)
    private String status;

    private Boolean isActive;

    @Size(max = 255, message = "Tên người thao tác tối đa 255 ký tự")
    private String actor;

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

    public UUID getMajorId() {
        return majorId;
    }

    public void setMajorId(UUID majorId) {
        this.majorId = majorId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
