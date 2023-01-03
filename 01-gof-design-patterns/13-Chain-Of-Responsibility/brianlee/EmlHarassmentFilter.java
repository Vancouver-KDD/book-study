package me.whiteship.designpatterns._03_behavioral_patterns._13_chain_of_responsibilities.brianlee;

import java.util.Optional;

public class EmlHarassmentFilter implements Email {
    private final Email email;

    EmlHarassmentFilter(Email email) {
        this.email = email;
    }

    @Override
    public Optional<String> getHeader(String headerName) {
        return email.getHeader(headerName);
    }

    @Override
    public String getBody() {
        String body = email.getBody();
        return  body.replaceAll("Oh my god", "Oh my goodness");
    }
}
