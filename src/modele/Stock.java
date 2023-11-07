package modele;

import java.sql.Connection;
import java.sql.Date;

public class Stock {

  Article article;
  Mouvement[] mouvementInitial;
  Mouvement[] mouvementEntreDate1Date2;
  Mouvement[] entreeReste;
  double montant;
  double reste;

  public Stock(Article article) {
    setArticle(article);
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
    setEntreeReste(getStockParEntree());
    setMontant(calculMontant());
    setReste(calculReste());
  }

  public double getResteEntre() {
    return Mouvement.getReste(mouvementEntreDate1Date2);
  }

  public double calculReste() {
    return getResteEntre() + getQuantiteInitial();
  }

  public double calculMontant() {
    double montant = 0;
    int i = 0;
    while (entreeReste[i] != null) {
      montant +=
        entreeReste[i].getQuantite_entree() * entreeReste[i].getPrixUnitaire();
      System.out.println(
        "montant +=" +
        entreeReste[i].getQuantite_entree() +
        "*" +
        entreeReste[i].getPrixUnitaire()
      );
      i++;
      if (i == entreeReste.length) {
        break;
      }
    }

    return montant;
  }

  public double getPrixUnitaire() {
    return getMontant() / getReste();
  }

  public double getQuantiteInitial() {
    return Mouvement.getReste(mouvementInitial);
  }

  public Mouvement[] getStockParEntree() {
    Mouvement[] entree = Mouvement.copyArray(mouvementInitial);
    for (int i = 0; i < entree.length; i++) {
      if (entree[i].estEntree()) {
        for (Mouvement sortie : mouvementEntreDate1Date2) {
          if (sortie.estSortie()) {
            if (
              entree[i].idMouvement ==
              sortie.getEntreeCorrespondante().getIdMouvement()
            ) {
              entree[i].setQuantite_entree(
                  entree[i].getQuantite_entree() - sortie.getQuantite_sortie()
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
          if (entree[i].getQuantite_entree() == 0) {
            break;
          }
        }
      }
    }
    return entree;
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

  public Mouvement[] getEntreeReste() {
    return entreeReste;
  }

  public void setEntreeReste(Mouvement[] entree) {
    this.entreeReste = entree;
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
