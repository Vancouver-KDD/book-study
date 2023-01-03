package me.whiteship.designpatterns._03_behavioral_patterns._17_mediator._02_after;

import java.time.LocalDateTime;

public class ClientApp {

    public static void main(String[] args) {
        Guest guest = new Guest(1, new HotelFrontDesk());
        guest.requestTowels(3);
        guest.registerDinner(LocalDateTime.now().plusHours(1));
    }
}
