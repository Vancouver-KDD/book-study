package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian.lib;

public class GoogleAccount {

    private final String email;
    private final String name;


    public GoogleAccount(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
