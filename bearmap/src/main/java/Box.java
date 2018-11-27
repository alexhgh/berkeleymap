public class Box {
    public Line intervalX;
    public Line intervalY;

    public Box(Line intervalX, Line intervalY) {
        this.intervalX = intervalX;
        this.intervalY = intervalY;
    }

    public boolean intercept(Line x, Line y) {
        return (intervalX.intersects(x) && intervalY.intersects(y));
    }
}
