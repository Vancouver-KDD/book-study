public abstract class FighterDecorator implements Fighter {
    private Fighter decoratedFighter;
    public FighterDecorator(Fighter _decoratedFighter) {
        decoratedFighter = _decoratedFighter;
    }

    @Override
    public void attack () {
        decoratedFighter.attack();
    }
}
