package me.whiteship.designpatterns._03_behavioral_patterns._17_mediator._02_after;

public class CleaningService {

    private HotelFrontDesk frontDesk;

    public CleaningService(HotelFrontDesk frontDesk) {
        this.frontDesk = frontDesk;
    }

    public int getTowels(Integer guestId, int numberOfTowels) {
        String roomNumber = this.frontDesk.getRoomNumberFor(guestId);
        System.out.println("provided " + numberOfTowels + " towels to Room " + roomNumber);
        return numberOfTowels;
    }
}
