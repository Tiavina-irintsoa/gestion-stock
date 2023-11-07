package modele;

import connexion.Connect;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Vector;

public class Mouvement {

  int idMouvement;
  Article article;
  Date dateMouvement;
  double quantite_entree;
  double quantite_sortie;
  Mouvement entreeCorrespondante;
  Mouvement sortieCorrespondante;
  Mouvement[] mouvementsResultants;
  Magasin magasin;
  double prixUnitaire;

  public double getPrixUnitaire() {
    return prixUnitaire;
  }

  public void setPrixUnitaire(double prixUnitaire) {
    this.prixUnitaire = prixUnitaire;
  }

  public static double getReste(Mouvement[] mouvement) {
    double s = 0;
    for (int i = 0; i < mouvement.length; i++) {
      s += mouvement[i].getQuantite_entree();
      s -= mouvement[i].getQuantite_sortie();
    }
    return s;
  }

  public Mouvement(Mouvement other) {
    this.idMouvement = other.idMouvement;
    this.article = other.article;
    this.dateMouvement = other.dateMouvement;
    this.quantite_entree = other.quantite_entree;
    this.quantite_sortie = other.quantite_sortie;
    this.entreeCorrespondante = other.entreeCorrespondante;
    this.sortieCorrespondante = other.sortieCorrespondante;
    this.prixUnitaire = other.prixUnitaire;
    if (other.mouvementsResultants != null) {
      this.mouvementsResultants =
        new Mouvement[other.mouvementsResultants.length];
      for (int i = 0; i < other.mouvementsResultants.length; i++) {
        this.mouvementsResultants[i] =
          new Mouvement(other.mouvementsResultants[i]);
      }
    }
    this.magasin = other.magasin;
  }

  public static Mouvement[] copyArray(Mouvement[] original) {
    if (original == null) {
      return null;
    }

    Mouvement[] copiedArray = new Mouvement[original.length];

    for (int i = 0; i < original.length; i++) {
      copiedArray[i] = new Mouvement(original[i]);
    }

    return copiedArray;
  }

  public Mouvement() {}

  public Mouvement(int idMouvement) {
    setIdMouvement(idMouvement);
  }

  public boolean estEntree() {
    return (quantite_sortie == 0);
  }

  public boolean estSortie() {
    return (quantite_entree == 0);
  }

  public Mouvement(
    int idMouvement,
    String idArticle,
    Date datemouvement,
    double quantite_entree,
    double quantite_sortie,
    int identree,
    int idsortie,
    int idmagasin,
    double prixUnitaire
  ) {
    setIdMouvement(idMouvement);
    setDateMouvement(datemouvement);
    setQuantite_entree(quantite_entree);
    setQuantite_sortie(quantite_sortie);
    setEntreeCorrespondante(identree);
    setSortieCorrespondante(idsortie);
    setMagasin(idmagasin);
    setArticle(idArticle);
    setPrixUnitaire(prixUnitaire);
  }

  public void setArticle(String idArticle) {
    setArticle(new Article(idArticle));
  }

  public void setMagasin(int idMagasin) {
    setMagasin(new Magasin(idMagasin));
  }

  public void setEntreeCorrespondante(int idEntree) {
    if (idEntree != 0) {
      setEntreeCorrespondante(new Mouvement(idEntree));
    }
  }

  public void setSortieCorrespondante(int idEntree) {
    if (idEntree != 0) {
      setSortieCorrespondante(new Mouvement(idEntree));
    }
  }

  public static String generateRechercheSql(
    Date dateinitial,
    Date dateFinal,
    Magasin magasin,
    Article article
  ) {
    String sql =
      "select * from v_mouvement where datemouvement > '" +
      dateinitial +
      "' and datemouvement <= '" +
      dateFinal +
      "' and idmagasin = " +
      magasin.idMagasin +
      " and idarticle ='" +
      article.getIdArticle() +
      "'";
    if (dateinitial == null) {
      sql =
        "select * from v_mouvement where datemouvement <= '" +
        dateFinal +
        "' and idmagasin = " +
        magasin.idMagasin +
        " and idarticle ='" +
        article.getIdArticle() +
        "'";
    } else if (dateFinal == null) {
      sql =
        "select * from v_mouvement where datemouvement > '" +
        dateinitial +
        "' and datemouvement <= now() and idmagasin = " +
        magasin.idMagasin +
        " and idarticle ='" +
        article.getIdArticle() +
        "'";
    }
    return sql;
  }

  public static Mouvement[] getMouvement(
    Date dateinitial,
    Date dateFinal,
    Magasin magasin,
    Article article,
    Connection connection
  ) throws Exception {
    Vector<Mouvement> mouvements = new Vector<Mouvement>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql = generateRechercheSql(dateinitial, dateFinal, magasin,article);
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      while (res.next()) {
        mouvements.add(
          new Mouvement(
            res.getInt("idmouvement"),
            res.getString("idarticle"),
            res.getDate("datemouvement"),
            res.getDouble("quantite_entree"),
            res.getDouble("quantite_sortie"),
            res.getInt("entree"),
            res.getInt("sortie"),
            res.getInt("idmagasin"),
            res.getDouble("prixunitaire")
          )
        );
      }
    } catch (Exception e) {
      throw e;
    } finally {
      stmt.close();
      if (opened) {
        connection.close();
      }
    }
    return mouvements.toArray(new Mouvement[mouvements.size()]);
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

  @Override
  public String toString() {
    return (
      "Mouvement [idMouvement=" +
      idMouvement +
      ", article=" +
      article +
      ", dateMouvement=" +
      dateMouvement +
      ", quantite_entree=" +
      quantite_entree +
      ", quantite_sortie=" +
      quantite_sortie +
      ", entreeCorrespondante=" +
      entreeCorrespondante +
      ", sortieCorrespondante=" +
      sortieCorrespondante +
      ", mouvementsResultants=" +
      Arrays.toString(mouvementsResultants) +
      ", magasin=" +
      magasin +
      "]"
    );
  }

  public Mouvement getSortieCorrespondante() {
    return sortieCorrespondante;
  }

  public void setSortieCorrespondante(Mouvement sortieCorrespondante) {
    this.sortieCorrespondante = sortieCorrespondante;
  }
}
