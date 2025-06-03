import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {
    private WelcomePanel panel;

    public WelcomeFrame() {
        super("Cyber Security Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null); // Center on screen
        
        // Set application icon
        try {
            ImageIcon icon = new ImageIcon("icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Icon not found, use default
        }

        // Create and add panel
        panel = new WelcomePanel(this);
        add(panel);
        
        // Enable anti-aliasing for better text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
}