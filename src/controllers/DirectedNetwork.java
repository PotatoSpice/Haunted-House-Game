package controllers;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import interfaces.NetworkADT;
import java.util.Iterator;

public class DirectedNetwork<T> extends Graph<T> implements NetworkADT<T> {

    public DirectedNetwork() {
       super();
    }

    protected boolean checkVertexExistence(T vertex){
        for(int ix = 0; ix < numVertices; ix++){
            if(vertices[ix].equals(vertex)){
                return true;
            }
        }
        return false;
    }
    
    private boolean indexIsValid(int index) { 
        return ((index < numVertices) && (index >= 0)); 
    }
    
    /**
     * @param index index of the vertex to measure
     * @return true if its isolated, false otherwise
     */
    protected boolean isVertexIsolated(int index) {
        int count = 0;
        for(int ix=0; ix < numVertices; ix++){
            if(adjMatrix[index][ix]>=0)
                count++;
        }
        return (count<=1);
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

    /**
     * Este método cria um iterador que percorre, utilizando os caminhos mais curtos calculados pelo Algoritmo de Dijkstra, o caminho mais curto.
     *
     * @param sourceIndex Indíce do vértice de partida
     * @param destinationIndex Indice do vértice de destino
     * @return um iterador para o caminho encontrado através do algoritmo
     * @throws collections.exceptions.ElementNotFoundException
     */
    protected Iterator<T> dijkstraAlgorithm(int sourceIndex, int destinationIndex) 
            throws ElementNotFoundException {

        if(!indexIsValid(sourceIndex) && !indexIsValid(destinationIndex))
            throw new ElementNotFoundException("Index is invalid");

        int shortestPathValue[] = new int[numVertices];
        boolean vertexExistsInPath[] = new boolean[numVertices];
        Integer[] previous = new Integer[numVertices];

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
                    if (!vertexExistsInPath[jx] && adjMatrix[picked][jx] != null
                            && shortestPathValue[picked] < Integer.MAX_VALUE
                            && shortestPathValue[picked] + adjMatrix[picked][jx] < shortestPathValue[jx]) {
                        shortestPathValue[jx] = shortestPathValue[picked] + adjMatrix[picked][jx];
                        previous[jx]=picked;
                    }
                }
            }
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        int target = destinationIndex;
        if (previous[target] != null) {
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

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) 
            throws ElementNotFoundException {
        return dijkstraAlgorithm(getIndex(startVertex), getIndex(targetVertex));
    }
    
    @Override
    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++)
            if (vertices[i].equals(vertex))
                return i;
        return -1;
    }

    @Override
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
                if (adjMatrix[i][j] != null) {
                    result += i + " to " + j + "\t";
                    result += adjMatrix[i][j] + "\n";
                }
            }
        }
        result += "\n";
        return result;
    }
    
    // METODOS TEMPORARIOS -----------------------------------------------------
    
    public String testOnlyTOBEDELETED(){
        return toString();
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
}
