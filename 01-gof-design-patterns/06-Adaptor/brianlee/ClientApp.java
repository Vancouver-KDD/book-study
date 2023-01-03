package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian;

import me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian.lib.GoogleAuthService;

public class ClientApp {

    public static void main(String[] args) {
        MyAuthService myAuthService = new MyAuthService(new GoogleAuthService());

        // user1: dohunkim@pattern.com
        // user2: havenkim@pattern.com
        // user3: brianlee@gmail.com
        System.out.println(myAuthService.login("dohunkim@pattern.com", "1234"));
        System.out.println(myAuthService.login("havenkim@pattern.com", "5678"));
        System.out.println(myAuthService.login("brianlee@gmail.com", "520460946EE727EE354F0D3DF0856482"));
        System.out.println(myAuthService.login("unkwon@hotmail.com", "1234"));
    }
}
