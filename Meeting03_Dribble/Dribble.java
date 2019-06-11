
//package Meeting03_Dribble;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/*
    MatFis pertemuan 3
    Collision between parabolically moving object against wall

    TODO:
     0. Review about elastic and inelastic collisions. What happened when you change the coefficient of resistution (COR)?
        The higher the coefficient, the bouncier the balls become (going past 1.0  or below 0.1 will cause some problems).
     1. Add more balls with different colors, sizes, and velocities
     2. Create UI to add new balls and delete some instances
     3. Add COR field to the UI, so user can choose between using different COR than the default or not
     4. Turn all balls into linearly moving ones (apply Newton's first law here).
     5. Create diagonal walls and modify the calculation to adjust with diagonal walls
     6. Create UI to customize the walls
 */

public class Dribble {
    private JFrame frame;
    private DrawingArea drawingArea;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    public Dribble() {
        // configure the main canvas
        frame = new JFrame("Dribbling Balls");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create control panel in separate frame
        JFrame control = new JFrame("Control Panel");
        control.setSize(500, 500);
        control.setBackground(Color.WHITE);
        control.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        control.setVisible(false);
    
        // Add listener to summon control panel
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {;
                if(KeyEvent.VK_SPACE == e.getKeyCode()) {
                    if(control.isVisible()) control.setVisible(false);
                    else control.setVisible(true);
                } else if(KeyEvent.VK_P == e.getKeyCode()) {
                    drawingArea.trigger(); // pause/play
                } else if(KeyEvent.VK_G == e.getKeyCode()) {
                    drawingArea.callGravity();
                }
            }
        });

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(8, 2));

        // pX, pY, radius, vX, vY, color
        JLabel posXL = new JLabel("Position X: ");
        JTextField posX = new JTextField("300");
        JLabel posYL = new JLabel("Position Y: ");
        JTextField posY = new JTextField("300");
        JLabel ballRL = new JLabel("Ball radius: ");
        JTextField ballR = new JTextField("25");
        JLabel velXL = new JLabel("Velocity X: ");
        JTextField velX = new JTextField("5");
        JLabel velYL = new JLabel("Velocity Y: ");
        JTextField velY = new JTextField("5");
        JLabel colL = new JLabel("Color: ");
        JPanel col = new JPanel();
        col.setLayout(new GridLayout(3, 2));
        JLabel rL = new JLabel("Red (0-255):");
        JLabel gL = new JLabel("Green (0-255):");
        JLabel bL = new JLabel("Blue (0-255):");
        JTextField rT = new JTextField("0");
        JTextField gT = new JTextField("100");
        JTextField bT = new JTextField("255");
        JLabel eL = new JLabel("Coefficient of Restitution:");
        JTextField eF = new JTextField("0.9");
        col.add(rL);
        col.add(rT);
        col.add(gL);
        col.add(gT);
        col.add(bL);
        col.add(bT);
        buttons.add(posXL);
        buttons.add(posX);
        buttons.add(posYL);
        buttons.add(posY);
        buttons.add(ballRL);
        buttons.add(ballR);
        buttons.add(velXL);
        buttons.add(velX);
        buttons.add(velYL);
        buttons.add(velY);
        buttons.add(colL);
        buttons.add(col);
        buttons.add(eL);
        buttons.add(eF);
        // Add "new ball" button
        JButton addBall = new JButton("Add new ball");
        addBall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ballX = Integer.parseInt(posX.getText());
                int ballY = Integer.parseInt(posY.getText());
                int ballRad = Integer.parseInt(ballR.getText());
                int ballVX = Integer.parseInt(velX.getText());
                int ballVY = Integer.parseInt(velY.getText());
                drawingArea.addBall(new Ball(ballX, ballY, ballRad, ballVX, ballVY, new Color(Integer.parseInt(rT.getText()), Integer.parseInt(gT.getText()), Integer.parseInt(bT.getText())), Double.parseDouble(eF.getText())));
            }
        });
        

        // Add control panel to new frame
        buttons.add(addBall);
        
        // Add "delete ball" button
        JPanel delBalls = new JPanel();
        delBalls.setLayout(new GridLayout(1, 2));
        JTextField index = new JTextField("1");
        JButton delButton = new JButton("<html>Delete by index<br/>     Input => </html>");
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingArea.delBall(Integer.parseInt(index.getText()));
            }
        });
        delBalls.add(delButton);
        delBalls.add(index);
        

        buttons.add(delBalls);
        
        control.add(buttons);
        
        // create the walls
        createWalls();

        // create the ball
        balls.add(new Ball(300, 200, 50, 10, 10, Color.blue, 0.8));
        balls.add(new Ball(300, 100, 20, 3, -3, Color.green, 0.7));
        balls.add(new Ball(300, 150, 25, -5, 5, Color.red, 0.6)); // here, another ball :P
        
        // Add the drawing area
        drawingArea = new DrawingArea(frame.getWidth(), frame.getHeight(), balls, walls);
        frame.add(drawingArea);
        drawingArea.start();
    }

    private void createWalls() {
        // vertical wall must be defined in clockwise direction
        // horizontal wall must be defined in counter clockwise direction

        walls.add(new Wall(1300, 100, 50, 100, Color.black));	// horizontal top
        walls.add(new Wall(50, 600, 1300, 600, Color.black));  // horizontal bottom
        walls.add(new Wall(1300, 100, 1300, 600, Color.black));  // vertical right
        walls.add(new Wall(500, 600, 50, 100, Color.black));  // vertical left
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Dribble::new);
    }
}
