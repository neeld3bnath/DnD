import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation implements ActionListener {
    private ArrayList<BufferedImage> frames;
    private Timer timer;
    private int currentFrame;

    public Animation(ArrayList<BufferedImage> frames, Timer timer) {
        this.frames = frames;
        currentFrame = 0;
        timer.addActionListener(this);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }

    public void resetFrames() {
        currentFrame = 0;
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            //This advances the animation to the next frame
            //It also uses modulus to reset the frame to the beginning after the last frame
            //In other words, this allows our animation to loop
            currentFrame = (currentFrame + 1) % frames.size();
        }
    }
}