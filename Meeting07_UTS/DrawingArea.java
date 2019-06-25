import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class DrawingArea extends JPanel {
    private int frameWidth;
    private int frameHeight;
    private Ball ball;
    private Block controlledBlock;
    private Wall[] walls;
    private ArrayList<Block> targets;
    private Thread animator;
    private BufferedImage dbImage;

    //Variables used for the ball speed		
    private double v;
    
    //Variables used for the blocks to be hit
	int ttlPerRow;
	int ttlPerCol;
	double targetWidth;
    double targetHeight;
    
    //Variables used for the arena
	double arenaX1;
	double arenaY1;
	double arenaX2;
	double arenaY2;
	
	//Variables used for the controlled block
	double vX;
	double blockWidth;
    double blockHeight;
    
    //Variables used to draw the xy-coordinate  
	private double scale;	// the number of pixels for each one step (the distance between two numbers / steps)
	private double maxNumX;	// total numbers (steps) in the x-axis
	private double maxNumY;	// total numbers (steps) in the y-axis
	private double maxX;	// total numbers (steps) in the x-axis
	private double maxY;	// total numbers (steps) in the y-axis	
	private int originX;	// the x origin
	private int originY;	// the y origin
    
    public DrawingArea(int frameWidth, int frameHeight) {
        super(null);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        setBounds(0, 0, frameWidth, frameHeight);
        this.ball = ball;
        this.walls = walls;
        this.controlledBlock = controlledBlock;
        this.targets = targets;

        //setting the point of origin of the coordinate
		originX = getWidth()/2;
		originY = getHeight()/2;
		
		//maximal numbers (steps) on x-axis and y-axis
		maxNumX = 20;
		scale = (double)(getWidth()-originX)/(maxNumX);
		maxNumY = (int) ((getHeight()-originY)/scale);

		//calculating the maximum value in the x-axis and y-axis
		maxX = (getWidth() - originX)/scale;
		maxY = (getHeight() - originY)/scale;
					
		//create the ball (x-axis,y-axis,radius,vx,vy,color) in cartesian scale
		v = -0.3 + Math.random()*0.3;
		ball = new Ball(0, 0, 0.2, v, v, Color.BLACK);
		
		//create the arena
		arenaX1 = -15; arenaX2 = 15;
        arenaY1 = 10; arenaY2 = -10;			
        walls = new Wall[4];
		walls[0] = new Wall(arenaX2, arenaY1, arenaX1, arenaY1, Color.BLACK);	//north wall (right to left)
		walls[1] = new Wall(arenaX2, arenaY2, arenaX2, arenaY1, Color.BLACK);	//east wall (bottom to top)
		walls[2] = new Wall(arenaX1, arenaY2, arenaX2, arenaY2, Color.BLACK);	//south wall (left to right)
		walls[3] = new Wall(arenaX1, arenaY1, arenaX1, arenaY2, Color.BLACK);	//west wall (top to bottom)

		//create the block to be controlled
		vX = 0.1;
		blockWidth = 1.5;
		blockHeight= 0.5;
		controlledBlock = new Block(0, -6, blockWidth, blockHeight, Color.BLACK);
		
		//setting the position for the blocks to be hit
		ttlPerRow = 10;
		ttlPerCol = 1;
		targetWidth = 1.5; targetHeight= 0.8;
		double targetX, targetY, incX, incY, leftMostX, rightMostX, topY, bottomY;
		leftMostX = arenaX1 + 0.5 + targetWidth/2; rightMostX = arenaX2 - 0.5 - targetWidth/2;
		topY = arenaY1 - 1; bottomY = 0;
		targetX = leftMostX; targetY = topY;
		incX = ((rightMostX - leftMostX) / (ttlPerRow-1));
		incY = 0;
        //create the blocks to be hit
        targets = new ArrayList<Block>();
		for(int i=0; i<ttlPerRow; i++)			
			targets.add(new Block(targetX + i * incX, targetY + i * incY, targetWidth, targetHeight, Color.BLUE));
        animator = new Thread(this::run);
    }
    		
	/**
	 * detect collision between a ball and a block
	 * @param ball
	 * @param block
	 */
	public void detectCollision(Ball ball, Block block)
	{		
		//check if the returned side is the correct one based on ball's velocity
		Wall closestSide = null;
		if(ball.getVx() >= 0 && ball.getVy() >= 0)
			closestSide = block.closestSide(ball.getX(), ball.getY(), 0, 1);
		else if(ball.getVx() >= 0 && ball.getVy() < 0)			
			closestSide = block.closestSide(ball.getX(), ball.getY(), 1, 2);
		else if(ball.getVx() < 0 && ball.getVy() >= 0)
			closestSide = block.closestSide(ball.getX(), ball.getY(), 0, 3);
		else if(ball.getVx() < 0 && ball.getVy() < 0)
			closestSide = block.closestSide(ball.getX(), ball.getY(), 2, 3);			
		detectCollision(ball, closestSide);					
	}
	
	/**
	 * detect collision between a ball and a wall
	 * @param ball
	 * @param wall
	 */
	public void detectCollision(Ball ball, Wall wall)
	{
		double dist = wall.distanceLineSegment(ball.getX(), ball.getY()); 
		if(dist <= ball.getR())
		{
			//get the normal line of the wall
			Vector normWall = wall.normalLine();
			
			//dot product with ball's velocity
			double dotProduct = normWall.getX() * ball.getVx() + normWall.getY() * ball.getVy();
			
			//new velocity
			double newVx = -2*dotProduct*normWall.getX() + ball.getVx();
			double newVy = -2*dotProduct*normWall.getY() + ball.getVy();
			
			//calculate distance between ball and wall				
			double error = Math.abs(dist - ball.getR());
			double oldV = Math.sqrt(ball.getVx()*ball.getVx() + ball.getVy()*ball.getVy());
			ball.move(ball.getX() - ball.getVx()/oldV*error, ball.getY() - ball.getVy()/oldV*error);

			//set the ball's velocity
			ball.setVx(newVx);
			ball.setVy(newVy);
		}				
	}
    public void start() {
        animator.start();
    }
    public void update()
	{	
		//move the controlled block
		if(controlledBlock.getX() + controlledBlock.getWidth()/2 >= arenaX2)			
		{
			vX = -vX;
			controlledBlock.setX(arenaX2 - controlledBlock.getWidth()/2);
		}
		if(controlledBlock.getX() - controlledBlock.getWidth()/2 <= arenaX1)
		{
			vX = -vX;
			controlledBlock.setX(arenaX1 + controlledBlock.getWidth()/2);
		}
		controlledBlock.setX(controlledBlock.getX() + vX);
					
		//move the ball
		ball.move();	
		
		//detect collision between ball and walls
		for(int i=0; i<walls.length; i++)
			detectCollision(ball, walls[i]);			
		
		//detect collision between ball and controlled block
		detectCollision(ball, controlledBlock);
					
		//detect collision between ball and targets
		for(Block t: targets)
			detectCollision(ball, t);
						
	}
	
	public void render()
	{
		if(dbImage != null)
		{				
			//get graphics of the image where coordinate and function will be drawn
			Graphics g = dbImage.getGraphics();
		
			//clear screen
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
							
			//draw the ball
			ball.draw(g, originX, originY, scale);
			
			//draw the controlled block
			controlledBlock.draw(g, originX, originY, scale);
			
			//draw the arena
			for(int i=0; i<walls.length; i++)
				walls[i].draw(g, originX, originY, scale);
			
			//draw the targets
			for(Block t: targets)
				t.draw(g, originX, originY, scale);
		}
	}
	
	public void printScreen()
	{
		try
		{
			Graphics g = getGraphics();
			if(dbImage != null && g != null)
			{
				g.drawImage(dbImage, 0, 0, null);
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
    public void run() {
        dbImage = (BufferedImage) createImage(frameWidth, frameHeight);
		while(true)
		{
			update();
			render();
			printScreen();
			try{
				animator.sleep(10);
			}
			catch(Exception ex){}
		}		
	}
}