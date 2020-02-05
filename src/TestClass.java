import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import controllers.ClassificationManager;
import controllers.GameNetwork;
import controllers.GameSimulation;
import controllers.MapReader;
import models.ClassificationModel;

public class TestClass {

    public static void main(String args[]) throws ElementNotFoundException, EmptyCollectionException {

        MapReader mapReader = new MapReader();

        System.out.println(mapReader.loadMapFromJSON("./files/map1.json"));
        // mapReader.loadRooms(mapReader.getMapModel());
        mapReader.loadGameInformation(1,"entrada");
        System.out.println(mapReader.testOnlyTOBEDELETED());
        mapReader.printDijsktra();

        GameNetwork<String> gameNetwork = mapReader.getGame();
        GameSimulation gameSimulation = new GameSimulation(gameNetwork);
        gameSimulation.simulation();
        System.out.println(gameSimulation.simulationString());

        ClassificationManager classificationManager = new ClassificationManager("./files/classificationsTest","sexta-feira 13");
       // classificationManager.recordToFile(new ClassificationModel("sexta-feira 13","playerRand",16, 10, 3));
        System.out.println(classificationManager.getClassifications(3));

    }

}
