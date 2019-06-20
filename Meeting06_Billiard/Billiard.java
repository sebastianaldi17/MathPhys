//package Meeting06_Billiard;

/*
	Matfis pertemuan 6
	Half-fledged billiard

	TODO:
	 1. Create billiard balls, use 8-ball rules.
	 2. Assign one ball to be the hitter (preferably not colored white)
	 3. Create guideline to help aiming the hitter (refer to #04 Clasher)
	 4. Add collision for ball against wall and ball against balls.
	 5. Create holes for the balls
	 6. Add additional mechanics for the game and scoring system
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class Billiard {
	private JFrame frame;
	private Vector destination;
	private int frameHeight;

	//The collections of walls to be drawn
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Ball> balls = new ArrayList<>();
	private Ball hitter;

	private Billiard() {
		//configure the main canvas
		frame = new JFrame("Billiard");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
		frameHeight = frame.getHeight() - frame.getInsets().top;
		createObjects();

		destination = new Vector(hitter.getPositionX(), hitter.getPositionY());
		DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frameHeight, balls, walls, destination, hitter);
		// Add mouse listeners
		frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                drawingArea.setPress(true);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                double distanceX = e.getX() - hitter.getPositionX();
                double distanceY = e.getY() - hitter.getPositionY();
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                hitter.setVelocityX(-drawingArea.getTime() * distanceX / distance);
                hitter.setVelocityY(-drawingArea.getTime() * distanceY / distance);

                drawingArea.setPress(false);
            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                destination.setX((double) e.getX());
                destination.setY((double) e.getY());
            }
        });
		
		frame.add(drawingArea);

		drawingArea.start();
	}

	private void createObjects() {
		int wallWidth = (int) (frame.getWidth() * 0.9);
		int wallHeight = (int) (frameHeight * 0.6);
		int wallX = (int) (frame.getWidth() * 0.05);
		int wallY = (int) (frameHeight * 0.2);

		// vertical wall must be defined in clockwise direction
		// horizontal wall must be defined in counter clockwise direction
		walls.add(new Wall(wallWidth + wallX, wallY, wallX, wallY));	// top wall
		walls.add(new Wall(wallX, wallHeight + wallY, wallX, wallY));	// left wall
		walls.add(new Wall(wallWidth + wallX, wallY, wallWidth + wallX, wallHeight + wallY));	// bottom wall
		walls.add(new Wall(wallWidth + wallX, wallHeight + wallY, wallX, wallHeight + wallY));	// right wall

		// setup inital ball coordinates
		// "Nyontek dosa" - BeefBurrito
		int ballCounter = 0;
		int[] order = {9,7,12,8,15,6,10,3,14,11,2,13,4,5};
		balls.add(new Ball(frame.getWidth()*2/3 - Ball.RADIUS-1, frameHeight/2, randomColor(), 1)); // -1 is due to OCD kicks in
		for(int i = 2; i <= 5; i++) {
			for(int j = 1; j <= i; j++) {
				double padUp = (i-1)*Ball.RADIUS + Ball.RADIUS;
				double padRight = (i-1)*Ball.RADIUS*Math.sqrt(3);
				Color color = randomColor();
				if(order[ballCounter] == 8) color = Color.black;
				balls.add(new Ball(frame.getWidth()*2/3 - Ball.RADIUS + padRight, frameHeight/2 - Ball.RADIUS - padUp + j*2*Ball.RADIUS, color, order[ballCounter]));
				ballCounter++;
			}
		}
		// Add hitter
		hitter = new Ball(frame.getWidth()/3, frameHeight/2, new Color(0xdbdbdb), 0);
		balls.add(hitter);
	}
	private Color randomColor() {
		Random randomGenerator = new Random();
		Color color = new Color(randomGenerator.nextInt(255), randomGenerator.nextInt(255), randomGenerator.nextInt(255));
		return color.darker();
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(Billiard::new);
	}
}
