import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run the welcome frame on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcome = new WelcomeFrame();
            welcome.setVisible(true);
        });
    }
}