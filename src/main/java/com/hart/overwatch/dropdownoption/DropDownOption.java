package com.hart.overwatch.dropdownoption;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.customfield.CustomField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "dropdown_option")
public class DropDownOption {

    @Id
    @SequenceGenerator(name = "dropdown_option_sequence", sequenceName = "dropdown_option_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dropdown_option_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "option_value", length = 50)
    private String optionValue;

    @ManyToOne()
    @JoinColumn(name = "custom_field_id", referencedColumnName = "id")
    private CustomField customField;

    public DropDownOption(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
            String optionValue) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.optionValue = optionValue;
    }

    public DropDownOption(String optionValue, CustomField customField) {
        this.optionValue = optionValue;
        this.customField = customField;
    }

    public Long getId() {
        return id;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }

}
