/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Character {

    private static int idCounter = 1;
    private int characterRoundCounter = 0;
    private int id;
    private String studio;
    private String name;
    private int levelPriority;
    private boolean exceptional;
    private int abilities;
    private int health;
    private int strength;
    private int agility;
    private int uniquePoints; // Puntos únicos según nivel de prioridad
    private Set<String> specialAbilities; // Habilidades especiales

    private static final String[] STAR_TREK_NAMES = {"Data", "GabrielLorca", "GeordiLaForge", "GulDukat", "JamesTKirk", "Jaylah", "Jean-LucPicard", "KhanNoonien", "Kirk", "Krall", "LeonardMccoy", "MontgomeryScott", "Nero", "NyotaUhura", "Q", "ReinaBorg", "Sarek", "SevenOfNine", "Shinzo","Spock", "Spock2", "Sulu", "TheDoctor", "Worf"};
    private static final String[] STAR_WARS_NAMES = {"AhsokaTano", "BB-8", "BobaFett", "CassianAndor", "Chewbacca", "DarthMaul", "DarthVader", "EmperadorPalpatine", "Han", "HanSolo", "JabbaHutt", "KyloRen", "LeiaOrgana", "LukeSkywalker", "Mace", "MaceWindu", "Mandalorian", "Obi-WanKenobi", "PadméAmidala","Qui-GonJinn", "R2-D2", "Yoda"};

    private Character(String name, String studio) {
        this.id = idCounter++;
        this.name = name;
        this.studio = studio;
        this.abilities = generateQuality(60);
        this.health = generateQuality(70);
        this.strength = generateQuality(50);
        this.agility = generateQuality(40);

        // Determina si el personaje es excepcional
        if (countQualityAttributes() >= 3) {
            this.exceptional = true;
            this.levelPriority = 1;
        } else {
            this.exceptional = false;
            // Calcula el nivel de prioridad utilizando un método simplificado
            if (determineNumSpecialAbilities() == 1) {
                this.levelPriority = 3; //Al solo tener 1 habilidad se setea como Deficiente (Prioridad 3)
            } else {
                this.levelPriority = 2; //Al tener 2 habilidades se setea como Promedio(Prioridad 2)
            }
        }

        this.uniquePoints = generateUniquePoints();
        this.specialAbilities = generateSpecialAbilities();
    }

    public static Character createStarTrekCharacter() {
        String name = STAR_TREK_NAMES[new Random().nextInt(STAR_TREK_NAMES.length)];
        return new Character(name, "N"); // El nivel de prioridad se establecerá dentro del constructor
    }

    public static Character createStarWarsCharacter() {
        String name = STAR_WARS_NAMES[new Random().nextInt(STAR_WARS_NAMES.length)];
        return new Character(name, "B"); // El nivel de prioridad se establecerá dentro del constructor
    }

    private int generateQuality(int baseProbability) {
        return new Random().nextInt(100) < baseProbability ? new Random().nextInt(100) + 1 : 0;
    }

    private int generateUniquePoints() {
        // Genera puntos únicos según nivel de prioridad
        switch (levelPriority) {
            case 1:
                return new Random().nextInt(20) + 80; // Rango de 80 a 100 para prioridad 1
            case 2:
                return new Random().nextInt(30) + 50; // Rango de 50 a 80 para prioridad 2
            case 3:
                return new Random().nextInt(40) + 10; // Rango de 10 a 50 para prioridad 3
            default:
                return 0;
        }
    }

    private Set<String> generateSpecialAbilities() {
        Set<String> abilities = new HashSet<>();
        int numAbilities = determineNumSpecialAbilities();

        for (int i = 0; i < numAbilities; i++) {
            abilities.add("SpecialAbility" + (i + 1));
        }

        return abilities;
    }

    private int determineNumSpecialAbilities() {
        return new Random().nextInt(2) + 1; // Devuelve 1 o 2 aleatoriamente (dependiendo de el numero  de habilidades será Promedio o Deficiente)
    }

    private int countQualityAttributes() {
        int count = 0;
        if (abilities > 0) {
            count++;
        }
        if (health > 0) {
            count++;
        }
        if (strength > 0) {
            count++;
        }
        if (agility > 0) {
            count++;
        }
        return count;
    }

    //Lógica para contador de rounds y cambio de Queue
    public void increaseRoundCounter() {

        characterRoundCounter++;
        if(characterRoundCounter == 8 & levelPriority != 1){
//            System.out.println("Promoción por " + characterRoundCounter + " combates para " + name + id);
        }
        if (characterRoundCounter == 8) {
            characterRoundCounter = 0; // Resetea el round counter
            if (levelPriority != 1) {

                increasePriorityAndQueue(this, Admin.starTrekQueue1, Admin.starTrekQueue2, Admin.starTrekQueue3, Admin.starWarsQueue1, Admin.starWarsQueue2, Admin.starWarsQueue3);
            }
        }
    }

// AQUI SE PASAN A LA COLA DE MAYOR PRIORIDAD (AUN NO SE BORRA EL ANTERIOR POR LO QUE QUEDAN DUPLICADOS)
    private static Map<String, Integer> charactersToRemoveN = new HashMap<>();
    private static Map<String, Integer> charactersToRemoveB = new HashMap<>();

    private void increasePriorityAndQueue(Character character, Queue<Character> starTrekQueue1, Queue<Character> starTrekQueue2, Queue<Character> starTrekQueue3, Queue<Character> starWarsQueue1, Queue<Character> starWarsQueue2, Queue<Character> starWarsQueue3) {
        String nombreId = name + id;
        int priorityToRemove = levelPriority;
        // Guardar los datos para su uso posterior

        if (studio == "N") {
            charactersToRemoveN.put(nombreId, priorityToRemove);
            levelPriority--;

            Admin.upgradeToQueue(character, starTrekQueue1, starTrekQueue2, starTrekQueue3);
//            System.out.println("SE SUBIO EL NIVEL DE PRIORIDAD DE "+character+"-------------------------");
        } else {
            charactersToRemoveB.put(nombreId, priorityToRemove);
            levelPriority--;
            Admin.upgradeToQueue(character, starWarsQueue1, starWarsQueue2, starWarsQueue3);

        }

    }
// Método para utilizar los datos guardados

    public static void removeFromQueueN(Queue<Character> starTrekQueue2, Queue<Character> starTrekQueue3) {
        for (Map.Entry<String, Integer> entry : charactersToRemoveN.entrySet()) {
            String characterId = entry.getKey();
            int priority = entry.getValue();

            // Realizar operaciones con las colas
            if (priority == 2) {
                starTrekQueue2.remove(characterId);
            } else {
                starTrekQueue3.remove(characterId);
            }
        }
    }
       public static void removeFromQueueB(Queue<Character> starWarsQueue2, Queue<Character> starWarsQueue3) {
        for (Map.Entry<String, Integer> entry : charactersToRemoveB.entrySet()) {
            String characterId = entry.getKey();
            int priority = entry.getValue();

            // Realizar operaciones con las colas
            if (priority == 2) {
                starWarsQueue2.remove(characterId);
            } else {
                starWarsQueue3.remove(characterId);
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isExceptional() {
        return exceptional;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getUniquePoints() {
        return uniquePoints;
    }

    public Set<String> getSpecialAbilities() {
        return specialAbilities;
    }

    public int getLevelPriority() {
        return levelPriority;
    }

    @Override
    public String toString() {
        return name + id;
    }
}
