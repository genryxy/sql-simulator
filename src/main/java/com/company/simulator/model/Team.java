package com.company.simulator.model;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "author", "invitation", "name"})
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

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
