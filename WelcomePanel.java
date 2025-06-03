import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WelcomePanel extends JPanel implements ActionListener {

    private JTextField textField;
    private JButton startButton;
    private JButton exitButton;
    private JFrame enclosingFrame;
    private BufferedImage backgroundImage;

    public WelcomePanel(JFrame frame) {
        try {
            // Try to load a cyberpunk background image
            backgroundImage = ImageIO.read(new File("cyber_bg.jpg"));
        } catch (IOException e) {
            // If image loading fails, we'll just use a solid color
            backgroundImage = null;
        }
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 50, 10, 50);
        
        // Title label with styling
        JLabel titleLabel = new JLabel("CYBER SECURITY BATTLE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 255, 255)); // Cyan color
        add(titleLabel, gbc);
        
        // Subtitle
        JLabel subTitle = new JLabel("Enter your hacker name:");
        subTitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subTitle.setForeground(Color.WHITE);
        add(subTitle, gbc);
        
        // Text field with styling
        textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(new Color(30, 30, 40));
        textField.setForeground(Color.CYAN);
        textField.setCaretColor(Color.CYAN);
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));
        add(textField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        
        startButton = createStyledButton("START GAME");
        exitButton = createStyledButton("EXIT");
        
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        
        startButton.addActionListener(this);
        exitButton.addActionListener(e -> System.exit(0));
        
        add(buttonPanel, gbc);
        
        // Set the preferred size
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(10, 10, 20));
        
        enclosingFrame = frame;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 100, 150));
        button.setForeground(Color.BLACK); // Changed from WHITE to BLACK
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));
        button.setPreferredSize(new Dimension(180, 50));
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 200));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setForeground(Color.BLACK); // Ensure text stays black on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 150));
                button.setForeground(Color.BLACK); // Ensure text stays black when not hovering
            }
        });
        
        return button;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background image if available, otherwise use solid color
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            // Add a dark overlay for better text visibility
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Gradient background
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(10, 10, 30);
            Color color2 = new Color(0, 50, 70);
            GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Add some cyberpunk-style decorations
        g.setColor(new Color(0, 255, 255, 30));
        for (int i = 0; i < getWidth(); i += 50) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += 50) {
            g.drawLine(0, i, getWidth(), i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            final String name = textField.getText().trim().isEmpty() ? "Hacker" : textField.getText().trim();
            
            // Start the game with the player's name
            SwingUtilities.invokeLater(() -> {
                GameFrame game = new GameFrame(name);
                game.setVisible(true);
                enclosingFrame.dispose();
            });
        }
    }
}