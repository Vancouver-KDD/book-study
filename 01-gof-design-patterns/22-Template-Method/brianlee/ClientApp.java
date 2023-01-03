package me.whiteship.designpatterns._03_behavioral_patterns._22_template.brianlee;

public class ClientApp {

    public static void main(String[] args) {
        Counter workflow = new Counter(new int[]{1,2,3});
        // total attachment count
        workflow.work((result, email) -> result + email.getAttachmentCount());
        workflow.print();

        // total count of to: brian
        workflow.work((result, email) -> result + (email.getHeader("To").equals("brian@gmail.com")? 1 : 0));
        workflow.print();
    }
}
