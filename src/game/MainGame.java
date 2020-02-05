package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class MainGame {
    
    // GUI & Graficos
    public final GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    
    public int width = 500; // 320, 550
    public int height = 360; // 240, 360
    private int scale = 1;
    
    private JFrame window;
    private JLabel background;
    private JLabel titleImage;
    
    private JPanel mainMenuPanel;
    private JPanel playGamePanel;
    private JPanel loadMapPanel;
    
    private int difficulty;
    private JTextField playerName;
    
    // Jogo
    private HauntedHouseGame manualGame;
    //private GameSimulation simulateGame;
    
    private ImageIcon getSizedImage(String path, int w, int h) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            return null;
        }
        
        Image dimg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        
        return new ImageIcon(dimg);
    }
    
    public MainGame(String name, int scale) {
        /* Janela Principal */
        window = new JFrame(config);
        window.setLayout(new BorderLayout(0, 0));
        window.setResizable(false);
        window.setTitle(name);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (scale > 0 && scale < 4) {
            window.setSize((width = width * scale), (height = height * scale));
            this.scale = scale;
        } else {
            window.setSize(width, height);
            this.scale = 1;
        }
        window.setLocationRelativeTo(null);
        // Imagem de Fundo
        background = new JLabel();
        background.setIcon(getSizedImage("files/img/background.jpg", width, height));
        background.setLayout(new GridBagLayout());
        
        /** Main Menu */
        setMainMenu();
        background.add(mainMenuPanel);
        
        window.add(background);
        window.setVisible(true);
        
        /** Componentes do Jogo */
        manualGame = new HauntedHouseGame(this);
        // simulateGame = new GameSimulation(null);
    }
    
    /**
     * CRIAR BOTOES DO MENU 
     * - PLAY 
     *      - ESCOLHER DIFICULDADE 
     *      - SIMULAR JOGO (warning spoilers!) 
     *      - DETALHES DO JOGADOR (para classificação) 
     *      - etc. 
     *          - Classe do jogo manual iniciado 
     *          - VISUALIZAR MAPA VISTO (JMenuBar ou uma keybind) 
     * - VISUALIZAR MAPA (warning spoilers!) 
     * - CARREGAR MAPA (ficheiro JSON) (anteriores redirecionados para este se não houver mapa)
     */
    private void setMainMenu() {
        mainMenuPanel = new JPanel(new GridBagLayout());
        mainMenuPanel.setBackground(new Color(0,0,0,0));
        JButton buttonLoad, buttonPlay;
        Font font = new Font("Gabriola", Font.BOLD, 22);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Layout Configuration
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        // Title Image
        titleImage = new JLabel();
        titleImage.setIcon(getSizedImage("files/img/haunted_house.png", width / 4, height / 4));
        mainMenuPanel.add(titleImage, gbc);
        
        // Layout Configuration
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 300;
        gbc.ipady = 50;
        gbc.gridheight = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        // Play 
        setPlayMenu();
        buttonPlay = new JButton("Play");
        buttonPlay.setBackground(Color.darkGray);
        buttonPlay.setFont(font);
        buttonPlay.setForeground(Color.white);
        buttonPlay.addActionListener(new PlayActionListener());
        gbc.gridy = 1;
        mainMenuPanel.add(buttonPlay, gbc);
        // Load
        setLoadMenu();
        buttonLoad = new JButton("Carregar Mapa");
        buttonLoad.setBackground(Color.darkGray);
        buttonLoad.setFont(font);
        buttonLoad.setForeground(Color.white);
        buttonLoad.addActionListener(new LoadActionListener());
        gbc.gridy = 3;
        mainMenuPanel.add(buttonLoad, gbc);
    }
    
    private class BackButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            background.setIcon(getSizedImage("files/img/background.jpg", width, height));
            background.removeAll();
            background.add(mainMenuPanel);
            SwingUtilities.updateComponentTreeUI(window);
        }
        
    }
    
    private class DifficultyItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JRadioButton item = (JRadioButton) e.getItem();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                item.setBackground(Color.darkGray);
                item.setForeground(Color.white);
                switch(item.getText()) {
                    case "EASY": difficulty = 1; 
                        break;
                    case "MEDIUM": difficulty = 2;
                        break;
                    default: difficulty = 3;
                }
            } else {
                item.setBackground(Color.lightGray);
                item.setForeground(Color.black);
            }
        }
        
    }
    
    private class PlayActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            background.removeAll();
            background.add(playGamePanel);
            SwingUtilities.updateComponentTreeUI(window);
        }
        
    }
    
    private class StartGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = playerName.getText();
            if (name != null && !name.isEmpty())
                // simulateGame.simulation();
                difficulty = 0;
            else
                playerName.requestFocusInWindow();
        }
        
    }
    
    private class SimulateGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = playerName.getText();
            if (name != null && !name.isEmpty())
                manualGame.startGame();
            else
                playerName.requestFocusInWindow();
        }
        
    }
    
    private void setPlayMenu() {
        playGamePanel = new JPanel(new GridBagLayout());
        playGamePanel.setBackground(new Color(0,0,0,0));
        JPanel diffPanel; // difficulty buttons
        ButtonGroup diffGroup; // difficulty buttons
        JLabel nameLabel; // player name
        Font font = new Font("Gabriola", Font.BOLD, 22);
        JButton backButton, startButton, simulateButton; // game buttons
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Layout Configuration
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 200;
        gbc.ipady = 10;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Difficulty Buttons
        diffPanel = new JPanel(new GridLayout(1, 3));
        diffPanel.setOpaque(false);
        diffGroup = new ButtonGroup();
        JRadioButton[] diffButtons = { new JRadioButton("EASY"), 
            new JRadioButton("MEDIUM"), new JRadioButton("HARD") };
        for (JRadioButton b : diffButtons) {
            b.setBackground(Color.lightGray);
            b.addItemListener(new DifficultyItemListener());
            b.setFont(font);
            b.setForeground(Color.black);
            diffGroup.add(b);
            diffPanel.add(b);
        }
        diffButtons[1].setSelected(true);
        playGamePanel.add(diffPanel, gbc);
        
        // Layout Configuration
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 50;
        gbc.ipady = 5;
        gbc.gridx = 1;
        gbc.gridy = 1;
        // Player Name
        playerName = new JTextField();
        playGamePanel.add(playerName, gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        nameLabel = new JLabel("Player Name: ");
        nameLabel.setFont(font);
        nameLabel.setForeground(Color.white);
        playGamePanel.add(nameLabel, gbc);
        
        // Layout Configuration
        gbc.insets = new Insets(10, 100, 10, 100);
        gbc.ipadx = 300;
        gbc.ipady = 50;
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 2;
        // Simulation Button
        simulateButton = new JButton("Simulate Game");
        simulateButton.setBackground(Color.darkGray);
        simulateButton.setFont(font);
        simulateButton.setForeground(Color.white);
        simulateButton.addActionListener(new SimulateGameListener());
        playGamePanel.add(simulateButton, gbc);
        // Start Game Button
        gbc.gridy = 3;
        startButton = new JButton("Start Game");
        startButton.setBackground(Color.darkGray);
        startButton.setFont(font);
        startButton.setForeground(Color.white);
        startButton.addActionListener(new StartGameListener());
        playGamePanel.add(startButton, gbc);
        
        // Layout Configuration
        gbc.ipadx = 150;
        gbc.ipady = 25;
        gbc.gridy = 4;
        // MainMenu Back Button
        backButton = new JButton("Back to Main Menu");
        backButton.setBackground(Color.darkGray);
        backButton.setFont(font);
        backButton.setForeground(Color.white);
        backButton.addActionListener(new BackButtonListener());
        playGamePanel.add(backButton, gbc);
    }
    
    private class LoadActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            background.removeAll();
            background.add(loadMapPanel);
            SwingUtilities.updateComponentTreeUI(window);
        }
        
    }
    
    private void setLoadMenu() {
        loadMapPanel = new JPanel(new GridBagLayout());
        loadMapPanel.setBackground(new Color(0,0,0,0));
        JButton backButton;
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Layout Configuration
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 300;
        gbc.ipady = 50;
        gbc.gridheight = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Layout Configuration
        gbc.ipadx = 150;
        gbc.ipady = 25;
        gbc.gridy = 2;
        // MainMenu Back Button
        backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new BackButtonListener());
        loadMapPanel.add(backButton, gbc);
    }
    
    public int getScale() {
        return scale;
    }
    
    protected void setMainGameWindow(Canvas canvas) {
        background.removeAll();
        background.add(canvas, 0);
        SwingUtilities.updateComponentTreeUI(window);
    }
    
    public static void main(String[] args) {
        MainGame game = new MainGame("HauntedHouseGame", 2);
    }
}
