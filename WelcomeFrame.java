import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {
    private WelcomePanel panel;

    public WelcomeFrame() {
        super("The Legend of Nath");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        
        try {
            ImageIcon icon = new ImageIcon("icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
        }

        panel = new WelcomePanel(this);
        add(panel);
        
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
}