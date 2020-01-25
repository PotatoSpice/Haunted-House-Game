package interfaces;

import collections.list.unordered.ArrayUnorderedList;

public interface IGameNetwork<T> extends  NetworkADT<T> {

    /**
     * 
     * @param vertex
     * @return 
     */
    ArrayUnorderedList<T> getRoomConnections(T vertex);

    /**
     * @param newPosition
     * @return
     */
    boolean setNewPosition(T newPosition);

    /**
     * @return
     */
    T getCurrentPosition();

    /**
     * @param remaniningHP
     * @return
     */
    boolean stillAlive(int remaniningHP);

    /**
     * @return
     */
    int getCurrentHp();
    
    /**
     * @return
     */
    String getMapName();
    
    /**
     * @return
     */
    int getDifficulty();

    /**
     * @param index
     * @return
     */
    boolean isFinished(int index);

    /**
     *
     */
    void saveClassification();

    /**
     * @return
     */
    String getClassifications();

}
