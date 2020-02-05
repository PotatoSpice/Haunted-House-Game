package game.hhgame;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import controllers.ClassificationManager;
import controllers.GameNetwork;
import game.MainMenu;
import game.hhgame.models.GameElement;
import game.hhgame.models.Player;
import game.hhgame.models.RoomDoor;
import game.hhgame.models.RoomWall;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import models.ClassificationModel;

/**
 * Classe para o Jogo Manual.
 * 
 * Faz uso de um java.swing.JPanel para a gameplay.
 * Gameplay baseia-se na utilização do método {@link #repaint()} para cada vez
 * que haja input do utilizador.
 * 
 * A sua construção foi inspirada pelo jogo 'Sokoban' implementado no
 * repositório - https://github.com/janbodnar/Java-Sokoban-Game.
 */
public class HauntedHouseGame extends JLabel {
    
    public static final int OFFSETY = 100; // espaço entre borda janela e borda do jogo
    public static final int OFFSETX = 200; // espaço entre borda janela e borda do jogo
    public static final int SPACE = 20; // tamanho de cada elemento do jogo
    public static final int LEFT_COLLISION = 1;
    public static final int RIGHT_COLLISION = 2;
    public static final int TOP_COLLISION = 3;
    public static final int BOTTOM_COLLISION = 4;
    
    // Janela e Gráficos Jogo
    private final Font font = new Font("Gabriola", Font.BOLD, 22);
    private final int windowW;
    private final int windowH;

    private final int rows;
    private final int cols;
    // # - parede ; $ - porta ; @ - player ; ' ' - espaço vazio
    private final char[][] level; // modelo da sala a ser desenhada pelo canvas
    
    // Variáveis de Jogo
    private ArrayUnorderedList<RoomWall> walls;
    private ArrayUnorderedList<RoomDoor> doors;
    private Player player;
    
    private GameNetwork<String> gameNetwork;
    private String playerName;
    
    private String entranceRoom, topWallRoom, bottomWallRoom, leftWallRoom, rightWallRoom;
    
    private boolean isSetup, isCompleted, isDead, isGhost;
    private ClassificationManager classifManager;
    private UpdateGameListener updateGameListener;
    
    /**
     * Construtor para a classe de jogo manual da casa assombrada.
     * 
     * @param windowW largura da janela do jogo
     * @param windowH altura da janela do jogo
     */
    public HauntedHouseGame(int windowW, int windowH) {
        super.setFocusable(true);
        super.setIcon(MainMenu.getSizedImage("files/img/background.jpg", windowW, windowH));
        
        this.rows = 15;
        this.cols = 17;
        this.windowW = windowW;
        this.windowH = windowH;
        
        super.addKeyListener(new KeyBoardListener());
        this.level = new char[rows][cols];
    }
    
    /**
     * Método obrigatório para inicio do jogo.
     * Configura o nome do jogador e o grafo relativo ao mapa.
     * 
     * @param playerName nome do jogador
     * @param gameNetwork grafo relativo ao mapa do jogo
     */
    public void setupGame(String playerName, GameNetwork gameNetwork) {
        this.playerName = playerName;
        this.gameNetwork = gameNetwork;
        isSetup = true;
        isCompleted = isDead = isGhost = false;
    }
    
    /**
     * Inicializa a intância do jogo.
     * 
     * O final do jogo é sinalizado através do 'listener' enviado por parâmetro,
     * que deverá ser implementado pela classe que chame este método.
     * 
     * @param listener {@link game.hhgame.HauntedHouseGame.UpdateGameListener}
     *                  com os métodos a serem implementados
     * @param classif intância de classificações relativa a este jogo
     */
    public void startGame(UpdateGameListener listener, ClassificationManager classif) {
        if (isSetup) {
            updateGameListener = listener;
            classifManager = classif;
            
            clearBuildRoomWalls();

            try {
                entranceRoom = gameNetwork.getRoomConnections("entrada").first();
            } catch (EmptyCollectionException exc) {}

            gameNetwork.setNewPosition(entranceRoom);

            level[rows / 2][cols / 2] = '@'; // player position

            // Adicionar portas para as outras conexões
            Iterator<String> it = gameNetwork.getRoomConnections(entranceRoom).iterator();
            String room;
            while (it.hasNext()) {
                room = it.next();
                if (topWallRoom == null) {
                    level[0][cols / 2] = '$';
                    topWallRoom = room;

                } else if (rightWallRoom == null) {
                    level[rows / 2][cols - 1] = '$';
                    rightWallRoom = room;

                } else if (leftWallRoom == null) {
                    level[rows / 2][0] = '$';
                    leftWallRoom = room;

                }
            }

            prepareGraphicsFromMatrix();
        }
    }
    
