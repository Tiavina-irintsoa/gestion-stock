package modele;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import connexion.Connect;

public class Mouvement {

  int idMouvement;
  Article article;
  Date dateMouvement;
  double quantite_entree;
  double quantite_sortie;
  Mouvement entreeCorrespondante;
  Mouvement[] mouvementsResultants;
  Magasin magasin;
  double prixUnitaire;
  double reste;
  int etat;
  Mouvement[] stockMouvement;
  Date dateValidation;

  public Date getDateValidation() {
    return dateValidation;
  }

  public void setDateValidation(Date dateValidation) {
    this.dateValidation = dateValidation;
  }

  public boolean aMouvementUlterieur(Connection connection) throws Exception {
    Mouvement last = magasin.getLastMouvementValide(connection, article);
    if(last==null){
      return false;
    }
    // if (last.getDateMouvement().after(dateMouvement)) {
    //   return true;
    // }
    // else if(last.getDateMouvement().before(dateMouvement) && last.getDateValidation().after(getDateValidation())){
    //   System.out.println();
    //   return true;
    // }
    System.out.println("last:"+last.getDateValidation());
    System.out.println("now():"+getDateValidation());
    if(last.getDateValidation().compareTo(getDateMouvement())==0){
      return false;
    }
    if(last.getDateValidation().after(getDateMouvement())){
      return true;
    }
    return false;
  }
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

  public Mouvement(String id,String datevalidation) throws Exception {
    setIdMouvement(id);
    setDateValidation(datevalidation);
  }
  public void setDateValidation(String date) throws Exception{
    try {
      setDateValidation(Date.valueOf(date));
    } catch (Exception e) {
      throw new Exception("Date invalide");
    }
  }
  public static void validerSortie(String id,String date) throws Exception {
    Mouvement aValider = new Mouvement(id,date);
    // aValider.setDateValidation(date);
    aValider.valider(null);
  }

