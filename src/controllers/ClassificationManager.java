package controllers;

import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import collections.queue.ArrayQueue;
import com.google.gson.*;
import models.ClassificationModel;

import java.io.*;
import java.util.Iterator;

/**
 * Classe responsavél pela gestão, registos e apresentações de resultados do jogo
 */
public class ClassificationManager {

    // ClassificationModel classificationModel;
    String filepath, mapName;
    int counter = 0;

    public ClassificationManager(String filepath, String mapName) {
        //this.classificationModel=classificationModel;
        this.filepath = filepath;
        this.mapName = mapName;
    }

    /**
     * Método responsável pela escrita, em ficheiro JSON, das informações do jogo, de forma a ser utilizado para qualificações
     *
     * @param classificationModel Modelo contendo a informação a ser guardada
     */
    public void recordToFile(ClassificationModel classificationModel) {
        Gson gson = new Gson(); // Instância gson para escrever o ficheiro Json
        File pathf = new File(this.filepath); // Ficheiro de destino
        JsonElement file = loadFromJSONFile(this.filepath);
        JsonArray classificationDataFromFile
                = (file != null && file.isJsonArray()
                ? file.getAsJsonArray() : new JsonArray());

        JsonObject classificationData = new JsonObject();
        classificationData.addProperty("PlayerName", classificationModel.getPlayerName());
        classificationData.addProperty("MapName", classificationModel.getMapName());
        classificationData.addProperty("RemainingHP", classificationModel.getRemainingHP());
        classificationData.addProperty("NumberOfMoves", classificationModel.getNumberOfMoves());
        classificationData.addProperty("Difficulty", classificationModel.getDifficulty());
        classificationDataFromFile.add(classificationData);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathf))) {
            writer.write(gson.toJson(classificationDataFromFile));
            writer.flush();
        } catch (IOException ex) {
        }
    }

    /**
     * @param filepath caminho para o ficheiro contendo a informação
     * @return Elemento JSON com informação do ficheiro
     */
    private JsonElement loadFromJSONFile(String filepath) {
        JsonElement json;
        try {
            FileReader inputFile = new FileReader(filepath);
            JsonParser parser = new JsonParser();
            json = parser.parse(inputFile);
        } catch (FileNotFoundException ex) {
            return null;
        }

        if (json.isJsonArray() && json.getAsJsonArray().size() == 0)
            return null;

        counter++;
        return json;
    }

    /**
     * Método responsável por carregar todas as classificações referentes a
     *
     * @return A lista com as instâncias das classificacaoes no ficheiro JSON
     */
    private ArrayUnorderedList<ClassificationModel> getClassificationsMap() {
        ArrayUnorderedList<ClassificationModel> mapClassifications = new ArrayUnorderedList<>();
        JsonElement file = loadFromJSONFile(this.filepath);
        JsonArray classificationDataFromFile
                = (file != null && file.isJsonArray()
                ? file.getAsJsonArray() : new JsonArray());

        if (classificationDataFromFile != null) {
            for (int ix = 0; ix < classificationDataFromFile.size(); ix++) {
                JsonObject classificationObject = classificationDataFromFile.get(ix).getAsJsonObject();
                if (classificationObject.get("MapName").getAsString().equals(mapName)) {
                    mapClassifications.addToRear(new ClassificationModel(classificationObject.get("MapName").getAsString(),
                            classificationObject.get("PlayerName").getAsString(), classificationObject.get("RemainingHP").getAsInt(),
                            classificationObject.get("NumberOfMoves").getAsInt(), classificationObject.get("Difficulty").getAsInt()));
                }
            }
            return mapClassifications;
        }
        return null;
    }

    /**
     * @param difficulty dificuldade da qual se pretende verificar as classificações
     * @return A Queue with Strings with the Players and their reamining HP, organized from top to bottom
     */
    protected ArrayQueue<String> getRemainingHPClassificationMap(int difficulty) {
        ArrayUnorderedList mapClassifications = getClassificationsMap();
        String playerNames[] = new String[mapClassifications.size()];
        ArrayQueue<String> organizedData = new ArrayQueue<>();
        int remainingHPeachPlayer[] = new int[mapClassifications.size()];

        Iterator classificationIterator = mapClassifications.iterator();
        int counter = 0;
        while (classificationIterator.hasNext()) {
            ClassificationModel model = (ClassificationModel) classificationIterator.next();
            if (model.getDifficulty() == difficulty) {
                playerNames[counter] = model.getPlayerName();
                remainingHPeachPlayer[counter] = model.getRemainingHP();
                counter++;
            }
        }

        int temp;
        String tempString;
        for (int ix = 0; ix < counter; ix++) {
            for (int jx = ix + 1; jx < counter; jx++) {
                if (remainingHPeachPlayer[ix] < remainingHPeachPlayer[jx]) {
                    temp = remainingHPeachPlayer[ix];
                    tempString = playerNames[ix];
                    remainingHPeachPlayer[ix] = remainingHPeachPlayer[jx];
                    playerNames[ix] = playerNames[jx];
                    remainingHPeachPlayer[jx] = temp;
                    playerNames[jx] = tempString;
                }
            }
        }
        for (int ix = 0; ix < counter; ix++) {
            String classificationDataString = "PLAYER NAME: " + playerNames[ix] + "; REMAINING HP: " + remainingHPeachPlayer[ix];
            organizedData.enqueue(classificationDataString);
        }
        return organizedData;
    }

    /**
     * @param difficulty dificuldade da qual se pretende verificar as classificações
     * @return A Queue with Strings with the Players and their Number of moves, organized from top to bottom
     */
    protected ArrayQueue<String> getNumberMovesClassificationMap(int difficulty) {
        ArrayUnorderedList mapClassifications = getClassificationsMap();
        String playerNames[] = new String[mapClassifications.size()];
        ArrayQueue<String> organizedData = new ArrayQueue<>();
        int numberOfMovesPlayer[] = new int[mapClassifications.size()];

        Iterator classificationIterator = mapClassifications.iterator();
        int counter = 0;
        while (classificationIterator.hasNext()) {
            ClassificationModel model = (ClassificationModel) classificationIterator.next();
            if (model.getDifficulty() == difficulty) {
                playerNames[counter] = model.getPlayerName();
                numberOfMovesPlayer[counter] = model.getNumberOfMoves();
                counter++;
            }
        }

        int temp;
        String tempString;
        for (int ix = 0; ix < counter; ix++) {
            for (int jx = ix + 1; jx < counter; jx++) {
                if (numberOfMovesPlayer[ix] > numberOfMovesPlayer[jx]) {
                    temp = numberOfMovesPlayer[ix];
                    tempString = playerNames[ix];
                    numberOfMovesPlayer[ix] = numberOfMovesPlayer[jx];
                    playerNames[ix] = playerNames[jx];
                    numberOfMovesPlayer[jx] = temp;
                    playerNames[jx] = tempString;
                }
            }
        }

        for (int ix = 0; ix < counter; ix++) {
            String classificationDataString = "PLAYER NAME: " + playerNames[ix] + "; NUMBER OF MOVES: " + numberOfMovesPlayer[ix];
            organizedData.enqueue(classificationDataString);
        }
        return organizedData;
    }

    /**
     * @param difficulty dificuldade a passar para verificar as classificações
     * @return uma String construida com as Strings das duas duas classificações
     * @throws EmptyCollectionException
     */
    public String getClassifications(int difficulty){
        String returnString = "Number of Moves: ->\n";
        ArrayQueue<String> movesQueue = getNumberMovesClassificationMap(difficulty);
        ArrayQueue<String> HPQueue = getRemainingHPClassificationMap(difficulty);
        int queueSize = movesQueue.size() + 1;

        if(!movesQueue.isEmpty()) {
            int counterEntry = 0;
            for (int ix = 0; ix++ < queueSize; ix++) {
                counterEntry++;
                try {
                    returnString = returnString + counterEntry + ": " + movesQueue.dequeue() + "\n";
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
            }

            counterEntry = 0;
            returnString += "\n \nRemaining HP: ->\n";

            for (int ix = 0; ix++ < queueSize; ix++) {
                counterEntry++;
                try {
                    returnString = returnString + counterEntry + ": " + HPQueue.dequeue() + "\n";
                } catch (EmptyCollectionException e) {
                    e.printStackTrace();
                }
            }
            return returnString;
        }else
            return "Map without classifications";

    }

}
