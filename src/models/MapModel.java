package models;

import collections.list.unordered.ArrayUnorderedList;

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

    public ArrayUnorderedList<RoomModel> getRooms() { return roomModels; }
    
    public RoomModel getEntranceRoom() {
        java.util.Iterator<RoomModel> it = roomModels.iterator();
        RoomModel room;
        while (it.hasNext()) {
            if ((room = it.next()).hasEntrance())
                return room;
        }
        return null;
    }

    @Override
    public String toString() {
        return "mapname: " + name + " | healthpoints: " + points 
                + " | rooms: " + roomModels.toString();
    }
}
