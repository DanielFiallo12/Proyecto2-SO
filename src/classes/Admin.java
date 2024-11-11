/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.HashSet;
import java.util.Random;
import javax.swing.JLabel;
import interfaces.MainInterface;
/**
 *
 * @author adminccs
 */
public class Admin {
    
    public static int roundCounter = 0;
    public static Queue<Character> starWarsQueue1; // Colas para Nintendo 
    public static Queue<Character> starWarsQueue2;
    public static Queue<Character> starWarsQueue3;
    public static Queue<Character> starTrekQueue1; // Colas para Bethesda 
    public static Queue<Character> starTrekQueue2;
    public static Queue<Character> starTrekQueue3;
    public static Queue<Character> reinforcementQueueStarWars; // Cola de refuerzo para Nintendo
    public static Queue<Character> reinforcementQueueStarTrek; // Cola de refuerzo para Bethesda
    
    public Admin() {
        // Inicializa las colas para Star Wars
        starWarsQueue1 = new Queue<>();
        starWarsQueue2 = new Queue<>();
        starWarsQueue3 = new Queue<>();

        // Inicializa las colas para Star Trek
        starTrekQueue1 = new Queue<>();
        starTrekQueue2 = new Queue<>();
        starTrekQueue3 = new Queue<>();

        // Inicializa las colas de refuerzo
        reinforcementQueueStarWars = new Queue<>();
        reinforcementQueueStarTrek = new Queue<>();
    }
    
    public void initializeCharacters() {
        for (int i = 0; i < 20; i++) {
            Character starWarsCharacter = Character.createStarWarsCharacter();
            Character starTrekCharacter = Character.createStarTrekCharacter();

            addToQueue(starWarsCharacter, starWarsQueue1, starWarsQueue2, starWarsQueue3);
            addToQueue(starTrekCharacter, starTrekQueue1, starTrekQueue2, starTrekQueue3);
        }
    }
    
    public static void addToQueue(Character character, Queue<Character>... queues) {
        int priorityIndex = character.getLevelPriority() - 1;
        queues[priorityIndex].enqueue(character);
    }

    public static void upgradeToQueue(Character character, Queue<Character>... queues) {

        int NewpriorityIndex = character.getLevelPriority() - 1;
        int priorityIndex = character.getLevelPriority();
        // Eliminar el personaje de las colas existentes (si está presente)
        
        queues[NewpriorityIndex].enqueue(character);

    }
    
    public Character[] selectCharactersForBattle() {
        Character[] charactersForBattle = new Character[2];

        charactersForBattle[0] = selectCharacterFromNextNonEmptyQueue(starWarsQueue1, starWarsQueue2, starWarsQueue3);
        charactersForBattle[1] = selectCharacterFromNextNonEmptyQueue(starTrekQueue1, starTrekQueue2, starTrekQueue3);

        return charactersForBattle;

    }

    private Character selectCharacterFromNextNonEmptyQueue(Queue<Character>... queues) {
        for (int i = 0; i < queues.length; i++) {
            if (!queues[i].isEmpty()) {
                return queues[i].dequeue();
            }
        }
        return null; // Si todas las colas están vacías
    }
    
     // Ya no lo uso para selecionar los personajes de las colas.
    private Character selectCharacterFromQueue(int queueIndex, Queue<Character>... queues) {
        return queues[queueIndex].isEmpty() ? null : queues[queueIndex].dequeue();
    }

    public static void moveCharacterToPriority(Queue<Character> reinforcementQueue, Queue<Character> priorityQueue) {
        if (!reinforcementQueue.isEmpty()) {
            Character character = reinforcementQueue.dequeue();
            priorityQueue.enqueue(character);
        }
    }
        
    // Sube los personajes en la posicion 1 de las colas. 
    public void updateQueues() {
        for (int i = 1; i < 3; i++) {
            Character starWarsCharacter = selectCharacterFromQueue(i, starWarsQueue1, starWarsQueue2, starWarsQueue3);
            if (starWarsCharacter != null) {
                switch (i) {
                    case 1:
                        starWarsQueue1.enqueue(starWarsCharacter);
                        break;
                    case 2:
                        starWarsQueue2.enqueue(starWarsCharacter);
                        break;
                    case 3:
                        starWarsQueue3.enqueue(starWarsCharacter);
                        break;
                }
            }

            Character starTrekCharacter = selectCharacterFromQueue(i, starTrekQueue1, starTrekQueue2, starTrekQueue3);
            if (starTrekCharacter != null) {
                switch (i) {
                    case 1:
                        starTrekQueue1.enqueue(starTrekCharacter);
                        break;
                    case 2:
                        starTrekQueue2.enqueue(starTrekCharacter);
                        break;
                    case 3:
                        starTrekQueue3.enqueue(starTrekCharacter);
                        break;
                }
            }
        }
    }   
   
    public static boolean shouldGenerateCharacters() {
        // Devolver true con una probabilidad del 80% (para generar personajes)
        return new Random().nextInt(100) < 80;
    }
    
