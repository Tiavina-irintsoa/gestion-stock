package modele;

public class Article {

  String idArticle;
  String nomArticle;
  Unite unite;
  int methodeStockage;

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
}
