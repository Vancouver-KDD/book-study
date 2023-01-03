package me.whiteship.designpatterns._01_creational_patterns._02_factory_method.brian;

public class LongmanFactory implements DictionaryFactory {

    @Override
    public Dictionary getDictionary(DictionaryType type) {
        switch (type) {
            case USA_KOR:
                return new LongmanUsaKor();
            default:
                throw new IllegalArgumentException("Longman doesn't support the type of dictionary " + type);
        }
    }
}
