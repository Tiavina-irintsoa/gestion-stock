package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modele.EtatStock;
import modele.Magasin;

public class EtatStockServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    PrintWriter out = resp.getWriter();

    try {
      Magasin m = new Magasin(req.getParameter("idMagasin"));
      EtatStock e = m.getEtatStock(
        req.getParameter("date1"),
        req.getParameter("date2"),
        req.getParameter("idArticle")
      );
      resp.setContentType("application/json");
      out.print(e.getJSON());
    } catch (Exception e) {
      out.print(e.getMessage());
      e.printStackTrace();
    }
  }
}
