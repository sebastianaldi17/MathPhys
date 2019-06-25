
//package Meeting07_UTS;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

/*
	Matfis UTS
	Brick breaker

	TODO:
	 1. Separate drawing area from frame
     2. Make more brick targets
     3. Increase brick size and increase gaps among bricks
     4. Enlarge ball and change all colors in the game system
     5. Create point system
	 6. Make controlledBlock controllable by keyboard only
	 7. Make controlledBlock controllable by mouse only
     8. Make a multiplayer version (reference: Gamehouse's Hamsterball multiplayer)
 */

public class UTS {
	// Thread where animation is run
	private JFrame frame;

	// The blocks to be drawn
	Block controlledBlock;
	ArrayList<Block> targets = new ArrayList<>();

	// the ball to be drawn
	Ball ball;

	// the walls (arena)
	Wall[] walls = new Wall[4];

	// Variables used for the ball speed
	private double v;

	// Variables used for the arena
	double arenaX1;
	double arenaY1;
	double arenaX2;
	double arenaY2;

	// Variables used for the controlled block
	double vX;
	double blockWidth;
	double blockHeight;

	// Variables used for the blocks to be hit
	int ttlPerRow;
	int ttlPerCol;
	double targetWidth;
	double targetHeight;

	/**
	 * 
	 */
	public UTS() {
		// configure the main canvas
		frame = new JFrame();
		frame.setSize(1366, 768);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setLayout(null);
		frame.setVisible(true);

		// start the thread to draw functions to canvas
		DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight());
		frame.add(drawingArea);
		drawingArea.start();
		
		frame.addKeyListener(new KeyListener() {
		
		@Override public void keyTyped(KeyEvent e) {
		
		}
		
		@Override public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT)
				drawingArea.moveLeft();
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			drawingArea.moveRight();
		}
		
		@Override public void keyReleased(KeyEvent e) {
		
		}
		
		});
		
		frame.addMouseMotionListener(new MouseMotionListener(){
		
			@Override
			public void mouseMoved(MouseEvent e) {
				drawingArea.setControlX(e.getX());
			}
		
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(UTS::new);
	}
}
