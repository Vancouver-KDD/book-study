package me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course;

import java.util.ArrayList;
import java.util.List;

public class PublicState implements CourseState {

    private final StudyCourse course;

    public PublicState(StudyCourse course) {
        this.course = course;
    }

    @Override
    public void addReview(String review, Student student) {
        this.course.getReviews().add(review);
    }

    @Override
    public List<String> getReviews(Student student) {
        return new ArrayList<>(course.getReviews());
    }
}
