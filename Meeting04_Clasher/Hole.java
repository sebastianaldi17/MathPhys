//package Meeting04_Clasher;

import java.awt.*;
import java.util.ArrayList;

public class Hole {
    private double positionX;               // center of hole's position
    private double positionY;
    private double radius;    
    private Color ballColor = Color.black;

    public Hole(double positionX, double positionY, double radius) {
        this.radius = radius;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getRadius() {
        return radius;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    // drawing function
    public void draw(Graphics g) {
        Color tempColor = g.getColor();
        g.setColor(ballColor);
        g.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) (2 * radius), (int) (2 * radius));
        g.setColor(tempColor);
    }

    public double distance(Ball other) {
        double distanceX = this.positionX - other.getPositionX();
        double distanceY = this.positionY - other.getPositionY();
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }
}
