package Interfaces;

import DataStructures.ArrayUnorderedList;
import Models.MapModel;
import Models.RoomModel;

public interface IMapReader {

    /**
     * This method shall load the JSON information and parse it into the MapModel in order to load the game information and Rooms to be loaded into the Graph
     *
     * @param path variable which shall contain the path to the specified *.json file
     * @return true if it is possible to load the Map, false if otherwise
     */
    boolean loadMapFromJSON(String path);

    /**
     * This method shall load from the MapModel instance the Rooms and their information into an UnorderedList containing instances of the RoomModel to be treated
     *
     * @param mapModel Loaded map instance in which the rooms shall be "extracted"
     * @return The List containing the Rooms and their information
     */
    ArrayUnorderedList loadRooms (MapModel mapModel);

    /**
     * This method shall return a Network Oriented Graph containing a modeled version of the Map.
     * Each node/vertex symbolizes a room. Each edge contains the "phantom's" information, 0 if there is none, or any positive integer if it exists (weight of the network)
     *
     * @param roomModels The List Containing the RoomModels
     * @return the Network Oriented Graph loaded with Rooms and their weights
     */
    NetworkADT loadGraphWithRoom(ArrayUnorderedList<RoomModel> roomModels);



}
