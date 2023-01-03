package me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course;

import java.util.Collections;
import java.util.List;

public class PrivateState implements CourseState {

    private final StudyCourse course;

    public PrivateState(StudyCourse course) {
        this.course = course;
    }

    @Override
    public void addReview(String review, Student student) {
        if(this.course.getStudents().contains(student)) {
            this.course.getReviews().add(review);
        } else {
            System.out.println("Private Course students can add reviews");
        }
    }

    @Override
    public List<String> getReviews(Student student) {
        if(this.course.getStudents().contains(student)) {
            return this.course.getReviews();
        } else {
            System.out.println("Private Course students can see reviews");
            return Collections.emptyList();
        }
    }
}
