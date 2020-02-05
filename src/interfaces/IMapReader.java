package interfaces;

import collections.list.unordered.ArrayUnorderedList;
import controllers.GameNetwork;
import models.MapModel;
import models.RoomModel;

public interface IMapReader {

    /**
     * This method shall load the JSON information and parse it into the MapModel 
     * in order to load the game information and Rooms to be loaded into the Graph
     *
     * @param path variable which shall contain the path to the specified *.json file
     * @return true if it is possible to load the Map, false if otherwise
     */
    boolean loadMapFromJSON(String path);

    /**
     * This method shall return a Network Oriented Graph containing a modeled 
     * version of the Map.
     * Each node/vertex symbolizes a room. Each edge contains the "phantom's" 
     * information, 0 if there is none, or any positive integer if it exists 
     * (weight of the network)
     *
     * @param roomModels The List Containing the RoomModels
     * @return the Network Oriented Graph loaded with Rooms and their weights
     */
    GameNetwork<String> loadGraphWithRoom(ArrayUnorderedList<RoomModel> roomModels);

}
