package com.company.simulator.access;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Student;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.StudentRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccessStudent {
    private final User user;

    private final StudentRepo studentRepo;

    public AccessStudent(User user, StudentRepo studentRepo) {
        this.user = user;
        this.studentRepo = studentRepo;
    }

    public boolean toPractice(Practice practice) {
        final Set<Team> teams = practice.getTeams();
        final List<Student> students = studentRepo.findAllByUserId(user.getId())
            .orElseGet(ArrayList::new);
        for (Student student : students) {
            if (teams.contains(student.getTeam())) {
                return true;
            }
        }
        return false;
    }

    public boolean toTeam(Team team) {
        final List<Student> students = studentRepo.findAllByUserId(user.getId())
            .orElseGet(ArrayList::new);
        for (Student student : students) {
            if (team.equals(student.getTeam())) {
                return true;
            }
        }
        return false;
    }
}
