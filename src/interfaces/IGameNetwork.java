package interfaces;

import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;

public interface IGameNetwork<T> extends  NetworkADT<T> {

    /**
     * 
     * @param vertex
     * @return 
     */
    ArrayUnorderedList<T> getRoomConnections(T vertex);

    /**
     * Define a nova posição quando há uma mudança de aposento
     * Verificará os estados, a quantidade de pontos de vida que restam e incrementará ao número de movimentos efetuados
     *
     *
     * @param newPosition
     * @return
     */
    boolean setNewPosition(T newPosition);

    /**
     * Retorna a sua atual posição
     * @return
     */
    T getCurrentPosition();

    /**
     * Verifica, para cada "nova posição", se ainda se encontra vivo
     *
     * @param remaniningHP quantidade vida que resta
     * @return true se estiver com vida acima de 0, false se esta tiver a 0 ou inferior
     */
    boolean stillAlive(int remaniningHP);

    /**
     * @return os Pontos de Vida atualmente presentes
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
     * @param index índice da posição do aposento onde se encontra o jogador
     * @return true se o utilizador tiver atingido o índice do exterior, false se não
     */
    boolean isFinished(int index);

    /**
     * Guarda a classificação do jogador num ficheiro do tipo json.
     * @param playerName Nickname do jogador
     */
    void saveClassification(String playerName);

    /**
     * @return String com as classificações totais para o mapa
     * @throws collections.exceptions.EmptyCollectionException
     */
    String getClassifications() throws EmptyCollectionException;

}