    public static void generateCharacters() {
        Character starWarsCharacter = Character.createStarWarsCharacter();
        Character starTrekCharacter = Character.createStarTrekCharacter();

        addToQueue(starWarsCharacter, starWarsQueue1, starWarsQueue2, starWarsQueue3);
        addToQueue(starTrekCharacter, starTrekQueue1, starTrekQueue2, starTrekQueue3);
    }

    public static boolean shouldMoveToPriority1() {
        // Devuelve true con un 40% de probabilidad (para sacar de la cola de refuerzo)
        return new Random().nextInt(100) < 40;
    }
    //Lógica para aumentar contadores de Characters

    public static void incrementRoundCounters() {
        Queue<Character>[] allQueues = new Queue[]{
            starWarsQueue1, starWarsQueue2, starWarsQueue3,
            starTrekQueue1, starTrekQueue2, starTrekQueue3
        };

        for (Queue<Character> queue : allQueues) {
            incrementQueueRoundCounters(queue);
        }
    }
    
    // Lógica para aumentar el nivel de prioridad (subir de cola)
    private static void incrementQueueRoundCounters(Queue<Character> queue) {
        Queue<Character> tempQueue = new Queue<>();
        while (!queue.isEmpty()) {
            Character character = queue.dequeue();
            character.increaseRoundCounter();
            tempQueue.enqueue(character);
        }
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
    }
    
    // Método para actualizar las colas en los JLabel
    public static void actualizarColasEnInterfaz() {
        // Para Star Wars
        for (int i = 0; i < 4; i++) {
            StringBuilder resultadoStarWars = new StringBuilder();
            JLabel ColasSW = MainInterface.getColasStarWars(i + 1);

            if (ColasSW != null) {
                Queue<Character> currentQueue;

                // Determinar la cola actual según el índice
                if (i < 3) {
                    resultadoStarWars.append("Star Wars Queue " + (i + 1) + ":\n");
                    currentQueue = getStarWarsQueue(i + 1);
                } else {
                    resultadoStarWars.append("Reinforcement Queue Star Wars:\n");
                    currentQueue = reinforcementQueueStarWars;
                }

                resultadoStarWars.append(printQueueToString(currentQueue));
                ColasSW.setText(resultadoStarWars.toString());
            }
        }

        // Para Star Trek
        for (int i = 0; i < 4; i++) {
            StringBuilder resultadoStarTrek = new StringBuilder();
            JLabel ColasST = MainInterface.getColasStarTrek(i + 1);

            if (ColasST != null) {
                Queue<Character> currentQueue;

                // Determinar la cola actual según el índice
                if (i < 3) {
                    resultadoStarTrek.append("Star Trek Queue " + (i + 1) + ":\n");
                    currentQueue = getStarTrekQueue(i + 1);
                } else {
                    resultadoStarTrek.append("Reinforcement Queue Star Trek:\n");
                    currentQueue = reinforcementQueueStarTrek;
                }

                resultadoStarTrek.append(printQueueToString(currentQueue));
                ColasST.setText(resultadoStarTrek.toString());
            }
        }
    }
    
    private static Queue<Character> getStarWarsQueue(int index) {
        switch (index) {
            case 1:
                return starWarsQueue1;
            case 2:
                return starWarsQueue2;
            case 3:
                return starWarsQueue3;
            case 4:
                return reinforcementQueueStarWars;
            default:
                return null;
        }
    }

    private static Queue<Character> getStarTrekQueue(int index) {
        switch (index) {
            case 1:
                return starTrekQueue1;
            case 2:
                return starTrekQueue2;
            case 3:
                return starTrekQueue3;
            case 4:
                return reinforcementQueueStarTrek;
            default:
                return null;
        }
    }
  
    private static <T> String printQueuesToString(Queue<T>... queues) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < queues.length; i++) {
            result.append("Queue ").append(i + 1).append(": ");
            result.append(printQueueToString(queues[i]));
        }
        return result.toString();
    }
    
    private static <T> String printQueueToString(Queue<T> queue) {
        Queue<T> tempQueue = new Queue<>();
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            T data = queue.dequeue();
            result.append(data).append(" ");
            tempQueue.enqueue(data);
        }
        result.append("\n");
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
        return result.toString();
    }
    
    public void printQueues() {
        System.out.println("Star Wars Queues:");
        printQueues(starWarsQueue1, starWarsQueue2, starWarsQueue3);

        System.out.println("\nReinforcement Queue Star Wars:");
        printQueue(reinforcementQueueStarWars);

        System.out.println("\nStar Trek Queues:");
        printQueues(starTrekQueue1, starTrekQueue2, starTrekQueue3);

        System.out.println("\nReinforcement Queue Star Trek:");
        printQueue(reinforcementQueueStarTrek);
    }
    
     private <T> void printQueues(Queue<T>... queues) {
        for (int i = 0; i < queues.length; i++) {
            System.out.print("Queue " + (i + 1) + ": ");
            printQueue(queues[i]);
        }
    }

    private <T> void printQueue(Queue<T> queue) {
        Queue<T> tempQueue = new Queue<>();
        while (!queue.isEmpty()) {
            T data = queue.dequeue();
            System.out.print(data + " ");
            tempQueue.enqueue(data);
        }
        System.out.println();
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
    }
    
}
