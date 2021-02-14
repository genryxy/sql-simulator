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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @CollectionTable(name = "person", joinColumns = @JoinColumn(name = "user_id"))
    @NotBlank(message = "authorId cannot be empty")
    private Long authorId;

    @NotBlank(message = "text cannot be empty")
    private String text;

    @NotBlank(message = "correctQuery cannot be empty")
    private String correctQuery;

    @NotBlank(message = "points cannot be empty")
    private Integer points;

    @NotBlank(message = "isPrivate cannot be empty")
    private Boolean isPrivate;

    @NotBlank(message = "categoryId cannot be empty")
    private Long categoryId;

    @ManyToMany
    @JoinTable(
        name = "practice_x_task",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "practice_id"))
    private Set<Practice> practices;
}
