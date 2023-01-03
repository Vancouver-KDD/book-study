public class LaserDecorator extends FighterDecorator {
    public LaserDecorator (Fighter _decoratedFighter) {
        super(_decoratedFighter);
    }
    @Override
    public void attack() {
        super.attack();
        System.out.println("레이저 발사");
    }
    
}
