package controllers;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import collections.queue.ArrayQueue;

import java.util.Iterator;

/**
 * Classe responsável por gerir e apresentar a simulação de um jogo no mapa carregado.
 */
public class GameSimulation {

    GameNetwork<String> gameNetwork;
    ArrayUnorderedList<String> simulationRooms;
    ArrayUnorderedList<Integer> hpPerStage;
    boolean simulationError = true;
    boolean died = false;
    int counter=0;

    public GameSimulation(GameNetwork<String> gameNetwork){
        this.gameNetwork=gameNetwork;
        simulationRooms = new ArrayUnorderedList<>();
        hpPerStage = new ArrayUnorderedList<>();
    }

    /**
     * Este método carrega os pontos de vida e os rooms da simulação do jogo, aplicando o algoritmo de djisktra
     *
     * @throws ElementNotFoundException
     */
    public void simulation() throws ElementNotFoundException, EmptyCollectionException {
        Iterator<String> roomIterator = this.gameNetwork.iteratorShortestPath("entrada", "exterior");

        gameNetwork.setNewPosition(roomIterator.next());
        while(roomIterator.hasNext()){
            String room = roomIterator.next();
            if(gameNetwork.isFinished(room)){
                break;
            }
            if(gameNetwork.isMoveValid(room)){
                if(!gameNetwork.setNewPosition(room)){
                    died=true;
                    break;
                }
            }else{
                simulationError = true;
                break;
            }
            simulationRooms.addToRear(room);
            hpPerStage.addToRear(gameNetwork.getCurrentHp());
            counter++;
        }
    }

    /**
     * @return a String preparada para apresentar a simulação
     */
    public String simulationString(){
        String textSimulation="";
        int count = 0;
        Iterator iterator = simulationRooms.iterator();
        Iterator hpIterator = hpPerStage.iterator();
        while(iterator.hasNext()){
            String room = (String)iterator.next();
            int stageHP = (Integer)hpIterator.next();
            count++;
            textSimulation=textSimulation+"\n ROOM ORDER NUMBER: "+count+ "; Name: "+room+ "; HP Remaining: " +stageHP+"; \n \n NEXT ROUND-> \n \n";
        }
        textSimulation= textSimulation+"YOU ARRIVED AT THE EXTERIOR! Congratulations...";

        return textSimulation;
    }

    /**
     * @return o número de movimentos executados na simulação
     */
    public int numberOfMovesSim(){
        return this.counter;
    }

    /**
     * Este método serve para enviar a estrutura da simulação com os aposentos utilizados
     *
     * @return Um ArrayList desordenado com os rooms organizados por ordem de entrada
     */
    public ArrayUnorderedList<String> roomsInOrder(){return this.simulationRooms;}

    /**
     * Este método serve para enviar para a simulação gráfica as informações já calculadas com a HP
     *
     * @return Um ArrayList desordenado com a quantidade de HP por estágio da simulação
     */
    public ArrayUnorderedList<Integer> hpInOrder(){ return this.hpPerStage;}

}
