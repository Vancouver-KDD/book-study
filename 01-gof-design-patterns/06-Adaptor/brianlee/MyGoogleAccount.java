package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian;

import me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian.lib.GoogleAccount;

import java.util.Optional;

public class MyGoogleAccount implements Account {

    GoogleAccount googleAccount;

    public MyGoogleAccount(GoogleAccount googleAccount) {
        this.googleAccount = googleAccount;
    }

    @Override
    public String getEmail() {
        return this.googleAccount.getEmail();
    }

    @Override
    public Optional<String> getPassword() {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return this.googleAccount.getName();
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "MyAccount{" +
                "email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", address='" + getAddress() + '\'' +
                '}';
    }
}
