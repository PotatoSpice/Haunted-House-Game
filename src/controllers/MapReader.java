package controllers;

import com.google.gson.*;
import collections.list.unordered.ArrayUnorderedList;
import interfaces.IMapReader;
import interfaces.NetworkADT;
import models.MapModel;
import models.RoomModel;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class MapReader implements IMapReader {

    MapModel mapModel;

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
        for(int ix = 0; ix< rooms.size(); ix++) {
            JsonObject room = rooms.get(ix).getAsJsonObject();
            JsonArray roomconnections = room.get("ligacoes").getAsJsonArray();
            ArrayUnorderedList<String> connections = new ArrayUnorderedList<>();
            for(int jx = 0; jx < roomconnections.size(); jx++)
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
        return null;
    }

    @Override
    public NetworkADT loadGraphWithRoom(ArrayUnorderedList<RoomModel> roomModels) {
        return null;
    }
}
