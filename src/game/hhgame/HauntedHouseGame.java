package game.hhgame;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import controllers.GameNetwork;
import game.MainMenu;
import game.hhgame.models.GameElement;
import game.hhgame.models.Player;
import game.hhgame.models.RoomDoor;
import game.hhgame.models.RoomWall;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe para o Jogo Manual.
 * 
 * Faz uso de um {@link java.swing.JPanel} para a gameplay.
 * Gameplay baseia-se na utilização do método {@link #repaint()} para cada vez
 * que haja input do utilizador.
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
    private int difficulty;
    
    private String entranceRoom, topWallRoom, bottomWallRoom, leftWallRoom, rightWallRoom;
    
    private boolean isSetup, isCompleted, isDead;
    private StopGameListener stopGameListener;
    
    public HauntedHouseGame(int worldRows, int worldCols, int windowW, int windowH) {
        super.setFocusable(true);
        super.setIcon(MainMenu.getSizedImage("files/img/background.jpg", windowW, windowH));
        
        this.rows = worldRows;
        this.cols = worldCols;
        this.windowW = windowW;
        this.windowH = windowH;
        
        super.addKeyListener(new KeyBoardListener());
        this.level = new char[rows][cols];
        
        isSetup = isCompleted = isDead = false;
    }
    
    public void setupGame(String playerName, int difficulty, GameNetwork gameNetwork) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.gameNetwork = gameNetwork;
        isSetup = true;
    }
    
    public void startGame(StopGameListener listener) {
        if (isSetup) {
            stopGameListener = listener;
            buildRoomWalls();

            try {
                entranceRoom = gameNetwork.getRoomConnections("entrada").first();
            } catch (EmptyCollectionException exc) {}

            gameNetwork.setNewPosition(entranceRoom);
            // SET ENTRANCE ROOM DOORS! diferente do buildRoomDoors()
            // player pode começar no meio

            level[rows / 2][cols / 2] = '@'; // player position

            bottomWallRoom = "entrada";

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
    
    private void buildRoomWalls() {
        
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
    
    private void buildRoomDoors() {
        String current = gameNetwork.getCurrentPosition();
        ArrayUnorderedList<String> roomConn = gameNetwork.getRoomConnections(current);
        System.out.println("Current Rooms: " + roomConn.toString());
        
        // VERIFICAR VARIAVEIS topWallRoom, etc.
        // - adicionar a porta ao lado que não está null
        // - gerar portas aleatorio para os outros 3 sitios, consoante o numero
        // de conexoes
        
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
        
        /*
        int random = (int) Math.ceil(Math.random() * 4);
        // Adicionar porta para a "room" anterior à atual 
        // Colocar o player à frente desta porta
        switch (random) {
            case 1: // top wall
                level[0][cols / 2] = '$';
                level[1][cols / 2] = '@';
                break;
            case 2: // bottom wall
                level[rows - 1][cols / 2] = '$';
                level[rows - 2][cols / 2] = '@';
                break;
            case 3: // left wall
                level[rows / 2][0] = '$';
                level[rows / 2][1] = '@';
                break;
            default: // right wall
                level[rows / 2][cols - 1] = '$';
                level[rows / 2][cols - 2] = '@';
        }
        */
    }
    
    private boolean checkDoorCollision(int type) {
        
        switch (type) {
            
            case LEFT_COLLISION:
                // go through left side door
                for (Iterator<RoomDoor> it = doors.iterator(); it.hasNext(); ) {
                    RoomDoor door = it.next();
                    
                    if (player.isLeftCollision(door)) {
                        
                        if (gameNetwork.isFinished(leftWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            if (!gameNetwork.setNewPosition(leftWallRoom)) {
                                isDead = true;
                                
                            } else {
                                rightWallRoom = gameNetwork.getCurrentPosition();
                                leftWallRoom = topWallRoom = bottomWallRoom = null;
                                buildRoomWalls();
                                buildRoomDoors();
                                prepareGraphicsFromMatrix();
                                repaint();
                            }
                        }
                        return true;
                    }
                }
                break;
                
            case RIGHT_COLLISION:                
                // go through right side door
                for (Iterator<RoomDoor> it = doors.iterator(); it.hasNext(); ) {
                    RoomDoor door = it.next();
                    
                    if (player.isRightCollision(door)) {
                        
                        if (gameNetwork.isFinished(rightWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            if (!gameNetwork.setNewPosition(rightWallRoom)) {
                                isDead = true;
                                
                            } else {
                                leftWallRoom = gameNetwork.getCurrentPosition();
                                gameNetwork.setNewPosition(rightWallRoom);
                                rightWallRoom = topWallRoom = bottomWallRoom = null;
                                buildRoomWalls();
                                buildRoomDoors();
                                prepareGraphicsFromMatrix();
                                repaint();
                            }
                        }
                        return true;
                    }
                }
                break;
                
            case TOP_COLLISION:
                // go through top side door
                for (Iterator<RoomDoor> it = doors.iterator(); it.hasNext(); ) {
                    RoomDoor door = it.next();
                    
                    if (player.isTopCollision(door)) {
                        if (gameNetwork.isFinished(topWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            if (!gameNetwork.setNewPosition(topWallRoom)) {
                                isDead = true;
                                
                            } else {
                                bottomWallRoom = gameNetwork.getCurrentPosition();
                                gameNetwork.setNewPosition(topWallRoom);
                                leftWallRoom = topWallRoom = rightWallRoom = null;
                                buildRoomWalls();
                                buildRoomDoors();
                                prepareGraphicsFromMatrix();
                                repaint();
                            }
                        }
                        return true;
                    }
                }
                break;
                
            case BOTTOM_COLLISION:
                // go through bottom side door
                for (Iterator<RoomDoor> it = doors.iterator(); it.hasNext(); ) {
                    RoomDoor door = it.next();
                    
                    if (player.isBottomCollision(door)) {
                        if (gameNetwork.isFinished(bottomWallRoom)) {
                            isCompleted = true;
                            
                        } else {
                            if (!gameNetwork.setNewPosition(bottomWallRoom)) {
                                isDead = true;
                                
                            } else {
                                topWallRoom = gameNetwork.getCurrentPosition();
                                gameNetwork.setNewPosition(bottomWallRoom);
                                leftWallRoom = bottomWallRoom = rightWallRoom = null;
                                buildRoomWalls();
                                buildRoomDoors();
                                prepareGraphicsFromMatrix();
                                repaint();
                            }
                        }
                        return true;
                        
                    }
                }
                break;
                
            default:
        }
        return false;
    }
    
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
                    stopGameListener.stopGame();
                    break;
                    
                default:
            }
            e.consume();
            repaint();
        }
    
    }

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
    
    private void renderGameGraphics(Graphics g) {
        
        g.setColor(new Color(10, 10, 10));
        g.fillRect(OFFSETX, OFFSETY, cols * SPACE, rows * SPACE);
        
        g.setColor(new Color(255,255,255));
        String current = "Current Room: " + gameNetwork.getCurrentPosition();
        g.drawString(current, 25, 25);
        g.drawString("Top Room: " + (topWallRoom != null ? topWallRoom : "none"), OFFSETX, 25);
        g.drawString("Left Room: " + (leftWallRoom != null ? leftWallRoom : "none"), OFFSETX - 50, 50);
        g.drawString("Right Room: " + (rightWallRoom != null ? rightWallRoom : "none"), OFFSETX + 50, 50);
        g.drawString("Bottom Room: " + (bottomWallRoom != null ? bottomWallRoom : "none"), OFFSETX, 75);
        String health = "Health Points: " + gameNetwork.getCurrentHp();
        g.drawString(health, 25, 50);

        ArrayUnorderedList<GameElement> world = new ArrayUnorderedList<>();

        for (Iterator<RoomWall> it = walls.iterator(); it.hasNext(); ) {
            world.addToRear(it.next());
        }
        for (Iterator<RoomDoor> it = doors.iterator(); it.hasNext(); ) {
            world.addToRear(it.next());
        }
        world.addToRear(player);

        for (Iterator<GameElement> it = world.iterator(); it.hasNext(); ) {
            GameElement item = it.next();

            if (item instanceof Player || item instanceof RoomDoor) {
                g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2, null);
            } else {
                g.drawImage(item.getImage(), item.getX(), item.getY(), null);
            }

            if (isCompleted) {   
                g.setColor(new Color(10, 10, 10));
                g.fillRect(OFFSETX, OFFSETY, cols * SPACE, rows * SPACE);
                g.setColor(new Color(255, 255, 255));
                g.drawString("YOU WIN", (cols * SPACE) + (rows / 2), (rows * SPACE) + (cols / 2));
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderGameGraphics(g);
    }
    
    public interface StopGameListener {
        void stopGame();
    }
    
}
