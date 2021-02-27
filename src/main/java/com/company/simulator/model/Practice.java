package com.company.simulator.model;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
@ToString(of = {"id", "name"})
@Table(name = "practice")
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Practice name cannot be empty")
    private String name;

    @CollectionTable(name = "person", joinColumns = @JoinColumn(name = "user_id"))
    @NotBlank(message = "Practice authorId cannot be empty")
    private Long authorId;

    @ManyToMany(mappedBy = "practices")
    private Set<Task> tasks;
}
