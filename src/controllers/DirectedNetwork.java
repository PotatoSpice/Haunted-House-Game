package controllers;
import collections.exceptions.EmptyCollectionException;
import interfaces.NetworkADT;
import java.util.Iterator;

public class DirectedNetwork<T> implements NetworkADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;   // number of vertices in the graph
    protected int[][] adjMatrix;   // adjacency matrix
    protected boolean[][] connectionMatrix; //matrix which double checks connections between rooms
    protected T[] vertices;   // values of vertices

    public DirectedNetwork() {
        numVertices = 0;
        this.adjMatrix = new int[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.connectionMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[])(new Object[DEFAULT_CAPACITY]);
    }

    /** TODO: Criar entrada para o edificio
     *
     */
    protected void addEntrance(){

    }

    /**
     * TODO: Criar saída para o edíficio
     */
    protected void addExterior(){

    }

    protected boolean checkVertexExistence(T vertex){
        for(int ix = 0; ix < numVertices; ix++){
            if(vertices[ix].equals(vertex)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param vertex1 Vértice que contem o "fantasma" no seu room, ou seja, que terá o peso associado
     * @param vertex2 Vértice de onde o "utilizador parte ou parte para"
     * @param weight O Valor do Fantasma
     */
    @Override
    public void addEdge(T vertex1, T vertex2, int weight) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = weight;
          //
            //  adjMatrix[index2][index1] = 0;
            connectionMatrix[index1][index2]=true;
            connectionMatrix[index2][index1]=true;
        }
    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException {
        return 0;
    }

    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = -1;
            adjMatrix[i][numVertices] = -1;
            connectionMatrix[numVertices][i]=false;
            connectionMatrix[i][numVertices]=false;
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
                vertices[ix] = vertices[ix+1];

            for (int ix = index; ix < numVertices; ix++)
                for (int jx = 0; jx <= numVertices; jx++)
                    adjMatrix[ix][jx] = adjMatrix[ix+1][jx];

            for (int ix = index; ix < numVertices; ix++)
                for (int jx = 0; jx < numVertices; jx++)
                    adjMatrix[jx][ix] = adjMatrix[jx][ix+1];

            for (int ix = index; ix < numVertices; ix++)
                for (int jx = 0; jx < numVertices; jx++)
                    connectionMatrix[ix][jx] = connectionMatrix[ix+1][jx];

            for (int ix = index; ix < numVertices; ix++)
                for (int jx = 0; jx < numVertices; jx++)
                    connectionMatrix[jx][ix] = connectionMatrix[jx+1][ix];
        }
    }

    //Usa-se este quanto não há fantasma nas duas divisões, assume-se que. apesar de ser orientado, existe coneções entre todas excepto a entrada e o exterior, que teremos de ter tratamento diferente
    @Override
    public void addEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 0;
            adjMatrix[index2][index1] = 0;
            connectionMatrix[index1][index2]=true;
            connectionMatrix[index2][index1]=true;
        }
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = -1;
            adjMatrix[index2][index1] = -1;
            connectionMatrix[index1][index2] = false;
            connectionMatrix[index2][index1] = false;
        }
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
        return numVertices;
    }

    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++)
            if (vertices[i].equals(vertex))
                return i;
        return -1;
    }

    private boolean indexIsValid(int index)
    {
        return ((index < numVertices) && (index >= 0));
    }

    private void expandCapacity()
    {
        T[] largerVertices = (T[])(new Object[vertices.length*2]);

        int[][] largerAdjMatrix =
                new int[vertices.length*2][vertices.length*2];
        boolean[][] largerBooleanMatrix = new boolean[vertices.length*2][vertices.length*2];
        for (int ix = 0; ix < numVertices; ix++) {
            for (int jx = 0; jx < numVertices; jx++) {
                largerAdjMatrix[ix][jx] = adjMatrix[ix][jx];
                largerBooleanMatrix[ix][jx] = connectionMatrix[ix][jx];
            }
            largerVertices[ix] = vertices[ix];
        }

        vertices = largerVertices;
        adjMatrix = largerAdjMatrix;
    }

    public String testOnlyTOBEDELETED(){
        return toString();
    }

    public String toString() {
        if (numVertices == 0)
            return "Graph is empty";

        String result = new String("");

        /** Print the adjacency Matrix */
        result += "Adjacency Matrix\n";
        result += "----------------\n";
        result += "index\t";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i;
            if (i < 10)
                result += " ";
        }
        result += "\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";

            for (int j = 0; j < numVertices; j++) {
              /*  if (adjMatrix[i][j] >= 0)
                    result += "1 ";
                else
                    result += "-1 ";
                    */
                result += adjMatrix[i][j] + " ";
            }



                result += "\n";
            }

            /** Print the vertex values */
            result += "\n\nVertex Values";
            result += "\n-------------\n";
            result += "index\tvalue\n\n";

            for (int i = 0; i < numVertices; i++) {
                result += "" + i + "\t";
                result += vertices[i].toString() + "\n";
            }

            /** Print the weights of the edges */
            result += "\n\nWeights of Edges";
            result += "\n----------------\n";
            result += "index\tweight\n\n";

            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (adjMatrix[i][j] >= 0) {
                        result += i + " to " + j + "\t";
                        result += adjMatrix[i][j] + "\n";
                    }
                }
            }

            result += "\n";
            return result;
        }
}
