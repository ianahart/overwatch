package com.hart.overwatch.tag;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import com.hart.overwatch.topic.Topic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @SequenceGenerator(name = "tag_sequence", sequenceName = "tag_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Topic> topics = new ArrayList<>();


    public Tag() {

    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Tag{" + "id=" + id + ", name='" + name + '\'' + '}';
    }


}
