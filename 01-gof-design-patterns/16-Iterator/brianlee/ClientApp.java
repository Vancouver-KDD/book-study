package me.whiteship.designpatterns._03_behavioral_patterns._16_iterator.brianlee;

import java.sql.Blob;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ClientApp {

    // Store 객체가 있다고 가정
    static MessageStore store;

    public static void main(String[] args) {
        List<UUID> messageIds = new LinkedList<>();
        int size = 1000;
        while(size-- > 0) {
            messageIds.add(UUID.randomUUID());
        }
        Iterator<Blob> messageIterator = new MessageIterator(store, messageIds);
        // 장점
        // 1. message 하나당 store 를 한번만 호출 할 수 있다.
        // 2. while 루프 안에서 메모리가 생성되고 소멸될 수 있다.
        while(messageIterator.hasNext()) {
            Blob message = messageIterator.next();
        }
    }
}
