package com.company.simulator.processing;

import com.company.simulator.model.Practice;
import com.company.simulator.repos.PracticeRepo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PracticeFilter {
    private final PracticeRepo pracRepo;

    @Autowired
    public PracticeFilter(final PracticeRepo practiceRepo) {this.pracRepo = practiceRepo;}

    public Collection<Practice> filterByStatus(List<Practice> practices, String status) {
        final Collection<Practice> res;
        if (status == null) {
            res = practices;
        } else if (status.equals("active")) {
            res = practices.stream()
                .filter(
                    prac -> pracRepo.getDeadlineByPracticeId(prac.getId()).isAfter(LocalDateTime.now())
                ).collect(Collectors.toList());
        } else if (status.equals("archive")) {
            res = practices.stream()
                .filter(
                    prac -> pracRepo.getDeadlineByPracticeId(prac.getId()).isBefore(LocalDateTime.now())
                ).collect(Collectors.toList());
        } else {
            res = new ArrayList<>();
        }
        return res;
    }
}