  public boolean getById(Connection connection) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select * from mouvement where idmouvement = " + getIdMouvement();
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      if (res.next() == false) {
        return false;
      }
      setArticle(res.getString("idarticle"));
      setMagasin(res.getString("idmagasin"));
      setQuantite_entree(res.getDouble("quantite_entree"));
      setQuantite_sortie(res.getDouble("quantite_sortie"));
      setEtat(res.getInt("etat"));
      setDateMouvement(res.getDate("datemouvement"));
      setArticle(
        Article.getArticleById(getArticle().getIdArticle(), connection)
      );
      setMagasin(
        Magasin.getMagasinById(getMagasin().getIdMagasin(), connection)
      );
      return true;
    } catch (Exception e) {
      throw e;
    } finally {
      stmt.close();
      if (opened) {
        connection.close();
      }
    }
  }

  public void setIdMouvement(String idMouvement) throws Exception {
    if (idMouvement.length() == 0) {
      throw new Exception("Mouvement invalide");
    }
    setIdMouvement(Integer.valueOf(idMouvement));
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

  public void insertResultant(Connection connection) throws Exception {
    for (Mouvement mouvement : mouvementsResultants) {
      mouvement.insert(connection, true);
    }
  }

  public void valider(Connection connection) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    try {

      this.controle(connection);
      this.completeData(connection);
      this.insertResultant(connection);
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

  public void sortir(Connection connection) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    try {
      this.insert(connection, false);
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

  public Mouvement(double quantite_sortie, Mouvement entree,Date dv,Date datemouvement) throws Exception {
    setQuantite_sortie(quantite_sortie);
    setEntreeCorrespondante(entree);
    setArticle(entree.getArticle());
    setDateMouvement(datemouvement);
    setMagasin(entree.getMagasin());
    setDateValidation(dv);
  }

  public void completeData(Connection con) throws Exception {
    setMouvementsResultants(decomposer(con));
  }

  public PreparedStatement getPreparedStatement(
    Connection connection,
    boolean valide
  ) throws Exception {
    String sql =
      "insert into mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, idmagasin,prixUnitaire,etat) values (?,?,?,?,?,?,?,?) returning idmouvement";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, getArticle().getIdArticle());
    System.out.println("date insere : "+getDateMouvement());
    pstmt.setDate(2, getDateMouvement());
    pstmt.setDouble(3, getQuantite_entree());
    pstmt.setDouble(4, getQuantite_sortie());
    if (entreeCorrespondante != null) {
      pstmt.setInt(5, getEntreeCorrespondante().getIdMouvement());
    } else {
      pstmt.setNull(5, java.sql.Types.INTEGER);
    }

    pstmt.setInt(6, getMagasin().getIdMagasin());
    pstmt.setDouble(7, getPrixUnitaire());
    if (valide) {
      pstmt.setInt(8, 10);
    } else {
      pstmt.setInt(8, 0);
    }
    return pstmt;
  }

  public void ajoutValidation(Connection connection) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql = "insert into validation values (?,?)";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    try {
      pstmt.setInt(1, this.getIdMouvement());
      pstmt.setDate(2,this.getDateValidation());
      pstmt.executeUpdate();
      if (opened) {
        connection.commit();
      }
    } catch (Exception e) {
      if (opened) {
        connection.rollback();
      }
      throw e;
    } finally {
      pstmt.close();
      if (opened) {
        connection.close();
      }
    }
  }

  public void insert(Connection connection, boolean valide) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    PreparedStatement pstmt = null;
    try {
      pstmt = getPreparedStatement(connection, valide);
      ResultSet res = pstmt.executeQuery();
      res.next();
      setIdMouvement(res.getInt("idmouvement"));
      if (valide) {
        this.ajoutValidation(connection);
      }
      if (opened) {
        connection.commit();
      }
    } catch (Exception e) {
      if (opened) {
        connection.rollback();
      }
      throw e;
    } finally {
      pstmt.close();
      if (opened) {
        connection.close();
      }
    }
  }

  public Mouvement[] decomposer(Connection con) throws Exception {
    Vector<Mouvement> resultants = new Vector<Mouvement>();
    double sortietemp = getQuantite_sortie();
    double a_sortir = 0;
    for (Mouvement mouvement : this.getStockMouvement()) {
      if(mouvement.estEntree() && mouvement.getReste()>0){
        if (sortietemp > mouvement.getReste()) {
        a_sortir = mouvement.getReste();
        } else {
          a_sortir = sortietemp;
        }
        resultants.add(new Mouvement(a_sortir, mouvement,this.getDateValidation(),this.getDateMouvement()));
        sortietemp = sortietemp - a_sortir;
        if (sortietemp == 0) {
          break;
        }
      }
    }
    return resultants.toArray(new Mouvement[resultants.size()]);
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

  public static double getStock(Mouvement[] mouvement) {
    double s = 0;
    for (int i = 0; i < mouvement.length; i++) {
      s += mouvement[i].getQuantite_entree();
      s -= mouvement[i].getQuantite_sortie();
    }
    return s;
  }
  public static double getReste(Mouvement[] mouvement){
     double s = 0;
    for (int i = 0; i < mouvement.length; i++) {
      s += mouvement[i].getReste();
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
    if (this.getById(connection) == false) {
      throw new Exception("Mouvement ineistant");
    }
    if(this.getEtat()==10){
      throw new Exception("Mouvement deja valide");
    }
    if (article.exists(connection) == false) {
      throw new Exception("Produit invalide");
    }
    if (magasin.exists(connection) == false) {
      throw new Exception("Magasin invalide");
    }
    if (aMouvementUlterieur(connection) == true) {
      throw new Exception("Ce mouvement a un mouvement ulterieur valide");
    }
    setStockMouvement(
      magasin.getStockMouvement(connection, article, dateMouvement)
    );
    if (verifierStock(connection) == false) {
      throw new Exception("Stock insufisant");
    }
  }

  public boolean verifierStock(Connection con) throws Exception {
    double stock = Mouvement.getStock(getStockMouvement());
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
  ) throws Exception {
    setIdMouvement(idMouvement);
    setDateMouvement(datemouvement);
    setQuantite_entree(quantite_entree);
    setQuantite_sortie(quantite_sortie);
    setEntreeCorrespondante(identree);
    setMagasin(idmagasin);
    setArticle(idArticle);
    setPrixUnitaire(prixUnitaire);
    setReste(getQuantite_entree());
  }

  public Mouvement(
    int idMouvement,
    String idArticle,
    Date datemouvement,
    double quantite_entree,
    double quantite_sortie,
    int identree,
    int idmagasin,
    double prixUnitaire,
    double reste,
    int etat,
    Date datevalidation
  ) throws Exception {
    setIdMouvement(idMouvement);
    setDateMouvement(datemouvement);
    setQuantite_entree(quantite_entree);
    setQuantite_sortie(quantite_sortie);
    setEntreeCorrespondante(identree);
    setMagasin(idmagasin);
    setArticle(idArticle);
    setPrixUnitaire(prixUnitaire);
    setReste(reste);
    setEtat(etat);
    setDateValidation(datevalidation);
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

  public void setQuantite_entree(double quantite_entree) throws Exception {
    if (quantite_entree < 0) {
      throw new Exception("Quantite invalide");
    }
    this.quantite_entree = quantite_entree;
  }

  public double getQuantite_sortie() {
    return quantite_sortie;
  }

  public void setQuantite_sortie(double quantite_sortie) throws Exception {
    if (quantite_sortie < 0) {
      throw new Exception("Quantite invalide");
    }
    this.quantite_sortie = quantite_sortie;
  }

  @JsonIgnore
  public Mouvement getEntreeCorrespondante() {
    return entreeCorrespondante;
  }

  public void setEntreeCorrespondante(Mouvement entreeCorrespondante) {
    this.entreeCorrespondante = entreeCorrespondante;
  }

  @JsonIgnore
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

  public String getEtatString() {
    if (etat == 0) {
      return "Non valide";
    }
    return "Valide";
  }

  @JsonIgnore
  public static String getJsonById(String id) throws Exception {
    Mouvement m = new Mouvement(id);
    m.getById(null);
    return m.getObjectNode().toPrettyString();
  }

  public Mouvement(String id) throws Exception{
    setIdMouvement(id);
  }
  @JsonIgnore
  public ObjectNode getObjectNode() {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode mvt = objectMapper.createObjectNode();
    mvt.set("Article", article.getObjectNode());
    mvt.set("Magasin", magasin.getObjectNode());

    mvt.put("dateMouvement", getDateMouvement().toString());
    mvt.put("idmouvement", getIdMouvement());
    mvt.put("quantite_entree", getQuantite_entree());
    mvt.put("quantite_sortie", getQuantite_sortie());
    mvt.put("prixunitaire", getPrixUnitaire());
    mvt.put("etatString", getEtatString());
    mvt.put("etat", getEtat());

    return mvt;
  }

  @JsonIgnore
  public static String getJsonAll() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    List<Mouvement> all = getAll(null);
    ArrayNode arrayNode = objectMapper.createArrayNode();
    for (Mouvement mouvement : all) {
      arrayNode.add(mouvement.getObjectNode());
    }
    return arrayNode.toString();
  }

  public static Vector<Mouvement> getAll(Connection connection)
    throws Exception {
    Vector<Mouvement> liste = new Vector<Mouvement>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select * from v_mouvement order by datemouvement desc, idarticle asc, idmagasin asc ";
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      Mouvement m = null;
      while (res.next()) {
        m =
          new Mouvement(
            res.getInt("idmouvement"),
            res.getString("idarticle"),
            res.getDate("datemouvement"),
            res.getDouble("quantite_entree"),
            res.getDouble("quantite_sortie"),
            res.getInt("entree"),
            res.getInt("idmagasin"),
            res.getDouble("prixunitaire"),
            0,
            res.getInt("etat"),
            res.getDate("datevalidation")
          );
        m.setArticle(
          Article.getArticleById(m.getArticle().getIdArticle(), connection)
        );
        m.setMagasin(
          Magasin.getMagasinById(m.getMagasin().getIdMagasin(), connection)
        );
        liste.add(m);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      stmt.close();
      if (opened) {
        connection.close();
      }
    }
    return liste;
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
      ", mouvementsResultants=" +
      Arrays.toString(mouvementsResultants) +
      ", magasin=" +
      magasin +
      "]"
    );
  }

  public Mouvement[] getStockMouvement() {
    return stockMouvement;
  }

  public void setStockMouvement(Mouvement[] stockMouvement) {
    this.stockMouvement = stockMouvement;
  }

  public int getEtat() {
    return etat;
  }

  public void setEtat(int etat) {
    this.etat = etat;
  }
}
