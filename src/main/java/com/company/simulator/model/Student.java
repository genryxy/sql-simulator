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
@Table(name = "student")
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public final class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // TODO: User may belong to different groups therefore
    // it is necessary to add an intermediate table to connect
    // two entities or it can be done by using `embeddedId`.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // TODO: Create `Group` model because a specific
    // may have some additional info and usually many
    // students belong to one group. Don't forget to add
    // foreign key in script.
    private Long groupId;
}
