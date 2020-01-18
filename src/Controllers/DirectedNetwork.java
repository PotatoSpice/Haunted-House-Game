package Controllers;
import ADT.EmptyCollectionException;
import Interfaces.NetworkADT;
import java.util.Iterator;

public class DirectedNetwork<T> implements NetworkADT<T> {
    @Override
    public void addEdge(T vertex1, T vertex2, int weight) {

    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException {
        return 0;
    }

    @Override
    public void addVertex(T vertex) {

    }

    @Override
    public void removeVertex(T vertex) {

    }

    @Override
    public void addEdge(T vertex1, T vertex2) {

    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {

    }

    @Override
    public Iterator iteratorBFS(T startVertex) throws EmptyCollectionException {
        return null;
    }

    @Override
    public Iterator iteratorDFS(T startVertex) throws EmptyCollectionException {
        return null;
    }

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isConnected() throws EmptyCollectionException {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
