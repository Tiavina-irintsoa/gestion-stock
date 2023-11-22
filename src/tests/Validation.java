package tests;

import modele.Mouvement;

public class Validation {

  public static void main(String[] args) {
    try {
      Mouvement.validerSortie("25","2023-15-11");
    } catch (Exception e) {
        
      e.printStackTrace();
    }
  }
}
