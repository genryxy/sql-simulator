package com.company.simulator.processing;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.SubmissionRepo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TasksMarked {
    private final SubmissionRepo submRepo;

    @Autowired
    public TasksMarked(SubmissionRepo submRepo) {this.submRepo = submRepo;}

    /**
     * Marked tasks. If task was solved correct once, it would be marked as `correct`.
     * If there are not submissions for this task, this task will be marked as `not solved`.
     * @param tasks Collection of tasks which should be marked
     * @param practice Practice for which it is necessary to check
     * @param user User for whom it is necessary to check
     * @return Collection with marked tasks
     */
    public List<Task> markedStatus(Collection<Task> tasks, Practice practice, User user) {
        final List<Task> ctasks = new ArrayList<>(tasks);
        final Optional<List<Submission>> subms;
        subms = submRepo.findByUserAndPractice(user, practice);
        if (subms.isPresent()) {
            final Map<Long, Submission> submsMap = subms.get().stream()
                .collect(
                    Collectors.toMap(
                        item -> item.getTask().getId(),
                        Function.identity(),
                        (dupl1, dupl2) -> {
                            if (dupl1.isCorrect()) {
                                return dupl1;
                            }
                            return dupl2;
                        }
                    )
                );
            ctasks.stream()
                .filter(
                    task -> submsMap.containsKey(task.getId())
                ).forEach(
                task -> {
                    if (submsMap.get(task.getId()).isCorrect()) {
                        task.setState(Task.Status.CORRECT_SOLVED);
                    } else if (!task.getState().equals(Task.Status.CORRECT_SOLVED)) {
                        task.setState(Task.Status.WRONG_SOLVED);
                    }
                }
            );
        }
        return ctasks;
    }
}
