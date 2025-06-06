import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GameFrame extends JFrame {
    private int currentPlayer = 1;
    private int turnCount = 0;
    private boolean[] hasUsedZeroDay = {false, false};
    @SuppressWarnings("unchecked")
    private final Map<String, Boolean>[] sealedAttacks = (Map<String, Boolean>[]) new Map[2];
    private JLabel turnLabel;

    private final String player1Name;
    private final String player2Name;
    private JPanel mainPanel;
    private JLabel playerLabel, enemyLabel, playerHPLabel, enemyHPLabel;
    private JProgressBar playerHPBar, enemyHPBar;
    private JTextArea battleLog;
    private JButton[] attackButtons;
    private JButton[] defenseButtons;
    private JButton nextTurnButton;

    private int[] playerHP = {200, 200};

    private final String[] attacks = {
            "Zero day exploit - Blitzkrieg",
            "DoS - basic attack",
            "DDoS - flurry of attacks",
            "Insecure Wifi - corrupt attack",
            "Spoofing - mimic"
        };

    private final String[] defenses = {
        "Access Control - dodge",
        "Firewalls and Advanced Security Devices - shield",
        "Data Backup - recovery"
    };

    public GameFrame(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        setTitle("The Legend of Nath - " + player1Name + " vs " + player2Name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));

        sealedAttacks[0] = new HashMap<>();
        sealedAttacks[1] = new HashMap<>();

        initializeUI();
        setupGame();
    }
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(230, 230, 250));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 10));

        JPanel player1Panel = new JPanel(new BorderLayout(5, 5));
        playerLabel = createCharacterPanel(player1Name, 100);
        playerHPLabel = new JLabel("HP: 100/100");
        playerHPBar = new JProgressBar(0, 100);
        playerHPBar.setValue(100);
        playerHPBar.setStringPainted(true);
        playerHPBar.setForeground(Color.GREEN);

        JPanel player1Info = new JPanel(new BorderLayout(5, 5));
        player1Info.add(playerLabel, BorderLayout.CENTER);
        player1Info.add(playerHPLabel, BorderLayout.SOUTH);
        player1Info.add(playerHPBar, BorderLayout.NORTH);

        JPanel player2Panel = new JPanel(new BorderLayout(5, 5));
        enemyLabel = createCharacterPanel(player2Name, 100);
        enemyHPLabel = new JLabel("HP: 100/100");
        enemyHPBar = new JProgressBar(0, 100);
        enemyHPBar.setValue(100);
        enemyHPBar.setStringPainted(true);
        enemyHPBar.setForeground(Color.GREEN);

        JPanel player2Info = new JPanel(new BorderLayout(5, 5));
        player2Info.add(enemyLabel, BorderLayout.CENTER);
        player2Info.add(enemyHPLabel, BorderLayout.SOUTH);
        player2Info.add(enemyHPBar, BorderLayout.NORTH);

        topPanel.add(player1Panel);
        topPanel.add(player2Panel);

        player1Panel.add(player1Info, BorderLayout.CENTER);
        player2Panel.add(player2Info, BorderLayout.CENTER);
        turnLabel = new JLabel("Current Turn: " + player1Name, JLabel.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnLabel.setForeground(Color.YELLOW);
        JPanel turnPanel = new JPanel(new BorderLayout());
        turnPanel.setBackground(new Color(30, 30, 40));
        turnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        turnPanel.add(turnLabel, BorderLayout.CENTER);
        
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(topPanel, BorderLayout.CENTER);
        topContainer.add(turnPanel, BorderLayout.SOUTH);

        playerHP[0] = 200;
        playerHP[1] = 200;

        battleLog = new JTextArea(10, 40);
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(battleLog);

        JPanel attackButtonPanel = new JPanel(new GridLayout(1, 4, 15, 5));
        attackButtonPanel.setBackground(new Color(40, 40, 50));
        attackButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel defenseButtonPanel = new JPanel(new GridLayout(1, 3, 20, 5));
        defenseButtonPanel.setBackground(new Color(40, 40, 50));
        defenseButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

        attackButtons = new JButton[attacks.length];
        for (int i = 0; i < attacks.length; i++) {
            attackButtons[i] = new JButton("<html><div style='text-align: center; padding: 5px;'>" + 
                                       attacks[i].split(" - ")[0] + "<br>" +
                                       attacks[i].split(" - ")[1] + "</div></html>");
            attackButtons[i].setPreferredSize(new Dimension(200, 60));
            attackButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            attackButtons[i].setMargin(new Insets(5, 5, 5, 5));
            attackButtons[i].addActionListener(new AttackButtonListener(i));
            attackButtonPanel.add(attackButtons[i]);
            sealedAttacks[0].put(attacks[i], false);
            sealedAttacks[1].put(attacks[i], false);
        }

        defenseButtons = new JButton[defenses.length];
        for (int i = 0; i < defenses.length; i++) {
            defenseButtons[i] = new JButton("<html><div style='text-align: center; padding: 5px;'>" + 
                                         defenses[i].split(" - ")[0] + "<br>" +
                                         defenses[i].split(" - ")[1] + "</div></html>");
            defenseButtons[i].setPreferredSize(new Dimension(240, 60));
            defenseButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            defenseButtons[i].setMargin(new Insets(5, 5, 5, 5));
            defenseButtons[i].addActionListener(new DefenseButtonListener(i));
            defenseButtonPanel.add(defenseButtons[i]);
        }

        nextTurnButton = new JButton("End Turn");
        nextTurnButton.addActionListener(e -> endPlayerTurn());

        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(new Color(40, 40, 50));
        buttonContainer.add(Box.createVerticalStrut(5));
        buttonContainer.add(attackButtonPanel);
        buttonContainer.add(Box.createVerticalStrut(10));
        buttonContainer.add(defenseButtonPanel);
        buttonContainer.add(Box.createVerticalStrut(10));
        
        JPanel nextTurnPanel = new JPanel();
        nextTurnPanel.setBackground(new Color(40, 40, 50));
        nextTurnPanel.add(nextTurnButton);
        
        bottomPanel.add(buttonContainer, BorderLayout.CENTER);
        bottomPanel.add(nextTurnPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createCharacterPanel(String name, int maxHP) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                Color color1, color2;
                if (name.equals(player1Name) && currentPlayer == 1) {
                    color1 = new Color(0, 30, 60);
                    color2 = new Color(0, 80, 120);
                } else if (name.equals(player2Name) && currentPlayer == 2) {
                    color1 = new Color(60, 0, 0);
                    color2 = new Color(120, 0, 0);
                } else {
                    color1 = new Color(30, 30, 40);
                    color2 = new Color(50, 50, 60);
                }
                
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(0, 200, 200, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 200), 2));

        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        nameLabel.setForeground(new Color(0, 255, 255));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setOpaque(false);

        panel.add(nameLabel, BorderLayout.CENTER);

        JLabel container = new JLabel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 100);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                g2d.setColor(new Color(0, 0, 0, 0));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(0, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

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

    private void updateTurnLabel() {
        String currentPlayerName = (currentPlayer == 1) ? player1Name : player2Name;
        turnLabel.setText("Current Turn: " + currentPlayerName);
        turnLabel.setForeground((currentPlayer == 1) ? new Color(100, 200, 255) : new Color(255, 50, 50));
    }

    private void setupGame() {
        updateHPBars();

        for (int i = 0; i < sealedAttacks.length; i++) {
            sealedAttacks[i] = new HashMap<>();
            hasUsedZeroDay[i] = false;
        }
        
        if (attackButtons != null && attackButtons.length > 0) {
            attackButtons[0].setEnabled(true);
        }
        
        logMessage("=== The Legend of Nath ===");
        logMessage(player1Name + " vs " + player2Name);
        logMessage("\n" + player1Name + ", it's your turn first!");

        for (JButton button : attackButtons) {
            styleButton(button, new Color(0, 100, 150));
        }
        for (JButton button : defenseButtons) {
            styleButton(button, new Color(100, 0, 150));
        }

        updateTurnLabel();
        setButtonsEnabled(true, true);
    }

    private void styleButton(JButton button, Color baseColor) {
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(baseColor.brighter());
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(baseColor);
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void setButtonsEnabled(boolean attackEnabled, boolean defenseEnabled) {
        boolean gameOver = (playerHP[0] <= 0 || playerHP[1] <= 0);
        if (gameOver) {
            attackEnabled = false;
            defenseEnabled = false;
        }
        
        for (int i = 0; i < attackButtons.length; i++) {
            JButton button = attackButtons[i];
            String attackName = attacks[i];
            boolean isSealed = sealedAttacks[currentPlayer-1].getOrDefault(attackName, false);
            boolean isZeroDayUsed = (i == 0 && hasUsedZeroDay[currentPlayer-1]);
            
            boolean shouldEnable = attackEnabled && !isSealed && !isZeroDayUsed;
            button.setEnabled(shouldEnable);
            
            if (shouldEnable) {
                button.setBackground(new Color(0, 100, 150));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(180, 180, 180));
                button.setForeground(Color.DARK_GRAY);
            }
        }
        
        for (JButton button : defenseButtons) {
            boolean shouldEnable = defenseEnabled && !gameOver;
            button.setEnabled(shouldEnable);
            
            if (shouldEnable) {
                button.setBackground(new Color(100, 0, 150));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(180, 180, 180));
                button.setForeground(Color.DARK_GRAY);
            }
        }
        
        nextTurnButton.setEnabled(!gameOver && (attackEnabled || defenseEnabled));
        
        revalidate();
        repaint();
    }

    protected void updateHPBars() {
        playerHPBar.setMaximum(200);
        playerHPBar.setValue(playerHP[0]);
        updateHPBarColor(playerHPBar, playerHP[0]);
        playerHPLabel.setText("HP: " + playerHP[0] + "/200");

        enemyHPBar.setMaximum(200);
        enemyHPBar.setValue(playerHP[1]);
        updateHPBarColor(enemyHPBar, playerHP[1]);
        enemyHPLabel.setText("HP: " + playerHP[1] + "/200");
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
        setButtonsEnabled(false, false);
        nextTurnButton.setEnabled(false);
        
        revalidate();
        repaint();

        // Increment turn count after each full round (both players have taken a turn)
        if (currentPlayer == 2) {
            turnCount++;
        }
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        updateTurnLabel();
        
        if (playerLabel != null && enemyLabel != null) {
            playerLabel.getParent().repaint();
            enemyLabel.getParent().repaint();
        }

        Timer timer = new Timer(1000, e -> {
            if (playerHP[0] <= 0 || playerHP[1] <= 0) {
                checkGameOver();
                return;
            }
            
            updateTurnLabel();
            
            String currentPlayerName = (currentPlayer == 1) ? player1Name : player2Name;
            logMessage(currentPlayerName + ", it's your turn!");
            
            updateButtonStates();
            updateButtonStates();
            
            setButtonsEnabled(true, true);
            
            revalidate();
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateButtonStates() {
        boolean gameOver = (playerHP[0] <= 0 || playerHP[1] <= 0);
        
        for (int i = 0; i < attackButtons.length; i++) {
            String attackName = attacks[i];
            boolean isSealed = sealedAttacks[currentPlayer-1].getOrDefault(attackName, false);
            
            attackButtons[i].setOpaque(true);
            attackButtons[i].setBorderPainted(false);
            attackButtons[i].setFocusPainted(false);
            
            boolean shouldEnable = !gameOver && !isSealed && (i != 0 || !hasUsedZeroDay[currentPlayer-1]);
            attackButtons[i].setEnabled(shouldEnable);
            
            if (isSealed) {
                attackButtons[i].setBackground(new Color(150, 150, 150));
                attackButtons[i].setForeground(Color.WHITE);
                attackButtons[i].setToolTipText("Sealed! Cannot use this attack.");
            } else if (i == 0 && hasUsedZeroDay[currentPlayer-1]) {
                attackButtons[i].setBackground(new Color(150, 150, 150));
                attackButtons[i].setForeground(Color.WHITE);
                attackButtons[i].setToolTipText("Already used Zero Day Exploit!");
            } else if (!shouldEnable) {
                attackButtons[i].setBackground(new Color(180, 180, 180));
                attackButtons[i].setForeground(Color.DARK_GRAY);
            } else {
                attackButtons[i].setBackground(new Color(0, 100, 150));
                attackButtons[i].setForeground(Color.WHITE);
                attackButtons[i].setToolTipText(null);
            }
        }
        
        for (JButton button : defenseButtons) {
            button.setEnabled(!gameOver);
            if (gameOver) {
                button.setBackground(new Color(180, 180, 180));
                button.setForeground(Color.DARK_GRAY);
            } else {
                button.setBackground(new Color(100, 0, 150));
                button.setForeground(Color.WHITE);
            }
        }
        
        revalidate();
        repaint();
    }

    class AttackButtonListener implements ActionListener {
        private final int attackIndex;

        public AttackButtonListener(int index) {
            this.attackIndex = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
            
            int targetPlayer = (currentPlayer == 1) ? 1 : 0;

            String attack = attacks[attackIndex];
            String currentPlayerName = (currentPlayer == 1) ? player1Name : player2Name;
            logMessage(currentPlayerName + " used " + attack);
            if (sealedAttacks[currentPlayer-1] != null && sealedAttacks[currentPlayer-1].getOrDefault(attack, false)) {
                logMessage("Attack sealed! No effect.");
            } else {
                switch (attackIndex) {
                    case 0: // Zero day exploit - Blitzkrieg
                        if (hasUsedZeroDay[currentPlayer-1]) {
                            logMessage("Zero Day Exploit has been disabled!");
                            break;
                        }
                        
                        boolean isFirstTurn = !hasUsedZeroDay[currentPlayer-1] && turnCount == 0;
                        
                        if (isFirstTurn) {
                            int roll = 1 + (int)(Math.random() * 20);
                            int modifiedRoll = (roll <= 10) ? (roll + 10) : roll;
                            
                            if (Math.random() <= 0.01) { // 1% chance to miss
                                logMessage("Zero Day Exploit missed! (Roll: " + roll + ")");
                            } else {
                                int damage = modifiedRoll;
                                if (isTargetDodging()) {
                                    int dodgeRoll = 1 + (int)(Math.random() * 20);
                                    if (dodgeRoll >= 10) { // 55% chance to dodge (11-20)
                                        logMessage("DODGED! " + (currentPlayer == 1 ? player2Name : player1Name) + " avoided the attack! (Dodge roll: " + dodgeRoll + ")");
                                    } else {
                                        damage = applyFirewallReduction(damage, targetPlayer);
                                        playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - damage);
                                        logMessage("Zero Day Exploit dealt " + damage + " damage! (Dodge roll: " + dodgeRoll + ")");
                                    }
                                } else {
                                    damage = applyFirewallReduction(damage, targetPlayer);
                                    playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - damage);
                                    logMessage("Zero Day Exploit dealt " + damage + " damage! (Roll: " + roll + " → " + modifiedRoll + ")");
                                }
                            }
                            hasUsedZeroDay[currentPlayer-1] = true;
                            logMessage("Zero Day Exploit has been disabled after use!");
                        } else {
                            int roll = 1 + (int)(Math.random() * 20);
                            int modifiedRoll = Math.max(0, roll - 5);
                            
                            int perceptionRoll = 1 + (int)(Math.random() * 20);
                            
                            if (perceptionRoll > modifiedRoll) {
                                logMessage("Zero Day Exploit failed! " + (currentPlayer == 1 ? player2Name : player1Name) + 
                                    " detected the attack! (Your roll: " + roll + " → " + modifiedRoll + 
                                    " vs Perception: " + perceptionRoll + ")");
                                hasUsedZeroDay[currentPlayer-1] = true; // Disable after being countered
                            } else {
                                int damage = modifiedRoll;
                                if (isTargetDodging()) {
                                    int dodgeRoll = 1 + (int)(Math.random() * 20);
                                    if (dodgeRoll >= 10) { // 55% chance to dodge (11-20)
                                        logMessage("DODGED! " + (currentPlayer == 1 ? player2Name : player1Name) + " avoided the attack! (Dodge roll: " + dodgeRoll + ")");
                                    } else {
                                        damage = applyFirewallReduction(damage, targetPlayer);
                                        playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - damage);
                                        logMessage("Zero Day Exploit dealt " + damage + " damage! (Dodge roll: " + dodgeRoll + ")");
                                    }
                                } else {
                                    damage = applyFirewallReduction(damage, targetPlayer);
                                    playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - damage);
                                    logMessage("Zero Day Exploit dealt " + damage + " damage! (Roll: " + roll + " → " + modifiedRoll + 
                                        " vs Perception: " + perceptionRoll + ")");
                                }
                                hasUsedZeroDay[currentPlayer-1] = true; // Disable after successful attack
                                logMessage("Zero Day Exploit has been disabled after use!");
                            }
                        }
                        break;

                    case 1: // DoS - basic attack
                        int basicDamage1 = 10 + (int)(Math.random() * 15); // 10-25 damage
                        boolean attackHit = true;
                        
                        if (isTargetDodging()) {
                            int dodgeRoll = 1 + (int)(Math.random() * 20);
                            if (dodgeRoll >= 10) { // 55% chance to dodge (11-20)
                                logMessage("DODGED! " + (currentPlayer == 1 ? player2Name : player1Name) + " avoided the attack! (Dodge roll: " + dodgeRoll + ")");
                                attackHit = false;
                            } else {
                                logMessage("Dodge failed! (Roll: " + dodgeRoll + ")");
                            }
                        }
                        
                        if (attackHit) {
                            int originalDamage = basicDamage1;
                            basicDamage1 = applyFirewallReduction(basicDamage1, targetPlayer);
                            playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - basicDamage1);
                            if (basicDamage1 < originalDamage) {
                                logMessage("Dealt " + basicDamage1 + " damage with a basic attack! (Reduced from " + originalDamage + " by firewall)");
                            } else {
                                logMessage("Dealt " + basicDamage1 + " damage with a basic attack!");
                            }
                        }
                        break;

                    case 2: // DDoS - Flurry
                        int numAttacks = (int)(Math.random() * 4) + 2; // 2-5 attacks
                        logMessage("Launching " + numAttacks + " attacks!");
                        int baseRoll = (int)(Math.random() * 10) + 1 + 5; // 6-15 base damage
                        for (int i = 0; i < numAttacks; i++) {
                            int attackRoll = Math.max(3, baseRoll - (i * 2));
                            if (Math.random() > 0.3) { // 70% hit chance
                                if (isTargetDodging()) {
                                    int dodgeRoll = 1 + (int)(Math.random() * 20);
                                    if (dodgeRoll >= 10) { // 55% chance to dodge (11-20)
                                        logMessage("Attack #" + (i+1) + " DODGED! (Dodge roll: " + dodgeRoll + ")");
                                    } else {
                                        attackRoll = applyFirewallReduction(attackRoll, targetPlayer);
                                        playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - attackRoll);
                                        logMessage("Hit #" + (i+1) + ": " + attackRoll + " damage! (Dodge roll: " + dodgeRoll + ")");
                                    }
                                } else {
                                    attackRoll = applyFirewallReduction(attackRoll, targetPlayer);
                                    playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - attackRoll);
                                    logMessage("Hit #" + (i+1) + ": " + attackRoll + " damage!");
                                }
                            } else {
                                logMessage("Attack #" + (i+1) + " missed!");
                            }
                        }
                        break;

                    case 3: // Insecure Wifi - corrupt attack
                        if (Math.random() > 0.5) { // 50% chance to corrupt
                            int corruptDamage = 5 + (int)(Math.random() * 10); // 5-15 damage
                            if (isTargetDodging()) {
                                int dodgeRoll = 1 + (int)(Math.random() * 20);
                                if (dodgeRoll >= 10) { // 55% chance to dodge (11-20)
                                    logMessage("DODGED! " + (currentPlayer == 1 ? player2Name : player1Name) + " avoided the corruption! (Dodge roll: " + dodgeRoll + ")");
                                } else {
                                    corruptDamage = applyFirewallReduction(corruptDamage, targetPlayer);
                                    playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - corruptDamage);
                                    logMessage("Corrupted connection for " + corruptDamage + " damage! (Dodge roll: " + dodgeRoll + ")");
                                }
                            } else {
                                corruptDamage = applyFirewallReduction(corruptDamage, targetPlayer);
                                playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - corruptDamage);
                                logMessage("Corrupted connection for " + corruptDamage + " damage!");
                            }
                        } else {
                            logMessage("Corruption attempt failed!");
                        }
                        break;
                        
                    case 4: // Spoofing - mimic
                        int spoofRoll = 1 + (int)(Math.random() * 20);
                        logMessage("Spoofing roll: " + spoofRoll);
                        
                        if (spoofRoll >= 18) {
                            // Seal a random attack from opponent
                            int attackToSeal = (int)(Math.random() * attacks.length);
                            String attackName = attacks[attackToSeal];
                            sealedAttacks[targetPlayer].put(attackName, true);
                            logMessage("Spoofing successful! Sealed " + attackName + "!");
                            updateButtonStates();
                        } else if (spoofRoll <= 5) {
                            // Backfire - seal two of current player's attacks
                            int sealedCount = 0;
                            for (int i = 1; i < attacks.length && sealedCount < 2; i++) {
                                if (!sealedAttacks[currentPlayer-1].getOrDefault(attacks[i], false)) {
                                    sealedAttacks[currentPlayer-1].put(attacks[i], true);
                                    logMessage("Spoofing backfired! Sealed " + attacks[i] + "!");
                                    sealedCount++;
                                }
                            }
                            if (sealedCount == 0) {
                                logMessage("Spoofing backfired but no attacks could be sealed!");
                            }
                            updateButtonStates();
                        } else {
                            logMessage("Spoofing had no effect.");
                        }
                        break;



                    default:
                        int damage = 10 + (int)(Math.random() * 15);
                        playerHP[targetPlayer] = Math.max(0, playerHP[targetPlayer] - damage);
                        logMessage("Dealt " + damage + " damage with a basic attack!");
                        break;
                }
            }

            updateHPBars();
            checkGameOver();

            if (playerHP[0] > 0 && playerHP[1] > 0) {
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
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
            
            String defense = defenses[defenseIndex];
            String currentPlayerName = (currentPlayer == 1) ? player1Name : player2Name;
            logMessage(currentPlayerName + " used " + defense);

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
                    int maxHP = 200;
                    int healAmount = 25;
                    int newHP = Math.min(maxHP, playerHP[currentPlayer-1] + healAmount);
                    int actualHeal = newHP - playerHP[currentPlayer-1];
                    if (actualHeal > 0) {
                        playerHP[currentPlayer-1] = newHP;
                        logMessage(currentPlayerName + " recovered " + actualHeal + " HP!");
                        updateHPBars();
                    } else {
                        logMessage(currentPlayerName + " is already at full health!");
                    }
                    break;
            }
            
            endPlayerTurn();
        }
        
        protected void resetDefenses() {
            dodgeActive = false;
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
        
        protected void resetFirewall() {
            firewallActive = false;
        }
    }
    
    private boolean isTargetDodging() {
        int targetButtonIndex = (currentPlayer == 1) ? 0 : 0;
        for (ActionListener listener : defenseButtons[targetButtonIndex].getActionListeners()) {
            if (listener instanceof DefenseButtonListener) {
                DefenseButtonListener dbl = (DefenseButtonListener) listener;
                if (dbl.isDodgeActive()) {
                    int roll = 1 + (int)(Math.random() * 20);
                    dbl.resetDodge();
                    if (roll <= 5) {
                        logMessage("Dodge failed automatically! (Roll: " + roll + " <= 5)");
                        return false;
                    }
                    boolean dodged = roll >= 10;
                    logMessage("Dodge " + (dodged ? "succeeded" : "failed") + "! (Roll: " + roll + ")");
                    return dodged;
                }
            }
        }
        return false;
    }
    
    private int applyFirewallReduction(int damage, int targetPlayer) {
        int targetButtonIndex = (targetPlayer == 1) ? 0 : 1;
        for (ActionListener listener : defenseButtons[targetButtonIndex].getActionListeners()) {
            if (listener instanceof DefenseButtonListener) {
                DefenseButtonListener dbl = (DefenseButtonListener) listener;
                if (dbl.isFirewallActive()) {
                    int reducedDamage = Math.max(1, damage / 2);
                    logMessage("Firewall reduced damage from " + damage + " to " + reducedDamage + "!");
                    dbl.resetFirewall();
                    return reducedDamage;
                }
            }
        }
        return damage;
    }

    private void checkGameOver() {
        if (playerHP[0] <= 0 && playerHP[1] <= 0) {
            logMessage("Game Over! It's a draw!");
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
            updateButtonStates();
        } else if (playerHP[0] <= 0) {
            logMessage("Game Over! " + player2Name + " wins!");
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
            updateButtonStates();
        } else if (playerHP[1] <= 0) {
            logMessage("Game Over! " + player1Name + " wins!");
            setButtonsEnabled(false, false);
            nextTurnButton.setEnabled(false);
            updateButtonStates();
        }
    }
}
