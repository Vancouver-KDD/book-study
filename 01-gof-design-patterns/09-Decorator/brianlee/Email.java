package me.whiteship.designpatterns._02_structural_patterns._09_decorator.brianlee;

import java.util.Optional;

public interface Email {

    Optional<String> getHeader(String headerName);
    String getBody();
}
