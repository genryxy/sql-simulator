package com.company.simulator.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "task")
public class Task implements Serializable {

    public Task(Long authorId, String name, String text, String correctQuery, Integer points, Boolean isPrivate, Long categoryId) {
        this.authorId = authorId;
        this.name = name;
        this.text = text;
        this.correctQuery = correctQuery;
        this.points = points;
        this.isPrivate = isPrivate;
        this.categoryId = categoryId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CollectionTable(name = "person", joinColumns = @JoinColumn(name = "user_id"))
    private Long authorId;

    private String name;

    private String text;

    private String correctQuery;

    private Integer points;

    private Boolean isPrivate;

    private Long categoryId;
}
