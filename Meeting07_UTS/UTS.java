//package Meeting07_UTS;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JFrame;

/*
	Matfis UTS
	Brick breaker

	TODO:
	 1. Separate drawing area from frame
     2. Make more brick targets
     3. Increase brick size and lessen gaps among bricks
     4. Enlarge ball and change all colors in the game system
     5. Create point system
	 6. Make controlledBlock controllable by keyboard only
	 7. Make controlledBlock controllable by mouse only
     8. Make a multiplayer version (reference: Gamehouse's Hamsterball multiplayer)
 */


public class UTS {
	//Thread where animation is run
	private JFrame frame;
	
	//The blocks to be drawn
	Block controlledBlock;
	ArrayList<Block> targets = new ArrayList<>();
	
	//the ball to be drawn
	Ball ball;
	
	//the walls (arena)
	Wall[] walls = new Wall[4];

	
	//Variables used for the ball speed		
	private double v;
	
	//Variables used for the arena
	double arenaX1;
	double arenaY1;
	double arenaX2;
	double arenaY2;
	
	//Variables used for the controlled block
	double vX;
	double blockWidth;
	double blockHeight;
	
	//Variables used for the blocks to be hit
	int ttlPerRow;
	int ttlPerCol;
	double targetWidth;
	double targetHeight;
	
	//Variables used to draw the xy-coordinate  
	private double scale;	// the number of pixels for each one step (the distance between two numbers / steps)
	private double maxNumX;	// total numbers (steps) in the x-axis
	private double maxNumY;	// total numbers (steps) in the y-axis
	private double maxX;	// total numbers (steps) in the x-axis
	private double maxY;	// total numbers (steps) in the y-axis	
	private int originX;	// the x origin
	private int originY;	// the y origin
		
	/**
	 * 
	 */
	public UTS()
	{
		//configure the main canvas
		frame = new JFrame();
		frame.setSize(1366, 768);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setLayout(null);
		frame.setVisible(true);
		
		//start the thread to draw functions to canvas
		DrawingArea drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight());
		frame.add(drawingArea);
		drawingArea.start();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(UTS::new);
	}
}
