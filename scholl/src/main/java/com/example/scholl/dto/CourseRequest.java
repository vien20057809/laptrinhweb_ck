package com.example.scholl.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourseRequest {

    @NotNull(message = "Phải chọn khoa")
    private UUID departmentId;

    @NotBlank(message = "Mã học phần không được để trống")
    @Size(max = 100, message = "Mã học phần tối đa 100 ký tự")
    private String code;

    @NotBlank(message = "Tên học phần không được để trống")
    @Size(max = 255, message = "Tên học phần tối đa 255 ký tự")
    private String name;

    @Size(max = 255)
    private String nameEn;

    private BigDecimal credits;

    @Size(max = 20)
    private String courseType;

    private BigDecimal theoryHours;
    private BigDecimal practiceHours;
    private BigDecimal selfStudyHours;
    private BigDecimal internshipCredits;
    private String description;
    private Boolean isActive;

    @Size(max = 255, message = "Tên người thao tác tối đa 255 ký tự")
    private String actor;

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
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

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public BigDecimal getTheoryHours() {
        return theoryHours;
    }

    public void setTheoryHours(BigDecimal theoryHours) {
        this.theoryHours = theoryHours;
    }

    public BigDecimal getPracticeHours() {
        return practiceHours;
    }

    public void setPracticeHours(BigDecimal practiceHours) {
        this.practiceHours = practiceHours;
    }

    public BigDecimal getSelfStudyHours() {
        return selfStudyHours;
    }

    public void setSelfStudyHours(BigDecimal selfStudyHours) {
        this.selfStudyHours = selfStudyHours;
    }

    public BigDecimal getInternshipCredits() {
        return internshipCredits;
    }

    public void setInternshipCredits(BigDecimal internshipCredits) {
        this.internshipCredits = internshipCredits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
