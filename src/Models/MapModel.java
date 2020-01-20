package Models;

import DataStructures.ArrayUnorderedList;

public class MapModel {

    private String name;
    private int points;
    ArrayUnorderedList<RoomModel> roomModels;

    public MapModel(String name, int points, ArrayUnorderedList<RoomModel> roomModels) {
        this.name = name;
        this.points = points;
        this.roomModels = roomModels;
    }


    public String getName() { return name; }

    public int getPoints() { return points; }

    public ArrayUnorderedList getRooms() {return roomModels; }

}
