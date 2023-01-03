package me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee;

import me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course.PrivateState;
import me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course.PublicState;
import me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course.Student;
import me.whiteship.designpatterns._03_behavioral_patterns._20_state.brianlee.course.StudyCourse;

public class ClientApp {

    public static void main(String[] args) {
        Student yongju = new Student("yongju", "용주");
        Student haven = new Student("haven", "현겸");
        StudyCourse course = new StudyCourse();
        course.addReview("It worth to take!", yongju);
        System.out.println(course.getReviews(yongju));

        course.setState(new PublicState(course));
        course.addReview("It worth to take!", yongju);
        System.out.println(course.getReviews(yongju));

        course.addReview("This course is so cool!!", haven);
        System.out.println(course.getReviews(haven));

        course.setState(new PrivateState(course));
        course.addReview("This is nice!!!", yongju);
        System.out.println(course.getReviews(yongju));

        course.addReview("I want to add a review!!!!", haven);
        System.out.println(course.getReviews(haven));

        course.addStudent(yongju);
        course.addReview("This is nice!!!", yongju);
        System.out.println(course.getReviews(yongju));
    }
}
