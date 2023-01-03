package me.whiteship.designpatterns._03_behavioral_patterns._16_iterator.brianlee;

import java.sql.Blob;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MessageIterator implements Iterator<Blob> {

    private final MessageStore store;
    private final List<UUID> messageIds;

    private int idx;

    public MessageIterator(MessageStore store, List<UUID> messageIds) {
        this.store = store;
        this.messageIds = messageIds;
        this.idx = 0;
    }

    @Override
    public boolean hasNext() {
        if(this.idx < this.messageIds.size()) {
            return true;
        }
        return false;
    }

    @Override
    public Blob next() {
        return this.store.getMessage(messageIds.get(idx));
    }
}
