package me.whiteship.designpatterns._03_behavioral_patterns._22_template.brianlee;

import java.util.ArrayList;
import java.util.List;

public class Counter {

    private final List<Email> emails;
    private int count = 0;

    public Counter(int[] serialIds) {
        emails = new ArrayList<>();
        splitWorks(serialIds);
    }

    private void splitWorks(int[] serialIds) {
        for(int serialId: serialIds)
            emails.add(getEmailFromDB(serialId));
    }

    private Email getEmailFromDB(int serialId) {
        if(serialId == 1) return new Email("brian@gmail.com", "aleen@gmail.com", "Hi Aleen", 2);
        if(serialId == 2) return new Email("aleen@gmail.com", "brian@gmail.com", "Hi Brian", 3);
        if(serialId == 3) return new Email("rowan@gmail.com", "brian@gmail.com", "Hi Papa", 4);
        throw new IllegalArgumentException();
    }


    public final void work(Operator operator) {
        int result = 0;
        for(Email email: this.emails) {
            result = operator.getResult(result, email);
        }
        this.count = result;
    }

    public void print() {
        System.out.println("Count: " + count);
    }
}
