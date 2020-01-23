package controllers;
import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import collections.queue.LinkedQueue;
import collections.stack.LinkedStack;
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
            adjMatrix[index2][index1] = weight;
            connectionMatrix[index1][index2]=true;
        }
    }

    /**
     *
     *
     * @param vertex1 Vértice do começo
     * @param vertex2 Vértice do término
     * @return o valor do peso até ao final através do caminho de Dijkstra
     * @throws EmptyCollectionException
     * @throws ElementNotFoundException
     */
    @Override
    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException, ElementNotFoundException {

        int tempWeight=-1;
        int totalweight=0;

        Iterator iterator = dijkstraAlgorithm(getIndex(vertex1), getIndex(vertex2));
        while(iterator.hasNext()){
            int counter=0;
            while(tempWeight==-1){
                counter++;
                tempWeight = adjMatrix[counter][getIndex((T)iterator.next())];
            }
            totalweight=totalweight+tempWeight;
        }

        return totalweight;
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
           // adjMatrix[index2][index1] = 0;
           connectionMatrix[index1][index2]=true;
          //  connectionMatrix[index2][index1]=true;
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
            connectionMatrix[index1][index2] = false;
            connectionMatrix[index2][index1] = false;
        }
    }

    /**
     * Este método cria um iterador que percorre, utilizando os caminhos mais curtos calculados pelo Algoritmo de Dijkstra, o caminho mais curto.
     *
     * @param sourceIndex Indíce do vértice de partida
     * @param destinationIndex Indice do vértice de destino
     */
    public Iterator<T> dijkstraAlgorithm(int sourceIndex, int destinationIndex) throws ElementNotFoundException {

        if(!indexIsValid(sourceIndex) && !indexIsValid(destinationIndex))
            new ElementNotFoundException("Index is invalid");

        int shortestPathValue[] = new int[numVertices];
        boolean vertexExistsInPath[] = new boolean[numVertices];
        int[] previous = new int[numVertices];

        for (int ix = 0; ix < numVertices; ix++) {
            shortestPathValue[ix]=Integer.MAX_VALUE;
            vertexExistsInPath[ix]=false;
            previous[ix]=-1;
        }
        shortestPathValue[sourceIndex]=0;
        for (int ix = 1; ix < numVertices; ix++) {
                int picked = minimumDistance(shortestPathValue, vertexExistsInPath);
                vertexExistsInPath[picked] = true;

                for (int jx = 0; jx < numVertices; jx++) {
                    if (!vertexExistsInPath[jx] && adjMatrix[picked][jx] != -1 && shortestPathValue[picked] < Integer.MAX_VALUE
                            && shortestPathValue[picked] + adjMatrix[picked][jx] < shortestPathValue[jx]) {
                        shortestPathValue[jx] = shortestPathValue[picked] + adjMatrix[picked][jx];
                        previous[jx]=picked;
                    }
                }
            }
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        int target = destinationIndex;
        if (previous[target] != -1) {
            while (target != -1) {
                resultList.addToFront(this.vertices[target]);
                target = previous[target];

            }
        }
        printSolution(shortestPathValue);
        return resultList.iterator();
    }

    /**
     * @param shortestPathValue Array com distâncias entre os vértices
     * @param vertexExistsInPath Array que regista se os vértice estão presentes no caminho
     * @return o indice do caminho mínimo
     */
    private int minimumDistance(int shortestPathValue[], boolean vertexExistsInPath[]) {
        int minimum = Integer.MAX_VALUE;
        int minimum_index = -1;

        for (int ix = 0; ix < numVertices; ix++) {
            if (vertexExistsInPath[ix] == false && shortestPathValue[ix] <= minimum) {
                minimum = shortestPathValue[ix];
                minimum_index = ix;
            }
        }
        return minimum_index;
    }

    /**
     * TEST MEHTOD: Imprime o array cotendo as distâncias
     *
     * @param shortestPathValue Array com distâncias entre os vértices
     */
    void printSolution(int shortestPathValue[]) {
        System.out.println("Vertex \t\t Value of the Ghost");
        for (int i = 0; i < numVertices-1; i++)
            System.out.println(i + " \t\t " + shortestPathValue[i]);
    }

    /**
     * @param index index of the vertex to measure
     * @return true if its isolated, false otherwise
     */
    private boolean isVertexIsolated(int index){
        int count = 0;
        for(int ix=0; ix<numVertices; ix++){
            if(connectionMatrix[index][ix]==true)
                count++;
        }
        return (count<=1);
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
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) throws ElementNotFoundException {
        return dijkstraAlgorithm(getIndex(startVertex), getIndex(targetVertex));
    }

    @Override
    public boolean isEmpty() {
        return (numVertices==0);
    }

    @Override
    public boolean isConnected() throws EmptyCollectionException {
        return false;
    }

    @Override
    public int size() {
        return numVertices;
    }

    /**
     * @param index do vértice a ser verificado
     * @return true if it is, false if it isn't
     */
    protected boolean isConnectedToExterior(int index){ return connectionMatrix[index][numVertices-1]; }

    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++)
            if (vertices[i].equals(vertex))
                return i;
        return -1;
    }

    private boolean indexIsValid(int index) { return ((index < numVertices) && (index >= 0)); }

    /**
     * Duplica a capcidade do Array quando este atinge a sua capacidade máxima
     */
    private void expandCapacity() {
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
        connectionMatrix = largerBooleanMatrix;
    }

    public String testOnlyTOBEDELETED(){
        return toString();
    }

    public String toString() {
        if (numVertices == 0)
            return "Graph is empty";

        String result = "";

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
