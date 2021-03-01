package com.company.simulator.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class StudentPK implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;

    public StudentPK(Long userId, Long teamId) {
        this.userId = userId;
        this.teamId = teamId;
    }
}
