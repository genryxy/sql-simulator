package com.company.simulator.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
@EqualsAndHashCode(of = {"id"})
@Data
@ToString(of = {"id", "name"})
public class Task implements Serializable {

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
