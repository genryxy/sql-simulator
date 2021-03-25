package com.company.simulator.model;


import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

    @Transient
    private Task.Status state = Status.NOT_SOLVED;

    @ManyToMany
    @JoinTable(
        name = "practice_x_task",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "practice_id"))
    private Set<Practice> practices;

    public enum Status {
        CORRECT_SOLVED("correct"),
        WRONG_SOLVED("wrong"),
        NOT_SOLVED("not_solved");

        private final String val;

        Status(String value) {
            this.val = value;
        }

        public String value() {
            return val;
        }
    }
}
