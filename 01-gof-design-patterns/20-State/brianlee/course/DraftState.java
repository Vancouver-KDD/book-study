package me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course;

import java.util.Collections;
import java.util.List;

public class DraftState implements CourseState {
    @Override
    public void addReview(String review, Student student) {
        System.out.println("Can not add reviews for Draft Course");
    }

    @Override
    public List<String> getReviews(Student student) {
        System.out.println("There is no review for Draft Course");
        return Collections.emptyList();
    }
}
