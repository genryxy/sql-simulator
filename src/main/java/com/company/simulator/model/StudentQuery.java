package com.company.simulator.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_query")
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public final class StudentQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;

    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "practice_id")
    private Practice practice;

    public StudentQuery(final String query, final boolean isCorrect, final Task task) {
        this.query = query;
        this.isCorrect = isCorrect;
        this.task = task;
    }

    // Todo: Student query usually should contain id
    // on studentSolution. This entity should be added.
    // Also it needs to think how to submit tasks from the general
    // bank and how to connect multiple studentQueries with
    // one student solution.
}
