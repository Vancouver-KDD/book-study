package me.whiteship.designpatterns._01_creational_patterns._02_factory_method.brian;

public class OxfordUsaKor implements Dictionary {
    @Override
    public String getDescription(String word) {
        switch (word) {
            case "apple":
                return "맛있는 사과";
            default:
                return "단어가 존재하지 없습니다";
        }
    }
}
