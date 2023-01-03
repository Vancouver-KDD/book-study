package me.whiteship.designpatterns._03_behavioral_patterns._13_chain_of_responsibilities.brianlee;

/**
 * Decorator : 결과를 동일시 하기 + Runtime 시점에서 동적으로 변경 가능 => 기존 결과를 동적으로 변경하는데 초점
 * Chain of Responsibility : 체인을 구성해서 원하는 순서대로 실행할 수 있다 => 어떤 작업을 처리하는 과정중에 여러 책임을 지나도록 설계하는 패턴
 */
public class ClientApp {
    public static void main(String[] args) {
        StringBuilder eml = new StringBuilder();
        eml.append("Header-Delivered-To: brian@gmail.com|");
        eml.append("Header-From: Aleen <aleen@gmail.com>|");
        eml.append("Header-Subject: Hello|");
        eml.append("Body-My resident registration number is 990101-1234567. Oh my god!!");

        Email email = new EmlSecurityFilter(new EmlHarassmentFilter(new DefaultEmail(eml.toString())));

        System.out.println("From: " + email.getHeader("From"));
        System.out.println("To: " + email.getHeader("Delivered-To"));
        System.out.println("CC: " + email.getHeader("CC"));
        System.out.println("Subject: " + email.getHeader("Subject"));
        System.out.println("Body: " + email.getBody());
    }
}
