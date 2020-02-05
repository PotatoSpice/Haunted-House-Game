package controllers;

import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import collections.queue.ArrayQueue;
import interfaces.IGameNetwork;
import models.ClassificationModel;

import java.util.Iterator;
import java.util.Queue;

public class GameNetwork<T> extends DirectedNetwork<T> implements IGameNetwork<T> {

    private final int DEFAULT_HP = 100;
    private final int DEFAULT_DIFFICULTY = 1;

    private final String mapName;
    private final int difficulty;
    private T currentPosition;
    private int currentHp;

    private int moveCount = 0;
    private ClassificationManager classificationManager; //A Iniciar no Construtor

    public GameNetwork(String mapName, T initialPosition) {
        super();
        this.mapName = mapName;
        this.currentPosition = initialPosition;
        this.difficulty = DEFAULT_DIFFICULTY;
        this.currentHp = DEFAULT_HP;
    }

    public GameNetwork(String mapName, T initialPosition, int startHP) {
        super();
        this.mapName = mapName;
        this.currentPosition = initialPosition;
        this.difficulty = DEFAULT_DIFFICULTY;
        this.currentHp = (startHP > 0 ? startHP : DEFAULT_HP);
    }

    public GameNetwork(int difficulty, String mapName, T initialPosition) {
        super();
        this.mapName = mapName;
        this.currentPosition = initialPosition;
        this.difficulty = (difficulty > 0 && difficulty <= 3 ? difficulty : DEFAULT_DIFFICULTY);
        this.currentHp = DEFAULT_HP;
    }

    public GameNetwork(int difficulty, String mapName, T initialPosition, int startHP) {
        super();
        this.mapName = mapName;
        this.currentPosition = initialPosition;
        this.difficulty = (difficulty > 0 && difficulty <= 3 ? difficulty : DEFAULT_DIFFICULTY);
        this.currentHp = (startHP > 0 ? startHP : DEFAULT_HP);
    }

    /**
     * @param index do vértice a ser verificado
     * @return true if it is, false if it isn't
     */
    protected boolean isConnectedToExterior(int index) {
        return (adjMatrix[index][numVertices-1] != null);
    }

    @Override
    public ArrayUnorderedList<T> getRoomConnections(T vertex) {
        ArrayUnorderedList<T> connectedRooms = new ArrayUnorderedList<>();
        int index = getIndex(vertex);
        for(int ix=0; ix<numVertices; ix++){
            if(adjMatrix[index][ix]!=null){
               connectedRooms.addToRear(vertices[ix]);
            }
        }
        return connectedRooms;
    }

    /**
     * Este método verifica se uma nova posição é válida, de forma a evitar bugs.
     *
     * @param newPosition nova posição
     * @return true caso seja válido, falso caso seja inválido
     */
    public boolean isMoveValid(T newPosition){
        Iterator iterator = getRoomConnections(currentPosition).iterator();
        while(iterator.hasNext()){
            if(newPosition.equals(iterator.next())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setNewPosition(T newPosition) { //Decidi não fazer verificações sobre o Vértice é ou não válido.
        int newIndex = getIndex(newPosition);

        if (newPosition.equals("entrada"))
            currentPosition = newPosition;
        else if (!isFinished(newPosition)) {
            currentHp = currentHp - adjMatrix[getIndex(currentPosition)][newIndex]*difficulty;
            if (stillAlive()) {
                currentPosition = newPosition;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean stillAlive() {
        return (currentHp> 0);
    }

    @Override
    public boolean isFinished(T vertex) {
        return getIndex(vertex)==numVertices-1;
    }

    @Override
    public T getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public String getMapName() {
        return mapName;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }
}
