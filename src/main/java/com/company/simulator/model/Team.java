package com.company.simulator.model;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String invitation;

    /**
     * @TODO: Here should be an embeddable class because we need to
     * specify start and end dates in a separate table according to our
     * entity relationship diagram.
     */
    @ManyToMany(mappedBy = "teams")
    private Set<Practice> practices;

    @OneToMany(mappedBy = "team")
    private Set<Student> students;
}
