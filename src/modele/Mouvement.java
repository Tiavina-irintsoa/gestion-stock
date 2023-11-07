package modele;

import connexion.Connect;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Vector;

public class Mouvement {

  int idMouvement;
  Article article;
  Date dateMouvement;
  double quantite_entree;
  double quantite_sortie;
  Mouvement entreeCorrespondante;
  Mouvement sortieCorreponsante;
  Mouvement[] mouvementsResultants;
  Magasin magasin;

  public static String generateRechercheSql(
    Date dateinitial,
    Date dateFinal,
    Magasin magasin
  ) {
    String sql =
      "select * from mouvement where datemouvement > '" +
      "dateinitial +" +
      "' and datemouvement <= ' +" +
      "dateFinal +" +
      "' and idmagasin = ?";
      if(dateinitial == null){
        sql = "select * from mouvement where datemouvement <= "+dateFinal;
      }
      if(dateFinal == null){
        sql = "";
      }
      return sql;
  }

  public static Mouvement[] getMouvement(
    Date dateinitial,
    Date dateFinal,
    Magasin magasin,
    Connection connection
  ) throws Exception {
    Vector<Mouvement> mouvement = new Vector<Mouvement>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select * from mouvement where datemouvement > '" +
      dateinitial +
      "' and datemouvement <= '" +
      dateFinal +
      "' and idmagasin = ?";
    if (dateinitial == null) {
      sql =
        "select * from mouvement where datemouvement <= ? and idmagasin = ? ";
    }
  }

  public int getIdMouvement() {
    return idMouvement;
  }

  public void setIdMouvement(int idMouvement) {
    this.idMouvement = idMouvement;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public Date getDateMouvement() {
    return dateMouvement;
  }

  public void setDateMouvement(Date dateMouvement) {
    this.dateMouvement = dateMouvement;
  }

  public double getQuantite_entree() {
    return quantite_entree;
  }

  public void setQuantite_entree(double quantite_entree) {
    this.quantite_entree = quantite_entree;
  }

  public double getQuantite_sortie() {
    return quantite_sortie;
  }

  public void setQuantite_sortie(double quantite_sortie) {
    this.quantite_sortie = quantite_sortie;
  }

  public Mouvement getEntreeCorrespondante() {
    return entreeCorrespondante;
  }

  public void setEntreeCorrespondante(Mouvement entreeCorrespondante) {
    this.entreeCorrespondante = entreeCorrespondante;
  }

  public Mouvement getSortieCorreponsante() {
    return sortieCorreponsante;
  }

  public void setSortieCorreponsante(Mouvement sortieCorreponsante) {
    this.sortieCorreponsante = sortieCorreponsante;
  }

  public Mouvement[] getMouvementsResultants() {
    return mouvementsResultants;
  }

  public void setMouvementsResultants(Mouvement[] mouvementsResultants) {
    this.mouvementsResultants = mouvementsResultants;
  }

  public Magasin getMagasin() {
    return magasin;
  }

  public void setMagasin(Magasin magasin) {
    this.magasin = magasin;
  }
}
