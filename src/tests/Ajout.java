package tests;

import modele.Mouvement;

public class Ajout {
    public static void main(String[] args) {
        try {
            // Mouvement.sortir("2022-10-10", "1", "R101", "80", null);
            // Mouvement.sortir("2022-10-09", "1", "R101", "80", null);
            Mouvement.sortir("2022-10-12", "1", "R101", "20", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
