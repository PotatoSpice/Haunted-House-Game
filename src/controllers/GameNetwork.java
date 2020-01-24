package controllers;

import collections.list.unordered.ArrayUnorderedList;
import interfaces.IGameNetwork;

import java.util.Iterator;

public class GameNetwork<T> extends DirectedNetwork<T> implements IGameNetwork<T> {

    private String mapName;
    private int difficulty=1;
    private T currentPosition;
    private int HP;
    public GameNetwork(){
        super();
    }

    /**
     * @param index do vértice a ser verificado
     * @return true if it is, false if it isn't
     */
    protected boolean isConnectedToExterior(int index){ return (adjMatrix[index][numVertices-1]>=0); }

    @Override
    public void setDifficulty(int difficulty) {
        this.difficulty=difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public ArrayUnorderedList<T> getRoomConnections(T vertex) {
        ArrayUnorderedList<T> connectedRooms = new ArrayUnorderedList<>();
        int index = getIndex(vertex);
        for(int ix=0; ix<numVertices; ix++){
            if(adjMatrix[index][ix]>=0){
               connectedRooms.addToRear(vertices[ix]);
            }
        }
        return connectedRooms;
    }

    /**
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
        if (!isFinished(newIndex)) {
            HP = HP - adjMatrix[getIndex(currentPosition)][newIndex]*difficulty;
            if (stillAlive(HP)) {
                currentPosition = newPosition;
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setInitialPosition(T initialPosition) {
        currentPosition = initialPosition;
    }

    @Override
    public T getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public boolean stillAlive(int remainingHP) {
        return (remainingHP>0);
    }

    @Override
    public int getHP() {
        return HP;
    }

    @Override
    public boolean isFinished(int index) {
        return index==numVertices-1;
    }

    @Override
    public void saveClassification() {

    }

    @Override
    public String getClassifications() {
        return null;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
