package me.whiteship.designpatterns._02_structural_patterns._09_decorator.brianlee;

/**
 * 결과를 동일시 하기 + Runtime 시점에서 동적으로 변경 가능
 */
public class ClientApp {
    public static void main(String[] args) {
        StringBuilder eml = new StringBuilder();
        eml.append("Header-Delivered-To: brian@gmail.com|");
        eml.append("Header-From: Aleen <aleen@gmail.com>|");
        eml.append("Header-Subject: Hello|");
        eml.append("Body-My resident registration number is 990101-1234567. Oh my god!!");

        Email email = new DefaultEmail(eml.toString());
        email = new EmlHarassmentFilter(email);
        email = new EmlSecurityFilter(email);

        System.out.println("From: " + email.getHeader("From"));
        System.out.println("To: " + email.getHeader("Delivered-To"));
        System.out.println("CC: " + email.getHeader("CC"));
        System.out.println("Subject: " + email.getHeader("Subject"));
        System.out.println("Body: " + email.getBody());
    }
}
