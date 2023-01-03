package me.whiteship.designpatterns._03_behavioral_patterns._19_observer._02_after;

public class ClientApp {

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        User user1 = new User("yongju");
        User user2 = new User("haven");

        chatServer.register("DesignPattern", user1);
        chatServer.register("DesignPattern", user2);

        chatServer.register("Leetcode", user1);

        chatServer.sendMessage(user1, "DesignPattern", "공부 좀 하자!");
        chatServer.sendMessage(user2, "DesignPattern", "놉!!");

        chatServer.unregister("Leetcode", user2);

        chatServer.sendMessage(user2, "Leetcode", "DP hell");
    }
}
