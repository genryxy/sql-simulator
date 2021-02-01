package com.company.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
