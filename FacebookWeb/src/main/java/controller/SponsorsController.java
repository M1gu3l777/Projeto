package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.ModelException;
import model.Sponsor;
import model.dao.DAOFactory;
import model.dao.SponsorDAO;

@WebServlet(urlPatterns = {"/sponsors"})
public class SponsorsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            SponsorDAO dao =
                DAOFactory.createDAO(SponsorDAO.class);

            List<Sponsor> sponsors = dao.listAll();

            req.setAttribute("sponsors_list", sponsors);

            // O arquivo existente é Sponsors.jsp, com S maiúsculo.
            RequestDispatcher dispatcher =
                req.getRequestDispatcher("/Sponsors.jsp");

            dispatcher.forward(req, resp);

        } catch (ModelException e) {
            throw new ServletException(
                "Erro ao carregar patrocinadores.", e);
        }
    }
}