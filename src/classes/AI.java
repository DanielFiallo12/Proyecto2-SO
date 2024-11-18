/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import interfaces.MainInterface;
/**
 *
 * @author adminccs
 */
public class AI {
    
    public static int time = 10;
    private static final double WIN_PROBABILITY = 0.4;
    private static final double DRAW_PROBABILITY = 0.27;
    private static final double NO_COMBAT_PROBABILITY = 0.33;
    public static int ganadoresStarWars = 0;
    public static int ganadoresStarTrek = 0;
    
    public void processBattle(Character starWarsCharacter, Character starTrekCharacter) {

        MainInterface.setDefStats();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double result = Math.random();

        // Utilizamos los puntos únicos para determinar el resultado
        int starWarsPoints = starWarsCharacter.getUniquePoints();
        int starTrekPoints = starTrekCharacter.getUniquePoints();

        // Setear el nombre imagen y puntos unicos de los personajes a pelear.
        MainInterface.setStarWarsIcon(starWarsCharacter.getName(), starWarsCharacter.getUniquePoints());
        MainInterface.setStarTrekIcon(starTrekCharacter.getName(), starTrekCharacter.getUniquePoints());
        try {
            if (Admin.roundCounter < 1) {

            } else {
                MainInterface.AIState.setText("¡Peleando!");
            }
            Thread.sleep(1000 * time); //DECIDIENDO GANADOR
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result < WIN_PROBABILITY) {
            if (starWarsPoints > starTrekPoints) {
                handleWinnerStarWars(starWarsCharacter, starTrekCharacter, starWarsPoints, starTrekPoints);
                MainInterface.setCoronaPositionStarWars(MainInterface.SWImage);
                ganadoresStarWars++;
                MainInterface.marcadorStarWars(ganadoresStarWars);
            } else {
                handleWinnerStarTrek(starWarsCharacter, starTrekCharacter, starWarsPoints, starTrekPoints);
                MainInterface.setCoronaPositionStarTrek(MainInterface.STImage);
                ganadoresStarTrek++;
                MainInterface.marcadorStarTrek(ganadoresStarTrek);
            }
        } else if (result < WIN_PROBABILITY + DRAW_PROBABILITY) {
            handleDraw(starWarsCharacter, starTrekCharacter);
            MainInterface.setDrawVisible();
        } else {
            handleNoCombat(starWarsCharacter, starTrekCharacter);
            MainInterface.setCancelVisible();
        }
        // Después del combate, probabilidad del 40% de mover un personaje de refuerzo a la cola de prioridad 1
        if (Admin.shouldMoveToPriority1()) {
            Admin.moveCharacterToPriority(Admin.reinforcementQueueStarWars, Admin.starWarsQueue1);
        }

        if (Admin.shouldMoveToPriority1()) {
            Admin.moveCharacterToPriority(Admin.reinforcementQueueStarTrek, Admin.starTrekQueue1);
        }
        Admin.incrementRoundCounters(); // Incrementa los contadores de todas las colas
        Character.removeFromQueueN(Admin.starWarsQueue2, Admin.starWarsQueue3);
        Character.removeFromQueueB(Admin.starTrekQueue2, Admin.starTrekQueue3);
        //Generador de personajes (80% de probabilidad)
        Admin.roundCounter++;
        if (Admin.roundCounter % 2 == 0) { // Se han completado dos rondas
            if (Admin.shouldGenerateCharacters()) {
                Admin.generateCharacters();
                System.out.println("¡Se generaron dos personajes nuevos!");
            }
        }

        try {
            MainInterface.AIState.setText("Resultado");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainInterface.AIState.setText("Esperando...");
        MainInterface.coronaLabelSW.setVisible(false);
        MainInterface.coronaLabelST.setVisible(false);
        MainInterface.empateLabel.setVisible(false);
        MainInterface.cancelLabel.setVisible(false);
    }
    
    private void handleWinnerStarWars(Character winner, Character loser, int winnerPoints, int loserPoints) {
        System.out.println("¡Combate terminado! El ganador es: " + winner.getName()
                + " (ID: " + winner.getId() + ") con " + winnerPoints + " puntos únicos.");
        // Lógica para añadir a la lista de ganadores
        MainInterface.agregarGanador(winner.getName()+winner.getId());
    }
    
    private void handleWinnerStarTrek(Character loser, Character winner, int loserPoints, int winnerPoints) {
        System.out.println("¡Combate terminado! El ganador es: " + winner.getName()
                + " (ID: " + winner.getId() + ") con " + winnerPoints + " puntos únicos.");
        // Lógica para añadir a la lista de ganadores
        MainInterface.agregarGanador(winner.getName()+winner.getId());
    }
    
    private void handleDraw(Character character1, Character character2) {
        System.out.println("¡Combate empatado! Ambos personajes vuelven a la cola de prioridad 1.");
        // Lógica para regresarlos a la cola de prioridad 1
        Admin.starWarsQueue1.enqueue(character1);
        Admin.starTrekQueue1.enqueue(character2);
    }

    private void handleNoCombat(Character character1, Character character2) {
        System.out.println("No puede llevarse a cabo el combate. Ambos personajes van a la cola de refuerzo.");
        // Se agregan en la cola de refuerzo
        Admin.reinforcementQueueStarWars.enqueue(character1);
        Admin.reinforcementQueueStarTrek.enqueue(character2);
    }
}
