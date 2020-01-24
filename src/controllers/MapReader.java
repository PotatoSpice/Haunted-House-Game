package controllers;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import com.google.gson.*;
import collections.list.unordered.ArrayUnorderedList;
import interfaces.IMapReader;
import interfaces.NetworkADT;
import models.MapModel;
import models.RoomModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

public class MapReader implements IMapReader {

    MapModel mapModel;
    GameNetwork<String> mapNetwork = new GameNetwork<>();
    ArrayUnorderedList<RoomModel> rooms = new ArrayUnorderedList<>();

    public MapModel getMapModel() {
        return this.mapModel;
    }

    @Override
    public boolean loadMapFromJSON(String path) {

        JsonObject jsonObject;
        FileReader file;
        try {
            file = new FileReader(path);
            JsonParser parser = new JsonParser();
            jsonObject = parser.parse(file).getAsJsonObject();
        } catch (FileNotFoundException ex) { // Retorna falso se o ficheiro não existir
            System.err.println("ERROR:\n"
                    + "File not found. path: [" + path + "]");
            return false;
        }
        if (jsonObject.size() == 0)
            return false;

        JsonObject map = jsonObject;
        JsonArray rooms = map.get("mapa").getAsJsonArray();
        ArrayUnorderedList<RoomModel> roomModels = new ArrayUnorderedList<>();
        for (int ix = 0; ix < rooms.size(); ix++) {
            JsonObject room = rooms.get(ix).getAsJsonObject();
            JsonArray roomconnections = room.get("ligacoes").getAsJsonArray();
            ArrayUnorderedList<String> connections = new ArrayUnorderedList<>();
            for (int jx = 0; jx < roomconnections.size(); jx++)
                connections.addToRear(roomconnections.get(jx).getAsString());
            RoomModel model = new RoomModel(room.get("aposento").getAsString(), room.get("fantasma").getAsInt(), connections);
            roomModels.addToRear(model);
        }

        mapModel = new MapModel(map.get("nome").getAsString(), map.get("pontos").getAsInt(), roomModels);
        return true;
    }

    //Apercebi-me que isto pode ter sido um erro no meu raciocínio e poderá sofrer alterações
    @Override
    public ArrayUnorderedList loadRooms(MapModel mapModel) {
        rooms = mapModel.getRooms();
        //System.out.println("Load test 1:" +mapModel.getRooms());
        return rooms;
    }

    public GameNetwork loadGameInformation(int difficulty, String initialPosition){
        mapNetwork.setHP(mapModel.getPoints());
        mapNetwork.setMapName(mapModel.getName());
        mapNetwork.setDifficulty(difficulty);
        mapNetwork.setInitialPosition(initialPosition);
        return  mapNetwork;
    }

    @Override
    public DirectedNetwork<String> loadGraphWithRoom(ArrayUnorderedList<RoomModel> roomModels) {
        ArrayUnorderedList<RoomModel> tempRoomModel = new ArrayUnorderedList<>();
        Iterator iteratingRoom = rooms.iterator();
        mapNetwork.addVertex("entrada");
        while (iteratingRoom.hasNext()) {
            RoomModel room = (RoomModel) iteratingRoom.next();
            mapNetwork.addVertex(room.getRoomname());
            tempRoomModel.addToRear(room);
        }
        mapNetwork.addVertex("exterior");

        Iterator tempIteratingRoom = tempRoomModel.iterator();
        while (tempIteratingRoom.hasNext()) {
            RoomModel toCompareModel = (RoomModel) tempIteratingRoom.next();
            if (mapNetwork.checkVertexExistence(toCompareModel.getRoomname())) {
                ArrayUnorderedList<String> connections = toCompareModel.getRoomconections();
                Iterator connectionIterator = connections.iterator();
                while (connectionIterator.hasNext()) {
                    String roomName = (String) connectionIterator.next();
                    if (mapNetwork.checkVertexExistence(roomName) && !roomName.equals("entrada") && !roomName.equals("exterior")) {
                        mapNetwork.addEdge(toCompareModel.getRoomname(), roomName, toCompareModel.getPhantom());
                    } else if (roomName.equals("entrada")) {
                        mapNetwork.addEdge(toCompareModel.getRoomname(), roomName, toCompareModel.getPhantom());
                    } else if (roomName.equals("exterior")) {
                        mapNetwork.addEdge(toCompareModel.getRoomname(), roomName);
                    }
                }
            }

        }
        return mapNetwork;
    }

    public String testOnlyTOBEDELETED() {
        return mapNetwork.testOnlyTOBEDELETED();
    }

    /**
     * TEST METHOD ONLY
     */
    public void printDijsktra() throws ElementNotFoundException, EmptyCollectionException {
        Iterator iterator = mapNetwork.dijkstraAlgorithm(0, mapNetwork.numVertices - 1);
        System.out.println("Entrou " + iterator.hasNext());
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }


    //TEST METHOD:
    public GameNetwork<String> getGame(){
        return mapNetwork;
    }
}
