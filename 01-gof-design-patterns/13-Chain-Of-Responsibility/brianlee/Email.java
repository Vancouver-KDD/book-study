package me.whiteship.designpatterns._03_behavioral_patterns._13_chain_of_responsibilities.brianlee;

import java.util.Optional;

public interface Email {

    Optional<String> getHeader(String headerName);
    String getBody();
}
