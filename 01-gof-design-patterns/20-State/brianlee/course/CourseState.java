package me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course;

import java.util.List;

public interface CourseState {

    void addReview(String review, Student student);
    List<String> getReviews(Student student);
}
