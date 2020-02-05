package game.hhgame.models;

import game.hhgame.HauntedHouseGame;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Classe que descreve um elemento do jogo.
 * 
 * Disponibiliza métodos para a deteção de colisões com o objeto.
 */
public class GameElement {

    protected int x;
    protected int y;
    protected Image image;
    private String path;

    public GameElement(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(path).getImage();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    /**
     * Deteta a colisão deste objeto com o lado esquerdo do objeto em parâmetro.
     * 
     * @param actor objeto de colisão
     * @return true se existe colisão, false no contrário
     */
    public boolean isLeftCollision(GameElement actor) {
        
        return x - HauntedHouseGame.SPACE == actor.x && y == actor.y;
    }

    /**
     * Deteta a colisão deste objeto com o lado direito do objeto em parâmetro.
     * 
     * @param actor objeto de colisão
     * @return true se existe colisão, false no contrário
     */
    public boolean isRightCollision(GameElement actor) {
        
        return x + HauntedHouseGame.SPACE == actor.x && y == actor.y;
    }

    /**
     * Deteta a colisão deste objeto com o lado superior do objeto em parâmetro.
     * 
     * @param actor objeto de colisão
     * @return true se existe colisão, false no contrário
     */
    public boolean isTopCollision(GameElement actor) {
        
        return y - HauntedHouseGame.SPACE == actor.y && x == actor.x;
    }

    /**
     * Deteta a colisão deste objeto com o lado inferior do objeto em parâmetro.
     * 
     * @param actor objeto de colisão
     * @return true se existe colisão, false no contrário
     */
    public boolean isBottomCollision(GameElement actor) {
        
        return y + HauntedHouseGame.SPACE == actor.y && x == actor.x;
    }
    
    @Override
    public String toString() {
        return "xCoord: " + x + "; yCoord: " + y + "; imagePath: " + path;
    }
}
