package com.example.scholl.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TrainingProgramCourseRequest {

    @NotNull(message = "Phải chọn chương trình đào tạo")
    private UUID trainingProgramId;

    @NotNull(message = "Phải chọn học phần")
    private UUID courseId;

    @Size(max = 100)
    private String courseCode;

    @Size(max = 255)
    private String courseName;

    private UUID semesterId;

    @Size(max = 100)
    private String semesterCode;

    @Size(max = 20)
    private String academicYear;

    private Boolean isRequired;
    private String groupCode;
    private BigDecimal credits;
    private UUID prerequisiteCourseId;
    private Boolean isPrerequisiteRequired;

    @Size(max = 500)
    private String note;

    private Integer sortOrder;

    @Size(max = 50)
    private String status;

    private Boolean isActive;

    @Size(max = 255, message = "Tên người thao tác tối đa 255 ký tự")
    private String actor;

    public UUID getTrainingProgramId() {
        return trainingProgramId;
    }

    public void setTrainingProgramId(UUID trainingProgramId) {
        this.trainingProgramId = trainingProgramId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public UUID getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(UUID semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public UUID getPrerequisiteCourseId() {
        return prerequisiteCourseId;
    }

    public void setPrerequisiteCourseId(UUID prerequisiteCourseId) {
        this.prerequisiteCourseId = prerequisiteCourseId;
    }

    public Boolean getIsPrerequisiteRequired() {
        return isPrerequisiteRequired;
    }

    public void setIsPrerequisiteRequired(Boolean isPrerequisiteRequired) {
        this.isPrerequisiteRequired = isPrerequisiteRequired;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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
