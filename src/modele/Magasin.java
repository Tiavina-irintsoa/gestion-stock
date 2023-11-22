package modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import connexion.Connect;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class Magasin {

  int idMagasin;
  String nomMagasin;

  public Magasin(String id) {
    setIdMagasin(id);
  }

  public EtatStock getEtatStock(String date1, String date2, String idarticle)
    throws Exception {
    EtatStock etatStock = new EtatStock(date1, date2, idarticle, this);
    etatStock.setListeStock(etatStock.listeStock());
    return etatStock;
  }

  public Mouvement getLastMouvementValide(
    Connection connection,
    Article article
  ) throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select * from getLastMouvement('" +
      article.getIdArticle() +
      "'," +
      this.getIdMagasin() +
      ")";
    System.out.println(sql);
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      if(res.next()==false){
        return null;
      }
      
      return new Mouvement(
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
    } catch (Exception e) {
      throw e;
    } finally {
      stmt.close();
      if (opened) {
        connection.close();
      }
    }
  }

  public Mouvement[] getStockMouvement(
    Connection connection,
    Article article,
    Date datebefore
  ) throws Exception {
    Vector<Mouvement> mouvements = new Vector<Mouvement>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select * from getEtatStockMouvement('" +
      datebefore +
      "'," +
      this.getIdMagasin() +
      ",'" +
      article.getIdArticle() +
      "','" +
      article.getOrderString() +
      "')";
    System.out.println(sql);
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      double entree = 0;
      double utilise = 0;
      while (res.next()) {
        entree = res.getDouble("quantite_entree");
        utilise = res.getDouble("utilise");
        mouvements.add(
          new Mouvement(
            res.getInt("idmouvement"),
            res.getString("idarticle"),
            res.getDate("datemouvement"),
            res.getDouble("quantite_entree"),
            res.getDouble("quantite_sortie"),
            res.getInt("entree"),
            res.getInt("idmagasin"),
            res.getDouble("prixunitaire"),
            entree - utilise,
            res.getInt("etat"),
            res.getDate("datevalidation")
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

  public static String getJsonAll() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    String json = objectMapper.writeValueAsString(getAll(null));
    return json;
  }

  public static Magasin[] getAll(Connection connection) throws Exception {
    Vector<Magasin> liste = new Vector<Magasin>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql = "select * from magasin";
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      while (res.next()) {
        liste.add(
          new Magasin(res.getInt("idmagasin"), res.getString("nommagasin"))
        );
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (opened) {
        connection.close();
      }
    }
    return liste.toArray(new Magasin[liste.size()]);
  }

  public boolean exists(Connection con) throws Exception {
    Magasin magasin = getMagasinById(idMagasin, con);

    if (magasin == null) {
      return false;
    }
    setNomMagasin(magasin.getNomMagasin());
    return true;
  }

  public static Magasin getMagasinById(int id, Connection connection)
    throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql = "select * from magasin where idmagasin = " + id;
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      if (res.next() == false) {
        return null;
      }
      return new Magasin(res.getInt("idmagasin"), res.getString("nomMagasin"));
    } catch (Exception e) {
      throw e;
    } finally {
      stmt.close();
      if (opened) {
        connection.close();
      }
    }
  }

  public Magasin(int idMagasin, String nomMagasin) {
    this.idMagasin = idMagasin;
    this.nomMagasin = nomMagasin;
  }

  @JsonIgnore
  public ObjectNode getObjectNode() {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode objectNode = objectMapper.createObjectNode();

    objectNode.put("idMagasin", getIdMagasin());
    objectNode.put("nomMagasin", getNomMagasin());
    return objectNode;
  }

  public Magasin(int idMagasin) {
    this.idMagasin = idMagasin;
  }

  public Magasin() {}

  public int getIdMagasin() {
    return idMagasin;
  }

  public void setIdMagasin(int idMagasin) {
    this.idMagasin = idMagasin;
  }

  public String getNomMagasin() {
    return nomMagasin;
  }

  public void setIdMagasin(String idmagasin) {
    setIdMagasin(Integer.valueOf(idmagasin));
  }

  public void setNomMagasin(String nomMagasin) {
    this.nomMagasin = nomMagasin;
  }

  @Override
  public String toString() {
    return (
      "Magasin [idMagasin=" + idMagasin + ", nomMagasin=" + nomMagasin + "]"
    );
  }
}
