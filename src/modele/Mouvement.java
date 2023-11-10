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
  double reste;

  public static void sortir(
    String date,
    String mag,
    String idarticle,
    String quantite,
    Connection con
  ) throws Exception {
    Mouvement mouvement = new Mouvement(date, mag, idarticle, quantite);
    mouvement.sortir(con);
  }

  public Mouvement(String date, String mag, String idarticle, String quantite)
    throws Exception {
    setDateMouvement(date);
    setMagasin(mag);
    setArticle(idarticle);
    setQuantite_sortie(quantite);
  }

  public Mouvement(int id, double reste) {
    setIdMouvement(id);
    setReste(reste);
  }

  public void setQuantite_sortie(String quantite) throws Exception {
    try {
      setQuantite_sortie(Double.valueOf(quantite));
    } catch (Exception e) {
      throw new Exception("Quantite invalide");
    }
  }

  public void setDateMouvement(String date) throws Exception {
    try {
      setDateMouvement(Date.valueOf(date));
    } catch (Exception e) {
      throw new Exception("Date invalide");
    }
  }

  public void setMagasin(String idmagasin) {
    setMagasin(Integer.valueOf(idmagasin));
  }

  public void sortir(Connection connection) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    try {
      controle(connection);
      completeData(connection);
      this.insert(connection);
      for (Mouvement mouvement : mouvementsResultants) {
        System.out.println(mouvement.getIdMouvement());
        mouvement.insert(connection);
      }
      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      throw e;
    } finally {
      if (opened) {
        connection.close();
      }
    }
  }

  public Mouvement(double quantite_sortie, Mouvement entree, Mouvement sortie) {
    setQuantite_sortie(quantite_sortie);
    setEntreeCorrespondante(entree);
    setSortieCorrespondante(sortie);
    setArticle(sortie.getArticle());
    setDateMouvement(sortie.getDateMouvement());
    setMagasin(sortie.getMagasin());
  }

  public void completeData(Connection con) throws Exception {
    setMouvementsResultants(getMouvementResultants(con));
  }

  public void insert(Connection connection) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "insert into mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, sortie, idmagasin,prixUnitaire) values (?,?,?,?,?,?,?,?) returning idmouvement";

    PreparedStatement pstmt = connection.prepareStatement(sql);
    try {
      pstmt.setString(1, getArticle().getIdArticle());
      pstmt.setDate(2, getDateMouvement());
      pstmt.setDouble(3, getQuantite_entree());
      pstmt.setDouble(4, getQuantite_sortie());
      if (entreeCorrespondante != null) {
        pstmt.setInt(5, getEntreeCorrespondante().getIdMouvement());
      } else {
        pstmt.setNull(5, java.sql.Types.INTEGER);
      }
      if (sortieCorrespondante != null) {
        pstmt.setInt(6, getSortieCorrespondante().getIdMouvement());
      }
      else{
        pstmt.setNull(6, java.sql.Types.INTEGER);
      }
      pstmt.setInt(7, getMagasin().getIdMagasin());
      pstmt.setDouble(8, getPrixUnitaire());
      ResultSet res = pstmt.executeQuery();
      res.next();
      setIdMouvement(res.getInt("idmouvement"));
    } catch (Exception e) {
      throw e;
    } finally {
      pstmt.close();
      if (opened) {
        connection.close();
      }
    }
  }

  public Mouvement[] getMouvementResultants(Connection con) throws Exception {
    Vector<Mouvement> resultants = new Vector<Mouvement>();
    Mouvement[] avant = getEntreeAvant(con);
    double sortietemp = getQuantite_sortie();
    double a_sortir = 0;
    for (Mouvement mouvement : avant) {
      System.out.println("entree avant");
      if (sortietemp > mouvement.getReste()) {
        a_sortir = mouvement.getReste();
      } else {
        a_sortir = sortietemp;
      }
      resultants.add(new Mouvement(a_sortir, mouvement, this));
      sortietemp = sortietemp - a_sortir;
    }
    return resultants.toArray(new Mouvement[resultants.size()]);
  }

  public Mouvement[] getEntreeAvant(Connection connection) throws Exception {
    Vector<Mouvement> mouvements = new Vector<Mouvement>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select v_entree.idmouvement, v_entree.quantite_entree - sum(v_sortie.quantite_sortie) as reste from v_entree join v_sortie on v_sortie.entree = v_entree.idmouvement where v_sortie.dateMouvement < '" +
      dateMouvement +
      "' and v_entree.idMagasin =" +
      this.magasin.getIdMagasin() +
      " and v_entree.idarticle = '" +
      article.getIdArticle() +
      "' group by v_entree.idmouvement, v_entree.quantite_entree, v_entree.datemouvement order by v_entree.datemouvement  " +
      article.getOrderString();
    System.out.println(sql);
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      while (res.next()) {
        if (res.getDouble("reste") > 0) {
          mouvements.add(
            new Mouvement(res.getInt("idmouvement"), res.getDouble("reste"))
          );
        }
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

  public double getReste() {
    return reste;
  }

  public void setReste(double reste) {
    this.reste = reste;
  }

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
    this.reste = other.reste;
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

  public void controle(Connection connection) throws Exception {
    if (article.exists(connection) == false) {
      throw new Exception("Produit invalide");
    }
    if (magasin.exists(connection) == false) {
      throw new Exception("Magasin invalide");
    }
    if (verifierStock(connection) == false) {
      throw new Exception("Stock insufisant");
    }
  }

  public boolean verifierStock(Connection con) throws Exception {
    Mouvement[] avant = Mouvement.getMouvement(
      null,
      dateMouvement,
      magasin,
      article,
      con
    );
    double stock = Mouvement.getReste(avant);
    if (stock >= quantite_sortie) {
      return true;
    }
    return false;
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
    setReste(getQuantite_entree());
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
    if (dateFinal == null & dateinitial == null) {
      sql =
        "select * from v_mouvement where idmagasin = " +
        magasin.idMagasin +
        " and idarticle ='" +
        article.getIdArticle() +
        "'";
    } else if (dateinitial == null && dateFinal != null) {
      sql =
        "select * from v_mouvement where datemouvement <= '" +
        dateFinal +
        "' and idmagasin = " +
        magasin.idMagasin +
        " and idarticle ='" +
        article.getIdArticle() +
        "'";
    } else if (dateFinal == null && dateinitial != null) {
      sql =
        "select * from v_mouvement where datemouvement > '" +
        dateinitial +
        "' and datemouvement <= now() and idmagasin = " +
        magasin.idMagasin +
        " and idarticle ='" +
        article.getIdArticle() +
        "'";
    }

    System.out.println(sql);
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
    String sql = generateRechercheSql(dateinitial, dateFinal, magasin, article);
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
