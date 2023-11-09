package modele;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import connexion.Connect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Magasin {

  int idMagasin;
  String nomMagasin;

  
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
