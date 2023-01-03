package me.whiteship.designpatterns._03_behavioral_patterns._16_iterator.brianlee;

import java.sql.Blob;
import java.util.UUID;

public interface MessageStore {
    Blob getMessage(UUID messageId);
}
