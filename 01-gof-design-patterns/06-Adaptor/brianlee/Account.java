package me.whiteship.designpatterns._02_structural_patterns._06_adapter.brian;

import java.util.Optional;

public interface Account {

    String getEmail();
    Optional<String> getPassword();
    String getName();
    Optional<String> getAddress();
}
