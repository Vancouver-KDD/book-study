package me.whiteship.designpatterns._03_behavioral_patterns._22_template.brianlee;

import java.util.HashMap;
import java.util.Map;

public class Email {

    private final Map<String, String> headers;
    private final int attachmentCount;
    private final String boy;

    public Email(String from, String to, String boy, int attachmentCount) {
        this.headers = new HashMap<>();
        this.headers.put("From", from);
        this.headers.put("To", to);
        this.boy = boy;
        this.attachmentCount = attachmentCount;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public String getHeader(String type) {
        return this.headers.get(type);
    }
}
