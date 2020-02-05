package game.hhgame.models;

import game.hhgame.HauntedHouseGame;
import java.awt.Image;
import javax.swing.ImageIcon;

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

    public boolean isLeftCollision(GameElement actor) {
        
        return x - HauntedHouseGame.SPACE == actor.x && y == actor.y;
    }

    public boolean isRightCollision(GameElement actor) {
        
        return x + HauntedHouseGame.SPACE == actor.x && y == actor.y;
    }

    public boolean isTopCollision(GameElement actor) {
        
        return y - HauntedHouseGame.SPACE == actor.y && x == actor.x;
    }

    public boolean isBottomCollision(GameElement actor) {
        
        return y + HauntedHouseGame.SPACE == actor.y && x == actor.x;
    }
    
    @Override
    public String toString() {
        return "xCoord: " + x + "; yCoord: " + y + "; imagePath: " + path;
    }
}
