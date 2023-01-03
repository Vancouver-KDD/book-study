package me.whiteship.designpatterns._02_structural_patterns._09_decorator.brianlee;

import java.util.Optional;
import java.util.regex.Pattern;

public class EmlSecurityFilter implements Email {

    private static final Pattern RRN_PATTERN =
            Pattern.compile("\\d{2}([0]\\d|[1][0-2])([0][1-9]|[1-2]\\d|[3][0-1])[-]*[1-4]\\d{6}");

    private final Email email;

    EmlSecurityFilter(Email email) {
        this.email = email;
    }

    @Override
    public Optional<String> getHeader(String headerName) {
        return email.getHeader(headerName);
    }

    @Override
    public String getBody() {
        String body = email.getBody();
        return  RRN_PATTERN.matcher(body).replaceAll("******-*******");
    }
}
