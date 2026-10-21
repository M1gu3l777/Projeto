
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
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {
    "/users",
    "/users/form",
    "/users/save",
    "/users/update",
    "/users/delete"
})
public class UsersController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getServletPath();

        try {
            switch (action) {
                case "/users":
                    loadUsers(req);
                    forward(req, resp, "/users.jsp");
                    break;

                case "/users/form":
                    req.setAttribute("user", null);
                    forward(req, resp, "/users_form.jsp");
                    break;

                case "/users/update":
                    loadUser(req);
                    forward(req, resp, "/users_form.jsp");
                    break;

                case "/users/delete":
                    deleteUser(req);
                    redirect(req, resp, "/users");
                    break;

                case "/users/save":
                    resp.sendError(
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                        "Use o formulário para salvar o usuário."
                    );
                    break;

                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (ModelException e) {
            throw new ServletException(
                "Erro ao consultar usuários.", e
            );
        } catch (IllegalArgumentException e) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getServletPath();

        try {
            switch (action) {
                case "/users/save":
                    saveOrUpdateUser(req);
                    redirect(req, resp, "/users");
                    break;

                case "/users/delete":
                    deleteUser(req);
                    redirect(req, resp, "/users");
                    break;

                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (ModelException e) {
            throw new ServletException(
                "Erro ao processar usuário.", e
            );
        } catch (IllegalArgumentException e) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    private void loadUsers(HttpServletRequest req)
            throws ModelException {

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        List<User> users = dao.listAll();

        req.setAttribute("users_list", users);
    }

    private void loadUser(HttpServletRequest req)
            throws ModelException, ServletException {

        String idStr = req.getParameter("userId");

        if (idStr == null || idStr.isBlank()) {
            throw new ServletException(
                "ID do usuário não informado."
            );
        }

        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new ServletException(
                "ID do usuário inválido.", e
            );
        }

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        User user = dao.findById(id);

        if (user == null) {
            throw new ServletException(
                "Usuário não encontrado."
            );
        }

        req.setAttribute("user", user);
    }

    private void saveOrUpdateUser(HttpServletRequest req)
            throws ModelException {

        String idStr = req.getParameter("user_id");
        String nome = req.getParameter("user_name");
        String sexo = req.getParameter("user_gender");
        String email = req.getParameter("user_email");
        String senha = req.getParameter("user_pw");

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "Informe o nome."
            );
        }

        if (sexo == null || sexo.isBlank()) {
            throw new IllegalArgumentException(
                "Selecione o sexo."
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "Informe o e-mail."
            );
        }

        boolean atualizar =
            idStr != null && !idStr.isBlank();

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        User user;

        if (atualizar) {
            int id;

            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "ID do usuário inválido."
                );
            }

            user = dao.findById(id);

            if (user == null) {
                throw new IllegalArgumentException(
                    "Usuário não encontrado."
                );
            }
        } else {
            user = new User();
        }

        user.setName(nome.trim());
        user.setGender(sexo);
        user.setEmail(email.trim());

        if (senha != null && !senha.isBlank()) {
            user.setPasswordHash(
                PasswordEncryptor.hashPassword(senha)
            );
            user.setPassword("");
        } else if (!atualizar) {
            throw new IllegalArgumentException(
                "Informe uma senha para o novo usuário."
            );
        }

        if (atualizar) {
            dao.update(user);
        } else {
            dao.save(user);
        }
    }

    private void deleteUser(HttpServletRequest req)
            throws ModelException {

        String idStr = req.getParameter("userId");

        if (idStr == null || idStr.isBlank()) {
            idStr = req.getParameter("user_id");
        }

        if (idStr == null || idStr.isBlank()) {
            throw new IllegalArgumentException(
                "ID do usuário não informado."
            );
        }

        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "ID do usuário inválido."
            );
        }

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        User user = dao.findById(id);

        if (user == null) {
            throw new IllegalArgumentException(
                "Usuário não encontrado."
            );
        }

        dao.delete(user);
    }

    private void forward(HttpServletRequest req,
                         HttpServletResponse resp,
                         String path)
            throws ServletException, IOException {

        RequestDispatcher dispatcher =
            req.getRequestDispatcher(path);

        dispatcher.forward(req, resp);
    }

    private void redirect(HttpServletRequest req,
                          HttpServletResponse resp,
                          String path)
            throws IOException {

        resp.sendRedirect(req.getContextPath() + path);
    }
}
