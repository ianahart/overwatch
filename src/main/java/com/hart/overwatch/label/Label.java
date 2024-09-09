package com.hart.overwatch.label;

import java.util.List;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.activelabel.ActiveLabel;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.WorkSpace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "label")
public class Label {

    @Id
    @SequenceGenerator(name = "label_sequence", sequenceName = "label_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_checked")
    private Boolean isChecked;

    @Column(name = "title", length = 25)
    private String title;

    @Column(name = "color", length = 10)
    private String color;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private WorkSpace workSpace;

    @OneToMany(mappedBy = "label", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ActiveLabel> activeLabels;


    public Label() {

    }

    public Label(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isChecked,
            String title, String color) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isChecked = isChecked;
        this.title = title;
        this.color = color;
    }

    public Label(Boolean isChecked, String title, String color, User user, WorkSpace workSpace) {
        this.isChecked = isChecked;
        this.title = title;
        this.color = color;
        this.user = user;
        this.workSpace = workSpace;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public List<ActiveLabel> getActiveLabels() {
        return activeLabels;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public WorkSpace getWorkSpace() {
        return workSpace;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setWorkSpace(WorkSpace workSpace) {
        this.workSpace = workSpace;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setActiveLabels(List<ActiveLabel> activeLabels) {
        this.activeLabels = activeLabels;
    }
}
