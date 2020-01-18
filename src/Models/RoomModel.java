package Models;

import DataStructures.ArrayUnorderedList;

public class RoomModel {

    private String roomname; //
    private int phantom; //
    private ArrayUnorderedList<String> rommconections; //


    /**
     * This class represent the Model of a Room. The Room is the equivalent to the a Vertex on the Graph
     *
     * @param roomname Represents the Room Name
     * @param phantom Represents the Weight of the Edge.
     * @param rommconections Connections: Have the names of the rooms to which connections exist
     */
    public RoomModel(String roomname, int phantom, ArrayUnorderedList<String> rommconections) {
        this.roomname = roomname;
        this.phantom = phantom;
        this.rommconections = rommconections;
    }

    public String getRoomname() {
        return roomname;
    }

    public int getPhantom() {
        return phantom;
    }

    public ArrayUnorderedList<String> getRommconections() {
        return rommconections;
    }
}
