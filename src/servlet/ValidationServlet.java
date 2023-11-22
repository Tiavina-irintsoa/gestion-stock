package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import modele.Mouvement;

public class ValidationServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    try {
      System.out.println("parameter"+req.getParameter("datevalidation"));
      Mouvement.validerSortie(req.getParameter("idmouvement"),req.getParameter("datevalidation"));
      
      resp.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      PrintWriter out = resp.getWriter();
      out.print(e.getMessage());
      e.printStackTrace();
    }
  }
}
