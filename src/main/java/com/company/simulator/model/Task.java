package com.company.simulator.model;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "task")
@EqualsAndHashCode(of = {"id"})
@Data
public class Task implements Serializable {

    public Task(Long authorId, String name, String text, String ddlScript, String correctQuery, Integer points, Boolean isPrivate, Long categoryId) {
        this.authorId = authorId;
        this.name = name;
        this.text = text;
        this.ddlScript = ddlScript;
        this.correctQuery = correctQuery;
        this.points = points;
        this.isPrivate = isPrivate;
        this.categoryId = categoryId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CollectionTable(name = "person", joinColumns = @JoinColumn(name = "user_id"))
    private Long authorId;

    private String name;

    private String text;

    private String ddlScript;

    private String correctQuery;

    private Integer points;

    private Boolean isPrivate;

    private Long categoryId;

    @ManyToMany
    @JoinTable(
        name = "practice_x_task",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "practice_id"))
    private Set<Practice> practices;
}
