package models;

import collections.list.unordered.ArrayUnorderedList;

public class RoomModel {

    private String roomname; //
    private int phantom; //
    private ArrayUnorderedList<String> roomconections; //


    /**
     * This class represent the Model of a Room. The Room is the equivalent to the a Vertex on the Graph
     *
     * @param roomname Represents the Room Name
     * @param phantom Represents the Weight of the Edge.
     * @param roomconections Connections: Have the names of the rooms to which connections exist
     */
    public RoomModel(String roomname, int phantom, ArrayUnorderedList<String> roomconections) {
        this.roomname = roomname;
        this.phantom = phantom;
        this.roomconections = roomconections;
    }

    public String getRoomname() {
        return roomname;
    }

    public int getPhantom() {
        return phantom;
    }

    public ArrayUnorderedList<String> getRoomconections() {
        return roomconections;
    }
}
