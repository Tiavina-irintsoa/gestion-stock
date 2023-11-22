package modele;

import java.sql.Connection;
import java.sql.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Stock {

  Article article;
  Mouvement[] mouvementInitial;
  Mouvement[] mouvementAvantDate2;

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
    setmouvementAvantDate2(
      getmouvementAvantDate2(con, date1, date2, magasin, article)
    );
  }

  public double getReste() {
    return Mouvement.getReste(mouvementAvantDate2);
  }

  public double getMontant() {
    double montant = 0;
    for (int j = 0; j < mouvementAvantDate2.length; j++) {
      montant += mouvementAvantDate2[j].getPrixUnitaire() * mouvementAvantDate2[j].getReste();
    }
    return montant;
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

  public Mouvement[] getMouvementAvantDate1(
    Connection connection,
    Date date1,
    Magasin magasin,
    Article article
  ) throws Exception {
    return magasin.getStockMouvement(connection, article, date1);
  }

  public Mouvement[] getmouvementAvantDate2(
    Connection connection,
    Date date1,
    Date date2,
    Magasin magasin,
    Article article
  ) throws Exception {
    return magasin.getStockMouvement(connection, article, date2);
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

  public Mouvement[] getmouvementAvantDate2() {
    return mouvementAvantDate2;
  }

  public void setmouvementAvantDate2(Mouvement[] mouvementAvantDate2) {
    this.mouvementAvantDate2 = mouvementAvantDate2;
  }
}
