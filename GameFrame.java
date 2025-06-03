import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GameFrame extends JFrame {
    // Game state
    private boolean playerTurn = true;
    private boolean firstTurn = true;
    private boolean blitzkriegUsed = false;
    private boolean manAtMidAvailable = true;
    private final Map<String, Boolean> sealedPlayerAttacks = new HashMap<>();
    private final Map<String, Boolean> sealedEnemyAttacks = new HashMap<>();
    
    // UI Components
    private final String playerName;
    private JPanel mainPanel;
    private JLabel playerLabel, enemyLabel, playerHPLabel, enemyHPLabel;
    private JProgressBar playerHPBar, enemyHPBar;
    private JTextArea battleLog;
    private JButton[] attackButtons;
    private JButton[] defenseButtons;
    private JButton nextTurnButton;
    
    // Game stats
    private int playerHP = 100;
    private int enemyHP = 100;
    
    // Attack and defense definitions
    private final String[] attacks = {
        "Zero day exploit - Blitzkrieg",
        "XSS - steals opponent attack",
        "DoS - basic attack",
        "DDoS - flurry of attacks",
        "Insecure Wifi - corrupt attack",
        "Physical Security Flaw - Armor Weakness/Penetration",
        "Man At Mid - parry (once per wave)",
        "Spoofing - mimic"
    };
    
    private final String[] defenses = {
        "Access Control - dodge",
        "Firewalls and Advanced Security Devices - shield",
        "Data Backup - recovery"
    };
    
    public GameFrame(String name) {
        this.playerName = name;
        setTitle("Cyber Security Battle - " + playerName);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeUI();
        setupGame();
    }
    
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(230, 230, 250));
        
        // Top panel for player and enemy info
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        playerLabel = createCharacterPanel(this.playerName, 100);
        enemyLabel = createCharacterPanel("Enemy", 100);
        playerHPLabel = new JLabel("HP: 100/100");
        enemyHPLabel = new JLabel("HP: 100/100");
        
        playerHPBar = new JProgressBar(0, 100);
        enemyHPBar = new JProgressBar(0, 100);
        playerHPBar.setValue(100);
        enemyHPBar.setValue(100);
        playerHPBar.setStringPainted(true);
        enemyHPBar.setStringPainted(true);
        
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.add(playerLabel, BorderLayout.CENTER);
        playerPanel.add(playerHPLabel, BorderLayout.SOUTH);
        playerPanel.add(playerHPBar, BorderLayout.NORTH);
        
        JPanel enemyPanel = new JPanel(new BorderLayout());
        enemyPanel.add(enemyLabel, BorderLayout.CENTER);
        enemyPanel.add(enemyHPLabel, BorderLayout.SOUTH);
        enemyPanel.add(enemyHPBar, BorderLayout.NORTH);
        
        topPanel.add(playerPanel);
        topPanel.add(enemyPanel);
        
        // Battle log
        battleLog = new JTextArea(10, 40);
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(battleLog);
        
        // Attack buttons
        JPanel attackPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        attackButtons = new JButton[attacks.length];
        for (int i = 0; i < attacks.length; i++) {
            attackButtons[i] = new JButton("<html><center>" + attacks[i].split(" - ")[0] + "<br>" + 
                                       attacks[i].split(" - ")[1] + "</center></html>");
            attackButtons[i].addActionListener(new AttackButtonListener(i));
            attackPanel.add(attackButtons[i]);
            sealedPlayerAttacks.put(attacks[i], false);
        }
        
        // Defense buttons
        JPanel defensePanel = new JPanel(new GridLayout(1, 3, 5, 5));
        defenseButtons = new JButton[defenses.length];
        for (int i = 0; i < defenses.length; i++) {
            defenseButtons[i] = new JButton("<html><center>" + defenses[i].split(" - ")[0] + "<br>" + 
                                         defenses[i].split(" - ")[1] + "</center></html>");
            defenseButtons[i].addActionListener(new DefenseButtonListener(i));
            defensePanel.add(defenseButtons[i]);
        }
        
        // Next turn button
        nextTurnButton = new JButton("End Turn");
        nextTurnButton.addActionListener(e -> endPlayerTurn());
        
        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(attackPanel, BorderLayout.NORTH);
        bottomPanel.add(defensePanel, BorderLayout.CENTER);
        bottomPanel.add(nextTurnButton, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Initial game message
        logMessage("Battle started! Player's turn first.");
    }
    
    private JLabel createCharacterPanel(String name, int maxHP) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));
        
        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        nameLabel.setForeground(new Color(0, 255, 255));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel levelLabel = new JLabel("Lv. 1", JLabel.CENTER);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(nameLabel, BorderLayout.CENTER);
        panel.add(levelLabel, BorderLayout.SOUTH);
        
        JLabel container = new JLabel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 100);
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Draw background
                g2d.setColor(new Color(20, 20, 40));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw border
                g2d.setColor(new Color(0, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                // Draw grid lines
                g2d.setColor(new Color(0, 150, 150, 30));
                for (int i = 10; i < getWidth(); i += 10) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 10; i < getHeight(); i += 10) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
                
                g2d.dispose();
            }
        };
        
        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        
        return container;
    }
    
    protected void logMessage(String message) {
        battleLog.append(message + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
    
    private void setupGame() {
        // Initial game setup
        updateHPBars();
        
        // Style attack buttons
        for (JButton button : attackButtons) {
            styleButton(button, new Color(0, 100, 150));
        }
        
        // Style defense buttons with a different color
        for (JButton button : defenseButtons) {
            styleButton(button, new Color(100, 0, 150));
        }
        
        setButtonsEnabled(true, true); // Enable both attack and defense buttons
    }
    
    private void styleButton(JButton button, Color baseColor) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(baseColor);
        button.setForeground(button.isEnabled() ? Color.BLACK : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        
        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(baseColor.brighter());
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(baseColor);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }
    
    private void setButtonsEnabled(boolean attackEnabled, boolean defenseEnabled) {
        for (JButton button : attackButtons) {
            button.setEnabled(attackEnabled);
            button.setBackground(attackEnabled ? new Color(0, 100, 150) : Color.GRAY);
            button.setForeground(attackEnabled ? Color.BLACK : Color.WHITE);
        }
        for (JButton button : defenseButtons) {
            button.setEnabled(defenseEnabled);
            button.setBackground(defenseEnabled ? new Color(100, 0, 150) : Color.GRAY);
            button.setForeground(defenseEnabled ? Color.BLACK : Color.WHITE);
        }
    }
    
    protected void updateHPBars() {
        playerHPBar.setValue(playerHP);
        enemyHPBar.setValue(enemyHP);
        updateHPBarColor(playerHPBar, playerHP);
        updateHPBarColor(enemyHPBar, enemyHP);
        playerHPLabel.setText("HP: " + playerHP + "/100");
        enemyHPLabel.setText("HP: " + enemyHP + "/100");
    }
    
    private void updateHPBarColor(JProgressBar bar, int hp) {
        if (hp < 25) {
            bar.setForeground(Color.RED);
        } else if (hp < 50) {
            bar.setForeground(Color.ORANGE);
        } else {
            bar.setForeground(Color.GREEN);
        }
    }
    
    protected void endPlayerTurn() {
        playerTurn = false;
        logMessage("Player's turn ended. Enemy's turn!");
        setButtonsEnabled(false, false);
        nextTurnButton.setEnabled(false);
        
        // Process enemy turn after a short delay
        Timer timer = new Timer(1000, e -> {
            // Reset defense flags at the start of enemy turn
            for (JButton button : defenseButtons) {
                for (ActionListener listener : button.getActionListeners()) {
                    if (listener instanceof DefenseButtonListener) {
                        ((DefenseButtonListener) listener).resetDefenses();
                    }
                }
            }
            
            // Simple AI: Random attack or defense
            if (enemyHP > 0) {
                int action = (int)(Math.random() * 2); // 0 for attack, 1 for defense
                if (action == 0) { // Attack
                    int attackIndex = (int)(Math.random() * attacks.length);
                    if (sealedEnemyAttacks.getOrDefault(attacks[attackIndex], false)) {
                        // If attack is sealed, try another one
                        endPlayerTurn();
                        return;
                    }
                    logMessage("Enemy used " + attacks[attackIndex]);
                    
                    // Check if player dodged
                    boolean dodged = false;
                    boolean damageReduced = false;
                    
                    // Check all defense buttons for active defenses
                    for (JButton button : defenseButtons) {
                        for (ActionListener listener : button.getActionListeners()) {
                            if (listener instanceof DefenseButtonListener) {
                                DefenseButtonListener defense = (DefenseButtonListener) listener;
                                if (defense.isDodgeActive()) {
                                    int dodgeRoll = (int)(Math.random() * 20) + 1;
                                    logMessage("Dodge roll: " + dodgeRoll);
                                    if (dodgeRoll >= 10) {
                                        dodged = true;
                                    } else if (dodgeRoll <= 5) {
                                        logMessage("Dodge failed! You can't dodge the next attack.");
                                    }
                                    defense.resetDodge(); // Reset dodge after use
                                }
                                if (defense.isFirewallActive()) {
                                    damageReduced = true;
                                }
                            }
                        }
                    }
                    
                    if (dodged) {
                        logMessage("You dodged the enemy's attack!");
                    } else if (Math.random() > 0.3) { // 70% chance to hit
                        int damage = 5 + (int)(Math.random() * 15);
                        if (damageReduced) {
                            damage = Math.max(1, damage / 2); // At least 1 damage
                            logMessage("Firewall reduced the damage!");
                        }
                        playerHP = Math.max(0, playerHP - damage);
                        logMessage("Enemy dealt " + damage + " damage!");
                        updateHPBars();
                    } else {
                        logMessage("Enemy's attack missed!");
                    }
                } else { // Defense
                    int defenseIndex = (int)(Math.random() * defenses.length);
                    logMessage("Enemy used " + defenses[defenseIndex]);
                    
                    switch (defenseIndex) {
                        case 0: // Access Control - dodge
                            logMessage("Enemy is trying to dodge your next attack!");
                            break;
                            
                        case 1: // Firewall - reduce damage
                            logMessage("Enemy activated a firewall! Their next damage taken will be reduced by 50%.");
                            break;
                            
                        case 2: // Data Backup - heal
                            int healAmount = 15 + (int)(Math.random() * 10); // 15-25 HP
                            enemyHP = Math.min(100, enemyHP + healAmount);
                            logMessage("Enemy recovered " + healAmount + " HP!");
                            updateHPBars();
                            break;
                    }
                }
            }
            
            // Check win/lose conditions
            checkGameOver();
            
            // Start player's turn if game isn't over
            if (playerHP > 0 && enemyHP > 0) {
                playerTurn = true;
                firstTurn = false;
                logMessage("Your turn!");
                setButtonsEnabled(true, true);
                nextTurnButton.setEnabled(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ... (rest of the code remains the same)

    class AttackButtonListener implements ActionListener {
        private final int attackIndex;

        public AttackButtonListener(int index) {
            this.attackIndex = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!playerTurn) return;

            String attack = attacks[attackIndex];
            logMessage("You used " + attack);

            // Check if attack is sealed
            if (sealedPlayerAttacks.getOrDefault(attack, false)) {
                logMessage("But the attack is sealed!");
                return;
            }

            // Implement attack logic based on attack type
            switch (attackIndex) {
                case 3: // DDoS - Flurry
                    int numAttacks = (int)(Math.random() * 4) + 2; // 2-5 attacks
                    logMessage("Launching " + numAttacks + " attacks!");
                    int baseRoll = (int)(Math.random() * 20) + 1 + 5; // +5 as per rules
                    for (int i = 0; i < numAttacks; i++) {
                        int attackRoll = Math.max(5, baseRoll - (i * 3)); // Each attack is 3 lower than previous
                        if (Math.random() > 0.3) { // 70% hit chance
                            enemyHP = Math.max(0, enemyHP - attackRoll);
                            logMessage("Hit #" + (i+1) + ": " + attackRoll + " damage!");
                        } else {
                            logMessage("Attack #" + (i+1) + " missed!");
                        }
                    }
                    break;

                default:
                    // Default attack logic
                    int damage = 10 + (int)(Math.random() * 15);
                    enemyHP = Math.max(0, enemyHP - damage);
                    logMessage("Dealt " + damage + " damage!");
                    break;
            }

            updateHPBars();
            checkGameOver();

            // Auto-end turn after attack if game isn't over
            if (playerHP > 0 && enemyHP > 0) {
                endPlayerTurn();
            }
        }
    }

    class DefenseButtonListener implements ActionListener {
        private final int defenseIndex;
        private boolean dodgeActive = false;
        private boolean firewallActive = false;

        public DefenseButtonListener(int index) {
            this.defenseIndex = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!playerTurn) return;

            String defense = defenses[defenseIndex];
            logMessage("You used " + defense);

            switch (defenseIndex) {
                case 0: // Access Control - dodge
                    dodgeActive = true;
                    logMessage("Dodge activated! You'll get a dodge roll on the next attack.");
                    break;
                    
                case 1: // Firewall - reduce damage
                    firewallActive = true;
                    logMessage("Firewall active! Next attack's damage will be reduced by 50%.");
                    break;
                    
                case 2: // Data Backup - heal
                    int healAmount = 25; // 25% of max HP
                    playerHP = Math.min(100, playerHP + healAmount);
                    logMessage("Recovered " + healAmount + " HP!");
                    updateHPBars();
                    break;
            }
            
            // Auto-end turn after defense
            endPlayerTurn();
        }
        
        protected void resetDefenses() {
            firewallActive = false;
        }
        
        protected boolean isDodgeActive() {
            return dodgeActive;
        }
        
        protected boolean isFirewallActive() {
            return firewallActive;
        }
        
        protected void resetDodge() {
            dodgeActive = false;
        }
    }
    
    private void checkGameOver() {
        if (playerHP <= 0) {
            logMessage("Game Over! You were defeated...");
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
        } else if (enemyHP <= 0) {
            logMessage("Victory! You defeated the enemy!");
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
        }
    }
}
