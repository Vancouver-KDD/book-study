package leetcode.commandPattern;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class DrawCanvas extends Canvas implements Drawable {

    // 그리기 색상
    private Color color = Color.red;
    // 그리는 점의 반경
    private int radius = 6;
    // 이력
    private MacroCommand history;

    public DrawCanvas(int width, int height, MacroCommand history) {
        setSize(width, height);
        setBackground(Color.white);
        this.history = history;
    }

    // 전체 이력 다시 그리기
    public void paint(Graphics g) {
        history.execute();
    }

    // 그리기
    public void draw(int x, int y) {
        Graphics g = getGraphics();
        g.setColor(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
