package me.whiteship.designpatterns._03_behavioral_patterns._17_mediator._02_after;

import java.time.LocalDateTime;

public class Guest {

    private final int id;
    private HotelFrontDesk frontDesk;

    public Guest(int id, HotelFrontDesk frontDesk) {
        this.id = id;
        this.frontDesk = frontDesk;
    }

    public void requestTowels(int numberOfTowels) {
        int towelCount = this.frontDesk.provideTowers(this, numberOfTowels);
        System.out.println("Guest got " + towelCount + " towels");
    }

    public void registerDinner(LocalDateTime dateTime) {
        System.out.println("Request dinner registration");
        this.frontDesk.registerDinner(this, dateTime);
    }

    public Integer getId() {
        return id;
    }
}
