package interfaces;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;

public interface NetworkADT<T> extends GraphADT<T>
{
    /** 
     * Inserts an edge between two vertices of this graph.
     * 
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     * @param weight conexion weight between the vertexes
     */
    public void addEdge(T vertex1, T vertex2, int weight);

    /**
     * Returns the weight of the shortest path in this network.
     * 
     * @param vertex1 starting vertex
     * @param vertex2 destination vertex
     * @return shortest path algorithm result
     * @throws collections.exceptions.EmptyCollectionException if collection is empty
     * @throws collections.exceptions.ElementNotFoundException if elements are not found
     */
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException, ElementNotFoundException;
}