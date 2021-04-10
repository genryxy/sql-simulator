package com.company.simulator.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CollectionTable;
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
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
@ToString(of = {"id", "name"})
@Table(name = "practice")
public class Practice implements Serializable {
    /**
     * ID of practice which is included common tasks. It is a common pool.
     * All students are available to solve tasks from this one.
     */
    public static final Long COMMON_POOL = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Practice name cannot be empty")
    private String name;

    @NotBlank(message = "Practice description cannot be empty")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @ManyToMany
    @JoinTable(
        name = "practice_x_team",
        joinColumns = @JoinColumn(name = "practice_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams;

    @ManyToMany(mappedBy = "practices")
    private Set<Task> tasks;
}
