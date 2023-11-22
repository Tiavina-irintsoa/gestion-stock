package modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import connexion.Connect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Article {

  String idArticle;
  String nomArticle;
  Unite unite;
  int methodeStockage;

  @JsonIgnore
  public String getOrderString() {
    if (methodeStockage == -1) {
      return "asc";
    }
    return "desc";
  }
  public static Article getArticleById(String id, Connection connection)
    throws Exception {
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql = "select * from v_article where idarticle = '" + id + "'";
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      if (res.next() == false) {
        return null;
      }
      return new Article(
        res.getString("idarticle"),
        res.getString("nomarticle"),
        res.getInt("methodeStockage"),
        res.getInt("idunite"),
        res.getString("nomunite"),
        res.getString("abreviation")
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
  public boolean exists( Connection con) throws Exception{
    Article article = getArticleById(this.getIdArticle(), con);
    if(article == null){
      return false;
    }
    setMethodeStockage(article.getMethodeStockage());
    setNomArticle(article.getNomArticle());
    return true;
  }
  @JsonIgnore
  public ObjectNode getObjectNode() {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode objectNode = objectMapper.createObjectNode();

    objectNode.put("idArticle", getIdArticle());
    objectNode.put("nomArticle", getNomArticle());
    objectNode.put("methodeStockage", getMethodeStockageString());
    objectNode.set("unite", getUnite().getObjectNode());
    return objectNode;
  }

  public String getMethodeStockageString() {
    if (methodeStockage == -1) {
      return "FIFO";
    }
    return "LIFO";
  }

  public Article() {}

  public Article(
    String idArticle,
    String nomArticle,
    int methodeStockage,
    int idUnite,
    String nomUnite,
    String abreviation
  ) {
    setIdArticle(idArticle);
    setNomArticle(nomArticle);
    setMethodeStockage(methodeStockage);
    setUnite(new Unite(idUnite, nomUnite, abreviation));
  }

  public Article(String idArticle) {
    this.idArticle = idArticle;
  }

  public String getIdArticle() {
    return idArticle;
  }

  public void setIdArticle(String idArticle) {
    this.idArticle = idArticle;
  }

  public String getNomArticle() {
    return nomArticle;
  }

  public void setNomArticle(String nomArticle) {
    this.nomArticle = nomArticle;
  }

  public Unite getUnite() {
    return unite;
  }

  public void setUnite(Unite unite) {
    this.unite = unite;
  }

  public int getMethodeStockage() {
    return methodeStockage;
  }

  public void setMethodeStockage(int methodeStockage) {
    this.methodeStockage = methodeStockage;
  }

  @Override
  public String toString() {
    return (
      "Article [idArticle=" +
      idArticle +
      ", nomArticle=" +
      nomArticle +
      ", unite=" +
      unite +
      ", methodeStockage=" +
      methodeStockage +
      "]"
    );
  }
}
