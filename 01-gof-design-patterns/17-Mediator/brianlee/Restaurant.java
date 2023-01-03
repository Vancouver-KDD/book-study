package me.whiteship.designpatterns._03_behavioral_patterns._17_mediator._02_after;

import java.time.LocalDateTime;

public class Restaurant {

    private HotelFrontDesk frontDesk;

    public Restaurant(HotelFrontDesk frontDesk) {
        this.frontDesk = frontDesk;
    }
    public void registerDinner(Integer id, LocalDateTime dateTime) {
        System.out.println("registered " + frontDesk.getGuestName(id) + "'s dinner at " + dateTime);
    }
}
