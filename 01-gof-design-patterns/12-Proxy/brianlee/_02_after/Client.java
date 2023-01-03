package me.whiteship.designpatterns._02_structural_patterns._12_proxy._02_after;

public class Client {

    public static void main(String[] args) {
        GameService gameService = new GameServiceProxy();
        // Real Subject 는 필요한 메서드가 호출되었을대 생성된다.
        gameService.startGame();
    }
}
