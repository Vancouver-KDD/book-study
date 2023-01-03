package me.whiteship.designpatterns._02_structural_patterns._09_decorator.brianlee;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultEmail implements Email {

    private final Map<String, String> headers;
    private final String body;

    public DefaultEmail(String emlString) {
        // Delimiter: |
        // Prefix of Header: Header-
        // Prefix of Body: Body-

        headers = new HashMap<>();
        body = emlString.split("Body-")[1].trim();

        String[] eml = emlString.split("\\|");
        for(String element: eml) {
            if(element.startsWith("Header-")) {
                String[] header = element.split(":");
                headers.put(header[0].replaceAll("Header-", "").trim(), header[1].trim());
            } else if(!element.startsWith("Body-")) {
                throw new IllegalArgumentException("Wrong EML Format");
            }
        }

        Collections.unmodifiableMap(headers);
    }

    @Override
    public Optional<String> getHeader(String headerName) {
        if(this.headers.containsKey(headerName)) {
            return Optional.of(this.headers.get(headerName));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String getBody() {
        return this.body;
    }
}
