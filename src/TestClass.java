import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import controllers.MapReader;

public class TestClass {

    public static void main(String args[]) throws ElementNotFoundException, EmptyCollectionException {

        MapReader mapReader = new MapReader();

        System.out.println(mapReader.loadMapFromJSON("./files/map1.json"));
        mapReader.loadRooms(mapReader.getMapModel());
        mapReader.loadGraphWithRoom(mapReader.getMapModel().getRooms());
        System.out.println(mapReader.testOnlyTOBEDELETED());
        mapReader.printDijsktra();

    }

}
