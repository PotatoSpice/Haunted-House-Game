package game;

import collections.exceptions.ElementNotFoundException;
import controllers.ClassificationManager;
import controllers.GameNetwork;
import controllers.MapReader;
import game.hhgame.HauntedHouseGame;
import java.awt.BorderLayout;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import game.hhgame.HauntedHouseGame.UpdateGameListener;
import java.util.Iterator;

/**
 * Classe que representa o Menu Principal para o jogo.
 * 
 * São disponibilizadas todas as funcionalidades a partir desta interface e 
 * respetivo conjunto de subinterfaces, Menu Play e Menu Classificações.
 */
public class MainMenu implements UpdateGameListener {
    
    // GUI & Graficos
    public final GraphicsConfiguration GRAPHICS_CONFIG =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    
    protected int width = 780; // 320, 550
    protected int height = 520; // 240, 360
    
    // Main Window e Background
    private final JFrame window;
    private final JLabel background;
    private JLabel titleImage;
    
    // Game Menu Panels
    private JPanel mainMenuPanel;
    private JPanel playGamePanel;
    private JPanel loadClassificationsPanel;
    private JTextArea classifTable;
    
    // Preferências Jogo
    private int difficulty;
    private JTextField playerName;
    private JTextField fileLabel;
    
    // Intâncias de Jogo
    private MapReader mapReader;
    private GameNetwork<String> gameNetwork;
    private ClassificationManager classifManager;
    
    private HauntedHouseGame manualGame;
    private UpdateGameListener stopGameListener;
    
