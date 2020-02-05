package game.hhgame.models;

/**
 * Classe que descreve uma parede no mapa do jogo.
 */
public class RoomWall extends GameElement {

    public RoomWall(int x, int y) {
        super(x, y, "files/img/game/room_wall.png");
    }
}
