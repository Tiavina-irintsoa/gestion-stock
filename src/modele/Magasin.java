package modele;

public class Magasin {

  int idMagasin;
  String nomMagasin;

  public Magasin(int idMagasin, String nomMagasin) {
    this.idMagasin = idMagasin;
    this.nomMagasin = nomMagasin;
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

  public void setNomMagasin(String nomMagasin) {
    this.nomMagasin = nomMagasin;
  }
}
