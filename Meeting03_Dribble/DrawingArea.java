//package Meeting03_Dribble;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingArea extends JPanel {
    private int height;
    private int width;
    private ArrayList<Ball> balls;
    private ArrayList<Wall> walls;
    private Thread animator;
    private BufferedImage drawingArea;
    private boolean pause = false;

    public DrawingArea(int width, int height, ArrayList<Ball> balls, ArrayList<Wall> walls) {
        super(null);
        this.height = height;
        this.width = width;
        setBounds(0, 0, width, height);
        this.balls = balls;
        this.walls = walls;
        animator = new Thread(this::eventLoop);
    }
    public void addBall(Ball ball) {
        balls.add(ball);
    }
    public void delBall(int index) {
        balls.remove(index - 1);
    }
    public void trigger() {
        if(!pause) pause = true;
        else pause = false;
    }
    public void start() {
        animator.start();
    }

    private void eventLoop() {
        drawingArea = (BufferedImage) createImage(width, height);
        while (true) {
            update();
            render();
            printScreen();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    private void update()
    {
        if(pause) return;
        for(Ball b : balls)
        {
            b.move();
            b.detectCollision(walls);
        }
    }

    private void render()
    {
        if(drawingArea != null)
        {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();

            //clear screen
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(new Font("Consolas", Font.PLAIN, 24));
            g.setColor(Color.BLACK);
            g.drawString("Press Space to show the control panel.", 20, 30);
            g.drawString("Press P to pause the simulation.", 20, 70);
            for(int i = 0; i < balls.size(); i++) {
                balls.get(i).draw(g);
                g.setFont(new Font("Consolas", Font.PLAIN, 24));
                g.drawString(Integer.toString(i+1), (int)balls.get(i).getX()-8, (int)balls.get(i).getY());
                g.setFont(new Font("Consolas", Font.PLAIN, 14));
                g.drawString("e:" + balls.get(i).getE(), (int)balls.get(i).getX()-14, (int)balls.get(i).getY()+(int)balls.get(i).getRadius() + 12);
            }

            for(Wall w : walls) {
                w.draw(g);
            }
        }
    }

    private void printScreen()
    {
        try
        {
            Graphics g = getGraphics();
            if(drawingArea != null && g != null)
            {
                g.drawImage(drawingArea, 0, 0, null);
            }

            // Sync the display on some systems.
            // (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception ex)
        {
            System.out.println("Graphics error: " + ex);
        }
    }
}
