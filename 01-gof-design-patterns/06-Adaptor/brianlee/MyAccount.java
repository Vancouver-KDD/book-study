package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian;

import java.util.Optional;

public class MyAccount implements Account {

    private final String email;
    private final String name;
    private final String password;
    private final String address;

    public MyAccount(String email, String name, String password, String address) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Optional<String> getPassword() {
        return Optional.of(this.password);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.of(this.address);
    }

    @Override
    public String toString() {
        return "MyAccount{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
