package game.hhgame.models;

public class Player extends GameElement {

    public Player(int x, int y) {
        super(x, y, "files/img/game/player.png");
    }

    public void move(int x, int y) {
        
        int dx = super.x + x;
        int dy = super.y + y;
        
        super.x = dx;
        super.y = dy;
    }
}
