import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class DrawingArea extends JPanel {
    private boolean draw = false;
    private int width;
    private int height;
    private int originX;        // the origin points (0, 0)
    private int originY;
    private double scaleX;      //scaling the canvas according to lengthX & lengthY
    private double scaleY;
    private double lengthX;     // how many numbers shown along absis and ordinate
    private double lengthY;
    private double currentX;    // current X-point, the Y is retrieved from
    private double increment;   // controlling detail
    private final static int MAX_POINTS = 1000;    // in case the function is a loop, or the thread runs for far too long
    private ArrayList<Point2D.Double> points1 = new ArrayList<>();
    private ArrayList<Point2D.Double> points2 = new ArrayList<>();
    private Image drawingArea;
    private Thread animator;    // thread to draw the graph

    // setup the drawing area
    public DrawingArea(int width, int height, int cpWidth) {
        super(null);
        this.width = width - cpWidth;
        this.height = height;
        setBounds(cpWidth, 0, this.width, this.height);
        originX = this.width/2;
        originY = this.height/2;
        setBackground(Color.white);
        drawingArea = createImage(this.width, this.height);
    }

    // functions to draw on the screen
    private double function1(double x) {
        return 10*Math.sin(x); // Draws y = 10 sin(x)
    }

    private double function2(double x) {
        return 1/x; // Disjoints at 0. Normally.
    } // This creates a line that connects from 0, -inf to 0, inf. Probably due to the nature of how java draw lines (connect from a to b, or from [-0, -inf] to [0, inf])

    // start drawing graph
    public void beginDrawing(double lengthX, double lengthY, double startX, double increment) {
        // retrieve data
        this.lengthX = lengthX + 1;
        this.lengthY = lengthY + 1;
        this.scaleX = (double) (width - originX) / lengthX;
        this.scaleY = (double) (height - originY) / lengthY;
        this.currentX = startX;
        this.increment = increment;

        // trigger drawing process
        draw = true;
        drawingArea = createImage(width, height);
        animator = new Thread(this::eventLoop);
        animator.start();
    }

    private void eventLoop() {
        while(draw) {
            update();
            render();
            printScreen();
            try {
                animator.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // clean up the thread
        System.out.println("stopping");
        draw = false;
        points1.clear();
        points2.clear();
        try {
            animator.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // add points to be drawn
    private void update()
    {
        // while there is still a need to draw
        if(currentX < lengthX && currentX > -lengthX && points1.size() < MAX_POINTS)
        {
            System.out.println("checking");
            currentX = currentX + increment;
            points1.add(new Point2D.Double(currentX, function1(currentX)));
            points2.add(new Point2D.Double(currentX, function2(currentX)));
        }
        else {      // cleanup for the next thread
            draw = false;
        }
    }

    // draw the buffered area before showing to screen
    private void render() {
        if (drawingArea != null) {
            //get graphics of the image where coordinate and function will be drawn
            Graphics g = drawingArea.getGraphics();
            g.setColor(Color.BLACK);

            //draw the x-axis and y-axis
            g.drawLine(0, originY, width, originY);
            g.drawLine(originX, 0, originX, height);

            //print numbers on the x-axis and y-axis, based on the scale
            for (int i = 0; i < lengthX; i++) {
                g.drawString(Integer.toString(i), (int) (originX + (i * scaleX)), originY);
                g.drawString(Integer.toString(-1 * i), (int) (originX + (-i * scaleX)), originY);
            }
            for (int i = 0; i < lengthY; i++) {
                g.drawString(Integer.toString(-1 * i), originX, (int) (originY + (i * scaleY)));
                g.drawString(Integer.toString(i), originX, (int) (originY + (-i * scaleY)));
            }

            // draw the lines
            for (int i = 0; i < points1.size() - 1; i++) {
                g.setColor(Color.BLACK); // set first line color to black
                g.drawLine((int) (originX + points1.get(i).x * scaleX), (int) (originY - points1.get(i).y * scaleY),
                        (int) (originX + points1.get(i + 1).x * scaleX), (int) (originY - points1.get(i + 1).y * scaleY));
                g.setColor(Color.BLUE); // set second line color to blue
                g.drawLine((int) (originX + points2.get(i).x * scaleX), (int) (originY - points2.get(i).y * scaleY),
                        (int) (originX + points2.get(i + 1).x * scaleX), (int) (originY - points2.get(i + 1).y * scaleY));
            }
        }
    }

    // print the previously buffered area to screen
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