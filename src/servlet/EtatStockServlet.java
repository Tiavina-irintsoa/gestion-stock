package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import modele.EtatStock;
import modele.Stock;

public class EtatStockServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    PrintWriter out = resp.getWriter();

    try {
      Map<String, String[]> parameters = req.getParameterMap();
      String date1 = parameters.get("date1")[0];
      System.out.println(date1 + "date1");
      String date2 = parameters.get("date2")[0];
      System.out.println(date2 + "date2");
      String idArticle = parameters.get("idArticle")[0];
      String idMagasin = parameters.get("idMagasin")[0];
      System.out.println(req.getParameter("date1"));
      resp.setContentType("application/json");
      EtatStock e = EtatStock.getEtatStock(date1, date2, idArticle, idMagasin);
      out.print(e.getJSON());
    } catch (Exception e) {
      out.print(e.getMessage());
      e.printStackTrace();
    }
  }
}
