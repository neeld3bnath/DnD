import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract public class Enemies {
    private double speed;
    private BufferedImage right;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private String sprite;
    private Animation idle;
    private double hp;
    private Animation run;
    private Animation death;
    private boolean spawned;
    private int drop;
    public Enemies(String rightImg, String sprite, double hp, double speed, int drop) {
        facingRight = false;
        this.speed = speed;
        xCoord = 2000;
        yCoord = 100;
        spawned = false;
        this.drop = drop;
        this.sprite = sprite;
        this.hp = hp;
        try {
            right = ImageIO.read(new File(rightImg));
        } catch (IOException e) {
            System.out.println(e.getMessage() + " " + this.sprite);
        }
    }
    public void setCoords(int x, int y) {
        xCoord = x;
        yCoord = y;
    }
    public void start() {
        spawned = true;
        xCoord = 713;
        yCoord = 222;
    }

    public void move() {    }

    public void despawn() {
        spawned = false;
    }

    public Rectangle enemyRect() {
        int imageHeight = right.getHeight();
        int imageWidth = right.getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }

    public BufferedImage getEnemyImage(String action) {
        if (action.equals("Run")) {
            return run.getActiveFrame();
        } else {
            return death.getActiveFrame();
        }
    }
    public int getDrop() {
        return drop;
    }
    public boolean isFacingRight() {
        return facingRight;
    }

    public int getxCoord() {
        if (facingRight) {
            return (int) xCoord;
        } else {
            return (int) (xCoord + (getEnemyImage("Run").getWidth()));
        }
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public void moveRight() {
        xCoord += speed;
    }

    public void moveLeft() {
        xCoord -= speed;
    }

    public void moveUp() {
        yCoord -= speed;
    }

    public void moveDown() {
        yCoord += speed;
    }

    public int getHeight() {
        return getEnemyImage("Idle").getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getEnemyImage("Idle").getWidth();
        } else {
            return getEnemyImage("Idle").getWidth() * -1;
        }
    }

    public boolean isSpawned() {
        return spawned;
    }

    public String test() {
        return sprite + " " + right;
    }
    public double getHp() {
        return hp;
    }
    public void hurt(double damage) {
        hp -= damage;
    }
    abstract public void damaged(double damage);
    public void setHp(double hp) {
        this.hp = hp;
    }
}