    /**
     * Método de construção da matriz que representa o mapa do jogo.
     * 
     * Controi todas as paredes da sala, tal como "limpa" todo o espaço interior.
     */
    private void clearBuildRoomWalls() {
        // Construir Paredes
        for (int i = 0; i < rows; i++) {
            if (i == 0) {
                for (int j = 0; j < cols; j++)
                    level[0][j] = '#';
                
            } else if (i == rows - 1) {
                for (int j = 0; j < cols; j++)
                    level[rows - 1][j] = '#';
                
            } else {
                level[i][0] = '#';
                    for (int j = 1; j < cols - 1; j++)
                        level[i][j] = ' ';
                    level[i][cols - 1] = '#'; 
            }
        }
    }
    
    /**
     * Método de construção da matriz que representa o mapa do jogo.
     * 
     * Controi todas as portas relativas ao aposento atual.<br/>
     * Adiciona portas consoante a posição inicial do player, relativamente ao 
     * aposento anterior. Isto é, se o player no aposento anterior entrar numa 
     * porta do lado direito, a sua posição seguinte será em frente à porta do 
     * lado esquerdo do novo aposento.
     */
    private void buildRoomDoors() {
        String current = gameNetwork.getCurrentPosition();
        ArrayUnorderedList<String> roomConn = gameNetwork.getRoomConnections(current);
        
        if (topWallRoom != null) {
            level[0][cols / 2] = '$';
            level[1][cols / 2] = '@';
            try {
                roomConn.remove(topWallRoom);
            } catch (ElementNotFoundException | EmptyCollectionException exc) {}
            
            Iterator<String> it = roomConn.iterator();
            String room;
            // Adicionar portas para as outras conexões
            while (it.hasNext()) {
                room = it.next();
                if (bottomWallRoom == null) {
                    level[rows - 1][cols / 2] = '$';
                    bottomWallRoom = room;
                    
                } else if (leftWallRoom == null) {
                    level[rows / 2][0] = '$';
                    leftWallRoom = room;

                } else if (rightWallRoom == null) {
                    level[rows / 2][cols - 1] = '$';
                    rightWallRoom = room;
                    
                }
            }
            
        } else if (bottomWallRoom != null) {
            level[rows - 1][cols / 2] = '$';
            level[rows - 2][cols / 2] = '@';
            try {
                roomConn.remove(bottomWallRoom);
            } catch (ElementNotFoundException | EmptyCollectionException exc) {}
            
            Iterator<String> it = roomConn.iterator();
            String room;
            // Adicionar portas para as outras conexões
            while (it.hasNext()) {
                room = it.next();
                if (topWallRoom == null) {
                    level[0][cols / 2] = '$';
                    topWallRoom = room;

                } else if (leftWallRoom == null) {
                    level[rows / 2][0] = '$';
                    leftWallRoom = room;

                } else if (rightWallRoom == null) {
                    level[rows / 2][cols - 1] = '$';
                    rightWallRoom = room;

                }
            }
            
        } else if (leftWallRoom != null) {
            level[rows / 2][0] = '$';
            level[rows / 2][1] = '@';
            try {
                roomConn.remove(leftWallRoom);
            } catch (ElementNotFoundException | EmptyCollectionException exc) {}
            
            Iterator<String> it = roomConn.iterator();
            String room;
            // Adicionar portas para as outras conexões
            while (it.hasNext()) {
                room = it.next();
                if (rightWallRoom == null) {
                    level[rows / 2][cols - 1] = '$';
                    rightWallRoom = room;

                } else if (topWallRoom == null) {
                    level[0][cols / 2] = '$';
                    topWallRoom = room;
                    
                } else if (bottomWallRoom == null) {
                    level[rows - 1][cols / 2] = '$';
                    bottomWallRoom = room;
                    
                }
            }
            
        } else {
            level[rows / 2][cols - 1] = '$';
            level[rows / 2][cols - 2] = '@';
            try {
                roomConn.remove(rightWallRoom);
            } catch (ElementNotFoundException | EmptyCollectionException exc) {}
            
            Iterator<String> it = roomConn.iterator();
            String room;
            // Adicionar portas para as outras conexões
            while (it.hasNext()) {
                room = it.next();
                if (leftWallRoom == null) {
                    level[rows / 2][0] = '$';
                    leftWallRoom = room;

                } else if (topWallRoom == null) {
                    level[0][cols / 2] = '$';
                    topWallRoom = room;

                } else if (bottomWallRoom == null) {
                    level[rows - 1][cols / 2] = '$';
                    bottomWallRoom = room;
                    
                }
            }
        }
    }
    
