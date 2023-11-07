package modele;

public class Stock {

  Article article;
  Mouvement[] mouvementInitial;
  Mouvement[] mouvementEntreDate1Date2;
  Mouvement[] entree;

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public Mouvement[] getMouvementInitial() {
    return mouvementInitial;
  }

  public void setMouvementInitial(Mouvement[] mouvementInitial) {
    this.mouvementInitial = mouvementInitial;
  }

  public Mouvement[] getMouvementEntreDate1Date2() {
    return mouvementEntreDate1Date2;
  }

  public void setMouvementEntreDate1Date2(
    Mouvement[] mouvementEntreDate1Date2
  ) {
    this.mouvementEntreDate1Date2 = mouvementEntreDate1Date2;
  }

  public Mouvement[] getEntree() {
    return entree;
  }

  public void setEntree(Mouvement[] entree) {
    this.entree = entree;
  }
}
