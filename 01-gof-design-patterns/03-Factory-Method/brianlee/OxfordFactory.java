package me.whiteship.designpatterns._01_creational_patterns._02_factory_method.brian;

public class OxfordFactory implements DictionaryFactory {
    @Override
    public Dictionary getDictionary(DictionaryType type) {
        switch (type) {
            case USA_KOR:
                return new OxfordUsaKor();
            default:
                throw new IllegalArgumentException("Oxford doesn't support the type of dictionary " + type);
        }
    }
}
