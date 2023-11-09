package modele;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.Connection;
import java.sql.Date;
import java.util.Vector;

public class Stock {

  Article article;
  Mouvement[] mouvementInitial;
  Mouvement[] mouvementEntreDate1Date2;
  double montant;
  double reste;

  public Stock(Article article) {
    setArticle(article);
  }

  public ObjectNode getJSON() {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode stockObject = objectMapper.createObjectNode();
    stockObject.set("Article", article.getObjectNode());
    stockObject.put("quantiteInitiale", getQuantiteInitial());
    stockObject.put("reste", getReste());
    stockObject.put("prixUnitaire", getPrixUnitaire());
    stockObject.put("montant", getMontant());
    return stockObject;
  }

  public void completeData(
    Date date1,
    Date date2,
    Magasin magasin,
    Article article,
    Connection con
  ) throws Exception {
    setMouvementInitial(getMouvementAvantDate1(con, date1, magasin, article));
    setMouvementEntreDate1Date2(
      getMouvementEntreDate1Date2(con, date1, date2, magasin, article)
    );
    calculResteEntree();
    setMontant(calculMontant());
    setReste(calculReste());
    for (Mouvement mouvement : mouvementInitial) {
      System.out.println(mouvement);
    }
    for (Mouvement mouvement : mouvementEntreDate1Date2) {
      System.out.println(mouvement);
    }
  }

  public double getResteEntre() {
    return Mouvement.getReste(mouvementEntreDate1Date2);
  }

  public double calculReste() {
    return getResteEntre() + getQuantiteInitial();
  }

  public double calculMontant() {
    return (
      calculMontant(mouvementInitial) + calculMontant(mouvementEntreDate1Date2)
    );
  }

  public double calculMontant(Mouvement[] mouvements) {
    try {
      double montant = 0;
      int i = 0;
      while (mouvements[i] != null) {
        montant += mouvements[i].getReste() * mouvements[i].getPrixUnitaire();
        i++;
        if (i == mouvements.length) {
          break;
        }
      }
      return montant;
    } catch (ArrayIndexOutOfBoundsException e) {
      return 0;
    }
  }

  public double getPrixUnitaire() {
    if (getMontant() == 0 || getReste() == 0) {
      return 0;
    }
    return getMontant() / getReste();
  }

  public double getQuantiteInitial() {
    return Mouvement.getReste(mouvementInitial);
  }

  public Mouvement[] getEntrees() {
    Vector<Mouvement> entree = new Vector<Mouvement>();
    for (int i = 0; i < mouvementInitial.length; i++) {
      if (mouvementInitial[i].estEntree()) {
        entree.add(new Mouvement(mouvementInitial[i]));
      }
    }
    for (int i = 0; i < mouvementEntreDate1Date2.length; i++) {
      if (mouvementEntreDate1Date2[i].estEntree()) {
        entree.add(new Mouvement(mouvementEntreDate1Date2[i]));
      }
    }
    return entree.toArray(new Mouvement[entree.size()]);
  }

  public void calculResteEntree() {
    getResteParEntree(mouvementInitial);
    getResteParEntree(mouvementEntreDate1Date2);
  }

  public void getResteParEntree(Mouvement[] entree) {
    for (int i = 0; i < entree.length; i++) {
      if (entree[i].estEntree()) {
        for (Mouvement sortie : mouvementEntreDate1Date2) {
          if (sortie.estSortie()) {
            if (
              entree[i].getIdMouvement() ==
              sortie.getEntreeCorrespondante().getIdMouvement()
            ) {
              entree[i].setReste(
                  entree[i].getReste() - sortie.getQuantite_sortie()
                );
            }
            System.out.println(
              entree[i].getIdMouvement() +
              ": " +
              entree[i].getQuantite_entree() +
              " apres idmouvement =" +
              sortie.getIdMouvement()
            );
          }
          if (entree[i].getReste() == 0) {
            break;
          }
        }
      }
    }
  }

  public Mouvement[] getMouvementAvantDate1(
    Connection connection,
    Date date1,
    Magasin magasin,
    Article article
  ) throws Exception {
    return Mouvement.getMouvement(null, date1, magasin, article, connection);
  }

  public Mouvement[] getMouvementEntreDate1Date2(
    Connection connection,
    Date date1,
    Date date2,
    Magasin magasin,
    Article article
  ) throws Exception {
    return Mouvement.getMouvement(date1, date2, magasin, article, connection);
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public Mouvement[] getMouvementInitial() {
    return mouvementInitial;
  }

  public void setMouvementInitial(Mouvement[] mouvementInitial) {
    this.mouvementInitial = mouvementInitial;
  }

  public Mouvement[] getMouvementEntreDate1Date2() {
    return mouvementEntreDate1Date2;
  }

  public void setMouvementEntreDate1Date2(
    Mouvement[] mouvementEntreDate1Date2
  ) {
    this.mouvementEntreDate1Date2 = mouvementEntreDate1Date2;
  }

  public double getMontant() {
    return montant;
  }

  public void setMontant(double montant) {
    this.montant = montant;
  }

  public double getReste() {
    return reste;
  }

  public void setReste(double reste) {
    this.reste = reste;
  }

  @Override
  public String toString() {
    return (
      "Stock [article=" +
      article +
      ", montant=" +
      String.valueOf(montant) +
      ", reste=" +
      reste +
      ", pu = " +
      getPrixUnitaire() +
      " , quantiteinitiale  = " +
      getQuantiteInitial() +
      "]"
    );
  }
}
