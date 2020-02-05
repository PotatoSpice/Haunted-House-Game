package interfaces;

import collections.list.unordered.ArrayUnorderedList;

public interface IGameNetwork<T> extends  NetworkADT<T> {

    /**
     * Retorna todas as conexões do vértice requerido a outros vértices.
     * 
     * @param vertex vértice para verificação
     * @return lista com todas as conexões
     */
    ArrayUnorderedList<T> getRoomConnections(T vertex);

    /**
     * Define a nova posição quando há uma mudança de aposento
     * Verificará os estados, a quantidade de pontos de vida que restam e 
     * incrementará ao número de movimentos efetuados
     *
     * @param newPosition aposento para a nova posição
     * @return true se a nova posição é válida, falso no contrário
     */
    boolean setNewPosition(T newPosition);

    /**
     * Retorna a posição atual (vértice atual) do grafo.
     * 
     * @return nome do aposento relativo à posição atual
     */
    T getCurrentPosition();

    /**
     * Verifica, para cada "nova posição", se ainda se encontra vivo
     *
     * @return true se estiver com vida acima de 0, false se esta tiver a 0 ou inferior
     */
    boolean stillAlive();

    /**
     * @return os Pontos de Vida atualmente presentes
     */
    int getCurrentHp();

    /**
     * @return nome do mapa relativo ao grafo
     */
    String getMapName();

    /**
     * @return difficuldade relativa ao mapa
     */
    int getDifficulty();

    /**
     * @param vertex vértice para verificação
     * @return true se o utilizador tiver atingido o índice do exterior, false se não
     */
    boolean isFinished(T vertex);

}
