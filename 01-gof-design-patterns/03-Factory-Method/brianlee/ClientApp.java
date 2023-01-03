package me.whiteship.designpatterns._01_creational_patterns._02_factory_method.brian;

public class ClientApp {

    private static DictionaryFactory dictionaryFactory;

    public static void main(String[] args) {
        dictionaryFactory = new LongmanFactory();
        runApp(DictionaryType.USA_KOR);

        dictionaryFactory = new OxfordFactory();
        runApp(DictionaryType.USA_KOR);
    }

    public static void runApp(DictionaryType type) {
        // 객체의 생성을 subclass 에게 맡긴다.
        // subclass 는 변경에 닫혀있고 생성되는 객체는 확장에 열려있다.
        Dictionary dic = dictionaryFactory.getDictionary(type);
        System.out.println(dic.getDescription("apple"));
        System.out.println(dic.getDescription("banana"));
    }
}
