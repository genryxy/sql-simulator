package com.company.simulator.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    private String name;

    private String text;

    private String ddlScript;

    private String correctQuery;

    private Integer points;

    private Boolean isPrivate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

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
