package controllers;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import collections.queue.LinkedQueue;
import collections.stack.LinkedStack;
import interfaces.GraphADT;

import java.util.Iterator;

public class Graph<T> implements GraphADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;   // number of vertices in the graph
    protected int[][] adjMatrix;   // adjacency matrix
    protected T[] vertices;   // values of vertices

    public Graph() {
        numVertices = 0;
        this.adjMatrix = new int[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[])(new Object[DEFAULT_CAPACITY]);
    }

    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = -1;
            adjMatrix[i][numVertices] = -1;
        }
        numVertices++;
    }

    /**
     * Remove o vértice do grafo. Faz "roll-back" do grafo para trás
     *
     * @param vertex Vértice a remover do grafo.
     */
    @Override
    public void removeVertex(T vertex) {
        int index = getIndex(vertex);
        if (indexIsValid(index)) {
            numVertices--;

            for (int ix = index; ix < numVertices; ix++)
                vertices[ix] = vertices[ix + 1];

            for (int ix = index; ix < numVertices; ix++)
                for (int jx = 0; jx <= numVertices; jx++)
                    adjMatrix[ix][jx] = adjMatrix[ix + 1][jx];

            for (int ix = index; ix < numVertices; ix++)
                for (int jx = 0; jx < numVertices; jx++)
                    adjMatrix[jx][ix] = adjMatrix[jx][ix + 1];
        }
    }

    /**
     * @param vertex1 Vertice Inicial
     * @param vertex2 Vertice Destino
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 0;
        }
    }

    /**
     * @param vertex1 Vértice fonte
     * @param vertex2 Vértice Destino
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = -1;
            adjMatrix[index2][index1] = -1;
        }
    }

    @Override
    public Iterator iteratorBFS(T startVertex) throws EmptyCollectionException {
        int startIndex = getIndex(startVertex);
        int tempIndex;
        LinkedQueue<Integer> linkedQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[numVertices];
        for (int ix = 0; ix < numVertices; ix++) {
            visited[ix] = false;
        }

        linkedQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!linkedQueue.isEmpty()) {
            tempIndex = linkedQueue.dequeue();
            resultList.addToRear(vertices[tempIndex]);
            for (int ix = 0; ix < numVertices; ix++) {
                if((adjMatrix[tempIndex][ix] < Integer.MAX_VALUE) && !visited[ix]) {
                    linkedQueue.enqueue(ix);
                    visited[ix] = true;
                }
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator iteratorDFS(T startVertex) throws EmptyCollectionException {
        int startIndex = getIndex(startVertex);

        if(!indexIsValid(startIndex)){
            new EmptyCollectionException("Invalid index");
        }

        int tempIndex;
        boolean found;
        LinkedStack<Integer> linkedStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList();
        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }
        for (int ix = 0; ix < numVertices; ix++) {
            visited[ix] = false;
        }

        linkedStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!linkedStack.isEmpty()) {
            tempIndex = linkedStack.peek();
            found = false;
            for (int ix = 0; (ix < numVertices) && !found; ix++) {
                if((adjMatrix[tempIndex][ix] < Integer.MAX_VALUE) && !visited[ix]) {
                    linkedStack.push(ix);
                    resultList.addToRear(vertices[ix]);
                    visited[ix] = true;
                    found = true;
                }
            }
            if (!found && !linkedStack.isEmpty()) {
                linkedStack.pop();
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) throws EmptyCollectionException, ElementNotFoundException {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return (numVertices==0);
    }

    @Override
    public boolean isConnected() throws EmptyCollectionException {
        if (isEmpty())
            return false;

        Iterator<T> it = iteratorBFS(vertices[0]);
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return (count == numVertices);
    }

    @Override
    public int size() {
        return numVertices;
    }

    /**
     * @param vertex a retirar o índice
     * @return o índice, -1 se for não existir
     */
    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++)
            if (vertices[i].equals(vertex))
                return i;
        return -1;
    }

    /**
     * @param index Indíce a verificar se está contido nos intervalos
     * @return true se estiver, false se não estiver
     */
    private boolean indexIsValid(int index) { return ((index < numVertices) && (index >= 0)); }

    /**
     * Duplica a capcidade do Array quando este atinge a sua capacidade máxima
     */
    private void expandCapacity () {
        T[] largerVertices = (T[]) (new Object[vertices.length * 2]);
        int[][] largerAdjMatrix = new int[vertices.length * 2][vertices.length * 2];
        boolean[][] largerBooleanMatrix = new boolean[vertices.length * 2][vertices.length * 2];
        for (int ix = 0; ix < numVertices; ix++) {
            for (int jx = 0; jx < numVertices; jx++) {
                largerAdjMatrix[ix][jx] = adjMatrix[ix][jx];
            }
            largerVertices[ix] = vertices[ix];
        }
        vertices = largerVertices;
        adjMatrix = largerAdjMatrix;
    }

}
