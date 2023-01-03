public class MissileDecorator extends FighterDecorator {
    public MissileDecorator(Fighter _decoratedFighter) {
        super(_decoratedFighter);
    }
    @Override
    public void attack() {
        super.attack();
        System.out.println("미사일 발사");
    }
}
