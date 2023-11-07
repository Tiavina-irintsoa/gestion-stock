package modele;

public class Unite {

  int idUnite;
  String nomUnite;
  String abreviation;

  public Unite(int idUnite, String nomUnite, String abreviation) {
    this.idUnite = idUnite;
    this.nomUnite = nomUnite;
    this.abreviation = abreviation;
}

public Unite() {
}

public int getIdUnite() {
    return idUnite;
  }

  public void setIdUnite(int idUnite) {
    this.idUnite = idUnite;
  }

  public String getNomUnite() {
    return nomUnite;
  }

  public void setNomUnite(String nomUnite) {
    this.nomUnite = nomUnite;
  }

  public String getAbreviation() {
    return abreviation;
  }

  public void setAbreviation(String abreviation) {
    this.abreviation = abreviation;
  }
}