    /**
     * Redimensiona uma imagem para os tamanhos descritos.
     * Retorna uma instância de {@link java.swing.ImageIcon}
     * 
     * @param path caminho para a imagem
     * @param w nova largura da imagem
     * @param h nova altura da imagem
     * @return instância com a imagem redimensionada
     */
    public static ImageIcon getSizedImage(String path, int w, int h) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            return null;
        }
        
        Image dimg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        
        return new ImageIcon(dimg);
    }
    
    /**
     * Construtor para o menu principal
     * 
     * @param name nome do jogo
     */
    public MainMenu(String name) {
        /* Janela Principal */
        window = new JFrame(GRAPHICS_CONFIG);
        window.setLayout(new BorderLayout(0, 0));
        window.setResizable(false);
        window.setTitle(name);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        // Imagem de Fundo
        background = new JLabel();
        background.setIcon(getSizedImage("files/img/background.jpg", width, height));
        background.setLayout(new BorderLayout());
        
        /** Main Menu */
        setupMainMenu();
        background.add(mainMenuPanel, BorderLayout.CENTER);
        
        window.add(background);
        window.pack();
        window.setVisible(true);
        
        /** Componentes do Jogo */
        this.mapReader = new MapReader();
        this.stopGameListener = this;
    }
    
    /** ======================================================================
     * Construção do Painel do Menu Principal.
     * Estrutura:
     *  - Carregar Mapa do Jogo ({@link java.swing.JFileChooser} dialog) (obrigatório)
     *  - Escolher Dificuldade (fácil, médio ou difícil. médio por defeito)
     *  - Play
     *  - Visualizar Tabela de Classificações
     */
    private void setupMainMenu() {
        mainMenuPanel = new JPanel(new GridBagLayout());
        mainMenuPanel.setOpaque(false);
        JButton buttonLoad, buttonPlay, buttonLoadMap;
        JPanel diffPanel; // difficulty buttons
        ButtonGroup diffGroup; // difficulty buttons
        Font font = new Font("Gabriola", Font.BOLD, 22);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Layout Configuration ==
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        // Title Image
        titleImage = new JLabel();
        titleImage.setIcon(getSizedImage("files/img/haunted_house.png", width / 4, height / 4));
        mainMenuPanel.add(titleImage, gbc);
        
        // Layout Configuration ==
        gbc.ipadx = 100;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        // Load Map File Menu
        buttonLoadMap = new JButton("Carregar Mapa");
        buttonLoadMap.setBackground(Color.darkGray);
        buttonLoadMap.setFont(font);
        buttonLoadMap.setForeground(Color.white);
        buttonLoadMap.addActionListener(new LoadMapActionListener());
        mainMenuPanel.add(buttonLoadMap, gbc);
        gbc.gridy = 2;
        fileLabel = new JTextField("Nenhum ficheiro selecionado.");
        fileLabel.setEditable(false);
        mainMenuPanel.add(fileLabel, gbc);
        
        // Layout Configuration ==
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 100;
        gbc.ipady = 5;
        gbc.gridwidth = 3;
        gbc.gridy = 3;
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
        mainMenuPanel.add(diffPanel, gbc);
        
        // Layout Configuration ==
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 200;
        gbc.ipady = 20;
        // Play 
        setupPlayMenu();
        gbc.gridy = 4;
        buttonPlay = new JButton("Play");
        buttonPlay.setBackground(Color.darkGray);
        buttonPlay.setFont(font);
        buttonPlay.setForeground(Color.white);
        buttonPlay.addActionListener(new PlayActionListener());
        mainMenuPanel.add(buttonPlay, gbc);
        // Load
        setupLoadClassificationsMenu();
        gbc.gridy = 5;
        buttonLoad = new JButton("Classificações");
        buttonLoad.setBackground(Color.darkGray);
        buttonLoad.setFont(font);
        buttonLoad.setForeground(Color.white);
        buttonLoad.addActionListener(new LoadClassificationsActionListener());
        mainMenuPanel.add(buttonLoad, gbc);
    }
    
    /** ======================================================================
     * Construção do painel do menu Play.
     *  - Player Name (obrigatório)
     *  - Jogo Manual
     *  - Simulação
     *  - Voltar ao Menu Principal
     */
    private void setupPlayMenu() {
        playGamePanel = new JPanel(new GridBagLayout());
        playGamePanel.setOpaque(false);
        JLabel nameLabel; // player name
        Font font = new Font("Gabriola", Font.BOLD, 22);
        JButton backButton, startButton, simulateButton; // game buttons
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Layout Configuration ==
        gbc.ipadx = 50;
        gbc.ipady = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Player Name
        nameLabel = new JLabel("Player Name: ");
        nameLabel.setFont(font);
        nameLabel.setForeground(Color.white);
        playGamePanel.add(nameLabel, gbc);
        gbc.gridy = 1;
        playerName = new JTextField();
        playerName.requestFocusInWindow();
        playGamePanel.add(playerName, gbc);
        
        // Layout Configuration ==
        gbc.insets = new Insets(10, 100, 10, 100);
        gbc.ipadx = 300;
        gbc.ipady = 50;
        gbc.gridwidth = 3;
        // Simulation Button
        gbc.gridy = 2;
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
        
        // Layout Configuration ==
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
    
    /** ======================================================================
     * Construção do painel do menu LoadClassifications.
     *  - Tabela de Classificações ({@link java.swing.JTextArea})
     *  - Voltar ao Menu Principal
     */
    private void setupLoadClassificationsMenu() {
        loadClassificationsPanel = new JPanel(new GridBagLayout());
        loadClassificationsPanel.setOpaque(false);
        JScrollPane scrollTable;
        JButton backButton;
        Font font = new Font("Gabriola", Font.BOLD, 22);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Layout Configuration ==
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 300;
        gbc.ipady = 100;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        classifTable = new JTextArea();
        classifTable.setEditable(false);
        scrollTable = new JScrollPane(classifTable);
        loadClassificationsPanel.add(scrollTable, gbc);
        
        // Layout Configuration ==
        gbc.ipadx = 150;
        gbc.ipady = 25;
        gbc.gridy = 2;
        // MainMenu Back Button
        backButton = new JButton("Back to Main Menu");
        backButton.setBackground(Color.darkGray);
        backButton.setFont(font);
        backButton.setForeground(Color.white);
        backButton.addActionListener(new BackButtonListener());
        loadClassificationsPanel.add(backButton, gbc);
    }
    
    /**
     * Action Listener relativo ao Dialog para escolha do ficheiro do mapa
     */
    private class LoadMapActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            JFileChooser chooser = new JFileChooser("./files/map1.json");
            chooser.setDialogTitle("Selecionar um ficheiro .json"); 

            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter restrict = 
                    new FileNameExtensionFilter("Somente ficheiros .json", "json"); 
            chooser.addChoosableFileFilter(restrict); 
  
            int result = chooser.showSaveDialog(null); 
  
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    if (mapReader.loadMapFromJSON(chooser.getSelectedFile().getPath())) {
                        fileLabel.setText(chooser.getSelectedFile().getPath());
                        // inicializar instância de classificações
                        classifManager = new ClassificationManager(
                                    "files/classifications.json", 
                                    mapReader.getMapModel().getName());
                    } else {
                        fileLabel.setText("Não foi possível inicializar o mapa através do ficheiro.");
                    }
                } catch (Exception exc) {
                    System.err.println(exc);
                    fileLabel.setText("Não foi possível inicializar o mapa através do ficheiro.");
                }
            }
            // ERROR e CANCEL result
        }
    }
    
    /**
     * Item Listener relatovo ao RadioButton para escolha da dificuldade do Jogo
     */
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
    
    /**
     * Action Listener relativo ao botão de volta para o MainMenu
     */
    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            background.removeAll();
            mainMenuPanel.setOpaque(false);
            background.add(mainMenuPanel);
            SwingUtilities.updateComponentTreeUI(window);
        } 
    }
    
    /**
     * Action Listener relativo ao botão de troca para o painel do menu Play
     */
    private class PlayActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mapReader.getMapModel() != null) {
                background.removeAll();
                playGamePanel.setOpaque(false);
                background.add(playGamePanel);
                SwingUtilities.updateComponentTreeUI(window);
            }
        }
    }
    
    /**
     * Action Listener relativo ao botão de troca para o menu LoadClassifications
     */
    private class LoadClassificationsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mapReader.getMapModel() != null) {
                background.removeAll();
                background.add(loadClassificationsPanel);
                classifTable.setText(classifManager.getClassifications(difficulty));
                SwingUtilities.updateComponentTreeUI(window);
            }
        }
    }
    
    /**
     * Action Listener do menu Play para dar início ao jogo manual
     */
    private class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = playerName.getText();
            if (name != null && !name.isEmpty()) {
                
                if (mapReader.getMapModel() != null) {
                    // carregar grafo do mapa
                    gameNetwork = mapReader.loadGameInformation(difficulty, "entrada");
                    System.out.println("Detalhes sobre Grafo do Mapa: \n" 
                            + gameNetwork.toString());
                    // inicializar classe jogo
                    manualGame = new HauntedHouseGame(width, height);
                    manualGame.setupGame(playerName.getText(), gameNetwork);
                    // adicionar o painel do jogo
                    background.removeAll();
                    background.add(manualGame, BorderLayout.CENTER);
                    // iniciar o jogo
                    manualGame.startGame(stopGameListener, classifManager);
                    // atualizar o layout
                    SwingUtilities.updateComponentTreeUI(window);
                    manualGame.requestFocusInWindow();
                    
                }
            } else
                playerName.requestFocusInWindow();
        }  
    }
    
    /**
     * Action Listener do menu Play para dar início à simulação do jogo
     */
    private class SimulateGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mapReader.getMapModel() != null) {
                // carregar grafo do mapa
                gameNetwork = mapReader.loadGameInformation(difficulty, "entrada");
                System.out.println("Detalhes sobre Grafo do Mapa: \n" 
                        + gameNetwork.toString());
                
                // mostrar simulação
                JPanel simulation = new JPanel(new GridBagLayout());
                simulation.setOpaque(false);
                JLabel route;
                JScrollPane scroll;
                JButton backButton;
                Font font = new Font("Gabriola", Font.BOLD, 22);
                GridBagConstraints gbc = new GridBagConstraints();

                // Layout Configuration ==
                gbc.insets = new Insets(10, 0, 10, 0);
                gbc.ipadx = 300;
                gbc.ipady = 100;
                gbc.gridx = 0;
                gbc.gridy = 0;
                route = new JLabel();
                route.setText(getSimulationPath());
                scroll = new JScrollPane(route);
                simulation.add(scroll, gbc);

                // Layout Configuration ==
                gbc.ipadx = 150;
                gbc.ipady = 25;
                gbc.gridy = 2;
                // MainMenu Back Button
                backButton = new JButton("Back to Play Menu");
                backButton.setBackground(Color.darkGray);
                backButton.setFont(font);
                backButton.setForeground(Color.white);
                backButton.addActionListener((ActionEvent ev) -> {
                    background.removeAll();
                    mainMenuPanel.setOpaque(false);
                    background.add(playGamePanel);
                    SwingUtilities.updateComponentTreeUI(window);
                });
                simulation.add(backButton, gbc);
                
                background.removeAll();
                mainMenuPanel.setOpaque(false);
                background.add(simulation);
                SwingUtilities.updateComponentTreeUI(window);
            }
        }
    }
    
    private String getSimulationPath() {
        try {
            String path = "<html><center>";
            boolean finished = false;
            
            Iterator<String> pathIt = gameNetwork.iteratorShortestPath("entrada", "exterior");
            
            String room = pathIt.next();
            gameNetwork.setNewPosition(room);
            path = path + "Arrived at Entrance Room! Current HP: " 
                    + gameNetwork.getCurrentHp() + "<br/><br/>";
            while(!finished && pathIt.hasNext()){
                room = pathIt.next();
                
                if(gameNetwork.isFinished(room)){
                    path = path + "Arrived at Last Room " + room + " with " 
                        + gameNetwork.getCurrentHp() + " HP!<br/>";
                    finished = true;
                } else if (gameNetwork.isMoveValid(room)) {
                    if(!gameNetwork.setNewPosition(room)) {
                        return "you can't win in this difficulty :(";
                    } else {
                        path = path + "Go through " + room + ". Your hp will be "
                                + gameNetwork.getCurrentHp() + "<br/><br/>";
                    }
                } else {
                    return "ERROR - Path to room '" + room + "' is invalid.";
                }
            }
            
            return path + "</center></html>";
            
        } catch (ElementNotFoundException exc) {
            System.err.println(exc);
            return "ERROR - Element in path is invalid (check console for debug)";
        }
    }
    
    @Override
    public void stopGame() {
        // dialog para verificar se o user quer sair
        background.removeAll();
        mainMenuPanel.setOpaque(false);
        background.add(mainMenuPanel);
        SwingUtilities.updateComponentTreeUI(window);
    }
    
    public static void main(String[] args) {
        MainMenu game = new MainMenu("HauntedHouseGame");
    }
}