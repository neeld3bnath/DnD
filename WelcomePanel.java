import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WelcomePanel extends JPanel implements ActionListener {

    private JTextField player1Field;
    private JTextField player2Field;
    private JButton startButton;
    private JButton exitButton;
    private JFrame enclosingFrame;
    private BufferedImage backgroundImage;

    public WelcomePanel(JFrame frame) {
        try {
            backgroundImage = ImageIO.read(new File("cyber_bg.jpg"));
        } catch (IOException e) {
            backgroundImage = null;
        }
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 50, 10, 50);
        
        JLabel titleLabel = new JLabel("The Legend of Nath") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(100, 200, 255),
                    getWidth(), 0, new Color(255, 100, 100)
                );
                
                g2.setPaint(gradient);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(getFont().deriveFont(Font.BOLD, 32f));
                
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setOpaque(false);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);
        
        JLabel player1Label = new JLabel("Player 1 (Hacker 1):");
        player1Label.setFont(new Font("Arial", Font.PLAIN, 16));
        player1Label.setForeground(new Color(100, 200, 255));
        add(player1Label, gbc);
        
        player1Field = new JTextField(15);
        styleTextField(player1Field);
        player1Field.setForeground(new Color(100, 200, 255));
        player1Field.setCaretColor(new Color(100, 200, 255));
        add(player1Field, gbc);
        
        JLabel player2Label = new JLabel("Player 2 (Hacker 2):");
        player2Label.setFont(new Font("Arial", Font.PLAIN, 16));
        player2Label.setForeground(new Color(255, 100, 100));
        add(player2Label, gbc);
        
        player2Field = new JTextField(15);
        styleTextField(player2Field);
        player2Field.setForeground(new Color(255, 100, 100));
        player2Field.setCaretColor(new Color(255, 100, 100));
        add(player2Field, gbc);
        
        gbc.insets = new Insets(30, 50, 10, 50);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        
        startButton = createStyledButton("START GAME");
        exitButton = createStyledButton("EXIT");
        
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        
        startButton.addActionListener(this);
        exitButton.addActionListener(e -> System.exit(0));
        
        add(buttonPanel, gbc);
        
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(10, 10, 20));
        
        enclosingFrame = frame;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 100, 150));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));
        button.setPreferredSize(new Dimension(180, 50));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 200));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 150));
                button.setForeground(Color.BLACK);
            }
        });
        
        return button;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBackground(new Color(30, 30, 40));
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(10, 10, 30);
            Color color2 = new Color(0, 50, 70);
            GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
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
            String player1Name = player1Field.getText().trim().isEmpty() ? "Hacker 1" : player1Field.getText().trim();
            String player2Name = player2Field.getText().trim().isEmpty() ? "Hacker 2" : player2Field.getText().trim();
            
            SwingUtilities.invokeLater(() -> {
                GameFrame game = new GameFrame(player1Name, player2Name);
                game.setVisible(true);
                enclosingFrame.dispose();
            });
        }
    }
}