    /**
     * Verifica se o 'player' colidiu com uma porta, atravessando para o aposento
     * respetivo.
     * 
     * É aqui verificado se o jogo foi concluído, chegando ao exterior ou levando
     * o HP a 0.
     * 
     * @param type lado da colisão {@link #BOTTOM_COLLISION}, {@link #TOP_COLLISION}, 
     *              {@link #LEFT_COLLISION} ou {@link #RIGHT_COLLISION}
     * @return true se houve colisão, false no contrário
     */
    private boolean checkDoorCollision(int type) {
        boolean collision = false;
        Iterator<RoomDoor> it = doors.iterator();
        
        switch (type) {    
            case LEFT_COLLISION:
                while (it.hasNext() && collision == false) {
                    RoomDoor door = it.next();
                    
                    // verificar colisão com a porta esquerda
                    if (player.isLeftCollision(door)) {
                        // verificar se o player chegou ao exterior
                        if (gameNetwork.isFinished(leftWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            // guardar temporariamente o aposento anterior
                            String temp = gameNetwork.getCurrentPosition();
                            // verificar HP do player
                            if (!gameNetwork.setNewPosition(leftWallRoom)) {
                                isDead = true;
                                
                            } else {
                                // avançar para o próximo aposento
                                int hp = gameNetwork.getCurrentHp();
                                rightWallRoom = temp;
                                leftWallRoom = topWallRoom = bottomWallRoom = null;
                                
                                isGhost = hp < gameNetwork.getCurrentHp();
                            }
                        }
                        collision = true;
                    }
                }
                break;
                
            case RIGHT_COLLISION:
                while (it.hasNext() && collision == false) {
                    RoomDoor door = it.next();
                    // verificar colisão com a porta direita
                    if (player.isRightCollision(door)) {
                        // verificar se o player chegou ao exterior
                        if (gameNetwork.isFinished(rightWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            // guardar temporariamente o aposento anterior
                            String temp = gameNetwork.getCurrentPosition();
                            // verificar HP do player
                            if (!gameNetwork.setNewPosition(rightWallRoom)) {
                                isDead = true;
                                
                            } else {
                                // avançar para o próximo aposento
                                int hp = gameNetwork.getCurrentHp();
                                leftWallRoom = temp;
                                rightWallRoom = topWallRoom = bottomWallRoom = null;
                                
                                isGhost = hp < gameNetwork.getCurrentHp();
                            }
                        }
                        
                        collision = true;
                    }
                }
                break;
                
            case TOP_COLLISION:
                while (it.hasNext() && collision == false) {
                    RoomDoor door = it.next();
                    // verificar colisão com a porta de cima
                    if (player.isTopCollision(door)) {
                        // verificar se o player chegou ao exterior
                        if (gameNetwork.isFinished(topWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            // guardar temporariamente o aposento anterior
                            String temp = gameNetwork.getCurrentPosition();
                            // verificar HP do player
                            if (!gameNetwork.setNewPosition(topWallRoom)) {
                                isDead = true;
                                
                            } else {
                                // avançar para o próximo aposento
                                int hp = gameNetwork.getCurrentHp();
                                bottomWallRoom = temp;
                                leftWallRoom = topWallRoom = rightWallRoom = null;
                                
                                isGhost = hp < gameNetwork.getCurrentHp();
                            }
                        }
                        
                        collision = true;
                    }
                }
                break;
                
            case BOTTOM_COLLISION:
                while (it.hasNext() && collision == false) {
                    RoomDoor door = it.next();
                    // verificar colisão com a porta de baixo
                    if (player.isBottomCollision(door)) {
                        // verificar se o player chegou ao exterior
                        if (gameNetwork.isFinished(bottomWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            // guardar temporariamente o aposento anterior
                            String temp = gameNetwork.getCurrentPosition();
                            // verificar HP do player
                            if (!gameNetwork.setNewPosition(bottomWallRoom)) {
                                isDead = true;
                                
                            } else {
                                // avançar para o próximo aposento
                                int hp = gameNetwork.getCurrentHp();
                                leftWallRoom = bottomWallRoom = rightWallRoom = null;
                                topWallRoom = temp;
                                isGhost = hp < gameNetwork.getCurrentHp();
                            }
                        }
                        
                        collision = true;
                    }
                }
                break;
                
            default:
        }
        
        if (collision) {
            // jogo acabado, escrever classificações
            if (isCompleted) {
                classifManager.recordToFile(
                        new ClassificationModel(gameNetwork.getMapName(), playerName, 
                                gameNetwork.getCurrentHp(), gameNetwork.getMoveCount(), 
                                gameNetwork.getDifficulty()));
                
            } else 
                // construir próximo aposento se o player ainda não morreu
                if(!isDead) {
                clearBuildRoomWalls();
                buildRoomDoors();
                prepareGraphicsFromMatrix();
                
            }
            repaint();
        }
        
        return collision;
    }
    
    /**
     * Verifica se o 'player' colidiu com uma parede, não o deixando atravessar.
     * 
     * @param type lado da colisão {@link #BOTTOM_COLLISION}, {@link #TOP_COLLISION}, 
     *              {@link #LEFT_COLLISION} ou {@link #RIGHT_COLLISION}
     * @return true se houve colisão, false no contrário
     */
    private boolean checkWallCollision(int type) {

        switch (type) {
            
            case LEFT_COLLISION:
                for (Iterator<RoomWall> it = walls.iterator(); it.hasNext(); ) {           
                    RoomWall wall = it.next();
                    
                    if (player.isLeftCollision(wall)) {
                        return true;
                    }
                }
                return false;
                
            case RIGHT_COLLISION:
                for (Iterator<RoomWall> it = walls.iterator(); it.hasNext(); ) {           
                    RoomWall wall = it.next();
                    
                    if (player.isRightCollision(wall)) {
                        return true;
                    }
                }
                return false;
                
            case TOP_COLLISION:
                for (Iterator<RoomWall> it = walls.iterator(); it.hasNext(); ) {           
                    RoomWall wall = it.next();
                    
                    if (player.isTopCollision(wall)) {       
                        return true;
                    }
                }
                return false;
                
            case BOTTOM_COLLISION:       
                for (Iterator<RoomWall> it = walls.iterator(); it.hasNext(); ) {           
                    RoomWall wall = it.next();
                    
                    if (player.isBottomCollision(wall)) {               
                        return true;
                    }
                }       
                return false;
                
            default:
                break;
        }
        return false;
    }
    
    /**
     * 'Listener' para os cliques do teclado.
     * 
     * É verificado para cada clique nas setas de direção a colisão com uma parede
     * ou porta, sendo realizado o respetivo movimento consoante.
     */
    private class KeyBoardListener extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {
            // 0 - nada
            // 1 - movimento normal
            // 2 - parede
            // 3 - porta
            switch (e.getKeyCode()) {
                
                case KeyEvent.VK_LEFT:
                    if (checkWallCollision(LEFT_COLLISION))
                        return;

                    if (checkDoorCollision(LEFT_COLLISION))
                        return;

                    player.move(-SPACE, 0);
                    break;
                    
                case KeyEvent.VK_RIGHT:
                    if (checkWallCollision(RIGHT_COLLISION))
                        return;

                    if (checkDoorCollision(RIGHT_COLLISION))
                        return;

                    player.move(SPACE, 0);
                    break;
                    
                case KeyEvent.VK_UP:
                    if (checkWallCollision(TOP_COLLISION))
                        return;

                    if (checkDoorCollision(TOP_COLLISION))
                        return;

                    player.move(0, -SPACE);
                    break;
                    
                case KeyEvent.VK_DOWN:
                    if (checkWallCollision(BOTTOM_COLLISION))
                        return;

                    if (checkDoorCollision(BOTTOM_COLLISION))
                        return;

                    player.move(0, SPACE);
                    break;
                    
                case KeyEvent.VK_Q:
                    isSetup = false;
                    updateGameListener.stopGame();
                    break;
                    
                default:
            }
            e.consume();
            repaint();
        }
    
    }

    /**
     * Preparação das variáveis para construção dos gráficos do jogo a partir
     * da matriz construída a cada movimento do jogador.
     * 
     * Legenda caracteres:<br/>
     * # - parede;
     * $ - porta;
     * @ - posição do player;
     * ' ' - espaço vazio;
     */
    private void prepareGraphicsFromMatrix() {
        
        walls = new ArrayUnorderedList<>();
        doors = new ArrayUnorderedList<>();

        int x = OFFSETX;
        int y = OFFSETY;

        RoomWall wall;
        RoomDoor door;
        
        for (int r = 0; r < rows; r++) {
            // verificar elementos por cada linha do mapa
            for (int c = 0; c < cols; c++) {
                switch (level[r][c]) {
                    case '#':
                        wall = new RoomWall(x, y);
                        walls.addToRear(wall);
                        x += SPACE;
                        break;

                    case '$':
                        door = new RoomDoor(x, y);
                        doors.addToRear(door);
                        x += SPACE;
                        break;

                    case '@':
                        player = new Player(x, y);
                        x += SPACE;
                        break;

                    case ' ':
                        x += SPACE;
                        break;

                    default:
                }
            }
            // nova linha do mapa
            y += SPACE;
            x = OFFSETX;
        }
    }
    
    /**
     * Contrução dos gráficos para a imagem de apresentação do jogo.
     * 
     * Aqui é desenhado o mapa da sala, tal como algumas informações relevantes
     * em formato textual.
     * 
     * @param g instância de gráficos da imagem do jogo
     */
    private void renderGameGraphics(Graphics g) {
        g.setFont(font);
        if (isCompleted) {   
            g.setColor(new Color(10, 10, 10));
            g.fillRect(OFFSETX, OFFSETY, cols * SPACE, rows * SPACE);
            g.drawImage(MainMenu.getSizedImage("files/img/win.jpg", 
                    cols * SPACE / 2, rows * SPACE / 2).getImage(), 
                    OFFSETX + 5, OFFSETY + 5, null);
            g.setColor(new Color(255, 255, 255));
            g.drawString("YOU WIN", 
                    (cols * SPACE) + (rows / 2), (rows * SPACE) + (cols / 2));
            g.drawString("Press Q to exit!", 
                    (cols * SPACE) + 20, (rows * SPACE) + (cols / 2) + 20);
            
        } else if (isDead) {   
            g.setColor(new Color(255, 255, 255));
            g.fillRect(OFFSETX, OFFSETY, cols * SPACE, rows * SPACE);
            g.drawImage(MainMenu.getSizedImage("files/img/dead.jpg", 
                    cols * SPACE, rows * SPACE).getImage(), 
                    OFFSETX, OFFSETY, null);
            g.setColor(new Color(10, 10, 10));
            g.drawString("YOU DEAD", 
                    OFFSETX + 20, OFFSETY + 20);
            g.drawString("Press Q to exit!", 
                    OFFSETX - 100, OFFSETY + 40);
            
        } else {
            // Room Floor
            if (isGhost) {
                g.setColor(new Color(240, 240, 240));
                g.fillRect(OFFSETX, OFFSETY, cols * SPACE, rows * SPACE);
                g.drawImage(MainMenu.getSizedImage("files/img/ghost.jpg", cols * SPACE, rows * SPACE)
                        .getImage(), OFFSETX, OFFSETY, null);
            } else {
                g.setColor(new Color(10, 10, 10));
                g.fillRect(OFFSETX, OFFSETY, cols * SPACE, rows * SPACE);
            }
            
            
            g.setColor(new Color(255,255,255));
            // Help
            String help = "Click Q to Quit Game (Warning! Game doesn't save!)";
            g.drawString(help, 25, 25);
            
            // Current Room
            String current = "Current Room: " + gameNetwork.getCurrentPosition();
            g.drawString(current, 25, 50);
            
            // Door Rooms
            if (gameNetwork.getDifficulty() != 3) {
                int w = (OFFSETX + ((cols * SPACE) / 2)) - 75, 
                        y = (OFFSETY + ((rows * SPACE) / 2)) - 75;
                g.drawString("Top Room: " + (topWallRoom != null ? topWallRoom : "none"), 
                        w, 25);
                g.drawString("Left Room: " + (leftWallRoom != null ? leftWallRoom : "none"), 
                        w - 100, 50);
                g.drawString("Right Room: " + (rightWallRoom != null ? rightWallRoom : "none"), 
                        w + 100, 50);
                g.drawString("Bottom Room: " + (bottomWallRoom != null ? bottomWallRoom : "none"), 
                        w, 75);
            }
            
            // Current Health
            String health = "Health Points: " + gameNetwork.getCurrentHp();
            g.drawString(health, 25, 75);

            GameElement item;
            // Desenhar todas as paredes
            for (Iterator<RoomWall> it = walls.iterator(); it.hasNext(); ) {
                item = it.next();
                
                g.drawImage(item.getImage(), item.getX(), item.getY(), null);
            }
            // Desenhar todas a portas
            for (Iterator<RoomDoor> it = doors.iterator(); it.hasNext(); ) {
                item = it.next();
                
                g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2, null);
            }
            // Desenhar o player
            item = player;
            g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2, null);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderGameGraphics(g);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    /**
     * Interface que deve ser implementada pela classe que inicialize uma intância
     * do jogo descrito por esta classe, para permitir o fecho do jogo.
     */
    public interface UpdateGameListener {
        void stopGame();
    }
    
}
