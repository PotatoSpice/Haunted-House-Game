package interfaces;

import collections.list.unordered.ArrayUnorderedList;

public interface IGameNetwork<T> extends  NetworkADT<T> {

    /**
     * @param difficulty
     */
    void setDifficulty(int difficulty);

    ArrayUnorderedList<T> getRoomConnections(T vertex);

    /**
     * @param newPosition
     * @return
     */
    boolean setNewPosition(T newPosition);

    void setInitialPosition(T initialPosition);

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
    int getHP();

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
