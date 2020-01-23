package interfaces;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.graph.adt.GraphADT;

public interface NetworkADT<T> extends GraphADT<T>
{
    /** Inserts an edge between two vertices of this graph. */
    public void addEdge(T vertex1, T vertex2, int weight);

    /** Returns the weight of the shortest path in this network. */
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException, ElementNotFoundException;
}