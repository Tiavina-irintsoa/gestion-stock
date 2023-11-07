package modele;

public class Article {

  String idArticle;
  String nomArticle;
  Unite unite;
  int methodeStockage;

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
