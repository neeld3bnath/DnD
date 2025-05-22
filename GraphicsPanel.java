import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private BufferedImage background;
    private boolean[] pressedKeys;
    private Timer timer;
    private int time;
    private String anim;
    private int tempX;
    private int tempY;
    private static boolean lose;
    private String name;
    private BufferedImage black;
    private BufferedImage blank;

    public GraphicsPanel(String name) {
        this.name = name;
        lose = false;
        try {
            background = ImageIO.read(new File("src/Assets/maps/rsz_1mnqwyzz9e5r61.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            black = ImageIO.read(new File("src/Assets/bUKrna.jpeg"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            blank = ImageIO.read(new File("src/Assets/Blank.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        pressedKeys = new boolean[128];
        time = 0;
        timer = new Timer(1000, this); // this Timer will call the actionPerformed interface method every 1000ms = 1 second
        timer.start();
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
    }

    @Override
    public void paintComponent(Graphics g) {
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }  // unimplemented; if you move your mouse while clicking,
    // this method isn't called, so mouseReleased is best

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            System.out.println("LMB CLICKED");
            Point mouseClickLocation = e.getPoint();
            tempX = mouseClickLocation.x;
            tempY = mouseClickLocation.y;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("RMB CLICKED");
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            System.out.println("Scrollbutton clicked");
        }
    }

    public void mouseEntered(MouseEvent e) { } // unimplemented

    public void mouseExited(MouseEvent e) { } // unimplemented

    // ACTIONLISTENER INTERFACE METHODS: used for buttons AND timers!
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            time++;
        }
    }
    public static void lose() {
        GraphicsPanel.lose = true;
    }
}