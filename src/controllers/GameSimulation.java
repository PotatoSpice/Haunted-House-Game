package controllers;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;

import java.util.Iterator;

public class GameSimulation {

    GameNetwork<String> gameNetwork;
    ArrayUnorderedList<String> simulationRooms;
    ArrayUnorderedList<Integer> hpPerStage;
    boolean simulationError = true;
    boolean died = false;

    public GameSimulation(GameNetwork<String> gameNetwork){
        this.gameNetwork=gameNetwork;
        simulationRooms = new ArrayUnorderedList<>();
        hpPerStage = new ArrayUnorderedList<>();
    }

    public void simulation() throws ElementNotFoundException, EmptyCollectionException {
        Iterator roomIterator = this.gameNetwork.iteratorShortestPath("entrada", "exterior");
        int counter=0;
        gameNetwork.setInitialPosition((String)roomIterator.next());
        while(roomIterator.hasNext()){
            String room = (String)roomIterator.next();
            if(gameNetwork.isFinished(gameNetwork.getIndex(room))){
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
            hpPerStage.addToRear(gameNetwork.getHP());
            counter++;
        }

    }

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


}
