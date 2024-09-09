package com.hart.overwatch.activelabel;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.label.Label;
import com.hart.overwatch.todocard.TodoCard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity()
@Table(name = "active_label")
public class ActiveLabel {

    @Id
    @SequenceGenerator(name = "active_label_sequence", sequenceName = "active_label_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "active_label_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JoinColumn(name = "todo_card_id", referencedColumnName = "id")
    private TodoCard todoCard;

    @ManyToOne()
    @JoinColumn(name = "label_id", referencedColumnName = "id")
    private Label label;


    public ActiveLabel() {

    }

    public ActiveLabel(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ActiveLabel(TodoCard todoCard, Label label) {
        this.todoCard = todoCard;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Label getLabel() {
        return label;
    }

    public TodoCard getTodoCard() {
        return todoCard;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setTodoCard(TodoCard todoCard) {
        this.todoCard = todoCard;
    }
}


