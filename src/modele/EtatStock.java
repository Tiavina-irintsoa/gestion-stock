package modele;

import java.sql.Date;

public class EtatStock {

  Date dateInitial;
  Date dateFinal;
  Article article;
  Stock[] listeStock;
  Magasin magasin;

  public Date getDateInitial() {
    return dateInitial;
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
}
