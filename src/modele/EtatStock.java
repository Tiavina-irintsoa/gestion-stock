package modele;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import connexion.Connect;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Vector;

public class EtatStock {

  Date dateInitial;
  Date dateFinal;
  Article article;
  Stock[] listeStock;
  Magasin magasin;

  public String getJSON() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("dateInitiale", dateInitial.toString());
    objectNode.put("dateFinale", dateFinal.toString());
    objectNode.set("magasin", magasin.getObjectNode());
    ArrayNode arrayNode = objectMapper.createArrayNode();
    for (Stock stock : listeStock) {
      arrayNode.add(stock.getJSON());
    }
    objectNode.set("listeStock",arrayNode);
    objectNode.put("montant",getMontantTotal());
    System.out.println(objectNode.toString());
    return objectNode.toString();
  }

  public EtatStock(
    String dateInitial,
    String dateFinal,
    String idArticle ,Magasin magasin ) throws Exception {
    setDateInitial(dateInitial);
    setDateFinal(dateFinal);
    setArticle(idArticle);
  }

  public Stock[] listeStock() throws Exception {
    Vector<Stock> stocks = new Vector<Stock>();
    Connect connect = new Connect();
    Connection connection = connect.getConnectionPostgresql();
    try {
      magasin = Magasin.getMagasinById(this.magasin.getIdMagasin(), connection);
      Article[] articles = getProduitsConcernes(connection);
      for (int i = 0; i < articles.length; i++) {
        Stock stock = new Stock(articles[i]);
        stock.completeData(
          dateInitial,
          dateFinal,
          magasin,
          articles[i],
          connection
        );
        stocks.add(stock);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      connection.close();
    }
    return stocks.toArray(new Stock[stocks.size()]);
  }

  public Article[] getProduitsConcernes(Connection connection)
    throws Exception {
    Vector<Article> articles = new Vector<Article>();
    boolean opened = false;
    if (connection == null) {
      Connect c = new Connect();
      connection = c.getConnectionPostgresql();
      opened = true;
    }
    String sql =
      "select * from v_article where idArticle like '" +
      article.getIdArticle() +
      "%'";
    Statement stmt = connection.createStatement();
    try {
      ResultSet res = stmt.executeQuery(sql);
      while (res.next()) {
        articles.add(
          new Article(
            res.getString("idarticle"),
            res.getString("nomarticle"),
            res.getInt("methodeStockage"),
            res.getInt("idunite"),
            res.getString("nomunite"),
            res.getString("abreviation")
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
    return articles.toArray(new Article[articles.size()]);
  }

  public void setDateInitial(String dateInitial) throws Exception {
    try {
      setDateInitial(Date.valueOf(dateInitial));
    } catch (Exception e) {
      throw new Exception("Date initiale invalide");
    }
  }

  public void setDateFinal(String dateFinal) throws Exception {
    try {
      setDateFinal(Date.valueOf(dateFinal));
    } catch (Exception e) {
      throw new Exception("Date finale invalide");
    }
  }

  public void setArticle(String id) {
    setArticle(new Article(id));
  }

  public void setMagasin(String idMagasin) throws Exception {
    try {
      setMagasin(new Magasin(Integer.valueOf(idMagasin)));
    } catch (Exception e) {
      throw new Exception("Magasin non valide");
    }
  }

  public Date getDateInitial() {
    return dateInitial;
  }

  public double getMontantTotal() {
    double s = 0;
    for (Stock stock : listeStock) {
      s += stock.getMontant();
    }
    return s;
  }

  public void setDateInitial(Date dateInitial) {
    this.dateInitial = dateInitial;
  }

  public Date getDateFinal() {
    return dateFinal;
  }

  public void setDateFinal(Date dateFinal) {
    this.dateFinal = dateFinal;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public Stock[] getListeStock() {
    return listeStock;
  }

  public void setListeStock(Stock[] listeStock) {
    this.listeStock = listeStock;
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
      "EtatStock [dateInitial=" +
      dateInitial +
      ", dateFinal=" +
      dateFinal +
      ", article=" +
      article +
      ", listeStock=" +
      Arrays.toString(listeStock) +
      ", magasin=" +
      magasin +
      "]"
    );
  }
}
