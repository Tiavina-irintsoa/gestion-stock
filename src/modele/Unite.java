package modele;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Unite {

  int idUnite;
  String nomUnite;
  String abreviation;

  public ObjectNode getObjectNode() {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("idunite", getIdUnite());
    objectNode.put("nomUnite", getNomUnite());
    objectNode.put("abreviation", getAbreviation());
    return objectNode;
  }

  public Unite(int idUnite, String nomUnite, String abreviation) {
    this.idUnite = idUnite;
    this.nomUnite = nomUnite;
    this.abreviation = abreviation;
  }

  public Unite() {}

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
