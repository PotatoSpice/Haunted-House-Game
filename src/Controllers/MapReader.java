package Controllers;

import DataStructures.ArrayUnorderedList;
import Interfaces.IMapReader;
import Interfaces.NetworkADT;
import Models.MapModel;
import Models.RoomModel;

public class MapReader implements IMapReader {
    @Override
    public boolean loadMapFromJSON(String path) {
        return false;
    }

    @Override
    public ArrayUnorderedList loadRooms(MapModel mapModel) {
        return null;
    }

    @Override
    public NetworkADT loadGraphWithRoom(ArrayUnorderedList<RoomModel> roomModels) {
        return null;
    }
}
