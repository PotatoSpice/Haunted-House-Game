package game.hhgame.models;

/**
 * Classe que descreve a posição do jogador no jogo
 */
public class Player extends GameElement {

    public Player(int x, int y) {
        super(x, y, "files/img/game/player.png");
    }

    /**
     * Move o jogador no mapa para as coordenadas descritas.
     * 
     * @param x coordenadas horizontais
     * @param y coordenadas verticais
     */
    public void move(int x, int y) {
        
        int dx = super.x + x;
        int dy = super.y + y;
        
        super.x = dx;
        super.y = dy;
    }
}
