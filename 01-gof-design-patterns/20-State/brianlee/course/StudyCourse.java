package me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StudyCourse {

    private CourseState state;
    private List<Student> students;
    private List<String> reviews;

    public StudyCourse() {
        this.state = new DraftState();
        this.students = new ArrayList<>();
        this.reviews = new LinkedList<>();
    }

    public void addReview(String review, Student student) {
        this.state.addReview(review, student);
    }

    public List<String> getReviews(Student student) {
        return this.state.getReviews(student);
    }

    public void setState(CourseState state) {
        this.state = state;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    List<String> getReviews() {
        return reviews;
    }
}
