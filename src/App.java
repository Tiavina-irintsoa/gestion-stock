import java.sql.Date;
import modele.EtatStock;
import modele.Magasin;
import modele.Mouvement;
import modele.Stock;

public class App {

  public static void main(String[] args) throws Exception {
    EtatStock e = EtatStock.getEtatStock(
      "2023-08-08",
      "2023-11-06",
      "R101",
      "1"
    );
    System.out.println(e.getListeStock().length);
    for (Stock s : e.getListeStock()) {

      System.out.println(s);
    }
    System.out.println(e);
  }
}
