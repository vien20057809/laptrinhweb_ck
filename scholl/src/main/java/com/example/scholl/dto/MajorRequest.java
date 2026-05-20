package com.example.scholl.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MajorRequest {

    @NotNull(message = "Phải chọn khoa")
    private UUID departmentId;

    @NotBlank(message = "Mã ngành không được để trống")
    @Size(max = 100, message = "Mã ngành tối đa 100 ký tự")
    private String code;

    @NotBlank(message = "Tên ngành không được để trống")
    @Size(max = 255, message = "Tên ngành tối đa 255 ký tự")
    private String name;

    private String description;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
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
