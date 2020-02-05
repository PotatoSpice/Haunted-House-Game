package models;

import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;

public class RoomModel {

    private String roomName; //
    private int phantom; //
    private ArrayUnorderedList<String> roomConnections; //


    /**
     * This class represent the Model of a Room. The Room is the equivalent to the a Vertex on the Graph
     *
     * @param roomname Represents the Room Name
     * @param phantom Represents the Weight of the Edge.
     * @param roomconections Connections: Have the names of the rooms to which connections exist
     */
    public RoomModel(String roomname, int phantom, ArrayUnorderedList<String> roomconections) {
        this.roomName = roomname;
        this.phantom = phantom;
        this.roomConnections = roomconections;
    }

    public String getRoomname() {
        return roomName;
    }

    public int getPhantom() {
        return phantom;
    }

    public ArrayUnorderedList<String> getRoomconections() {
        return roomConnections;
    }
    
    public boolean hasEntrance() {
        return roomConnections.contains("entrada");
    }
    
    public boolean hasExterior() {
        return roomConnections.contains("exterior");
    }
    
    @Override
    public String toString() {
        return "\n\troomname: " + roomName + " | phantom: " + phantom 
                + " | connections: " + roomConnections.toString();
    }
}
