package me.whiteship.designpatterns._03_behavioral_patterns._17_mediator._02_after;

import java.time.LocalDateTime;

public class HotelFrontDesk {

    private CleaningService cleaningService = new CleaningService(this);

    private Restaurant restaurant = new Restaurant(this);

    public int provideTowers(Guest guest, int numberOfTowers) {
        return cleaningService.getTowels(guest.getId(), numberOfTowers);
    }

    public String getRoomNumberFor(int guestId) {
        return "1111";
    }

    public String getGuestName(int guestId) {
        return "Yong ju";
    }

    public void registerDinner(Guest guest, LocalDateTime dateTime) {
        restaurant.registerDinner(guest.getId(), dateTime);
    }
}
