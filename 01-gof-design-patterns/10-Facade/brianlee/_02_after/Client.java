package me.whiteship.designpatterns._02_structural_patterns._10_facade._02_after;

public class Client {

    public static void main(String[] args) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setFrom("brian@gmail.com");
        emailMessage.setTo("aleen@gmail.com");
        emailMessage.setCc("yungju@gmail.com");
        emailMessage.setSubject("Let's study!");
        emailMessage.setText("Do you want to join our Desing Pattern study?");

        EmailSettings emailSettings = new EmailSettings();
        emailSettings.setHost("127.0.0.1");

        EmailSender emailSender = new EmailSender(emailSettings);
        emailSender.sendEmail(emailMessage);
    }
}
