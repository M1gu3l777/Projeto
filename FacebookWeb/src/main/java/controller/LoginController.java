
package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String contextPath = req.getContextPath();
        String action = req.getServletPath();

        if ("/logout".equals(action)) {
            HttpSession session = req.getSession(false);

            if (session != null) {
                session.invalidate();
            }
        }

        resp.sendRedirect(contextPath + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String contextPath = req.getContextPath();

        String userEmail = req.getParameter("user_login");
        String userPW = req.getParameter("user_pw");

        // 1. Verificar os campos
        if (userEmail == null || userPW == null
                || userEmail.isBlank() || userPW.isBlank()) {

            System.out.println("Login: e-mail ou senha vazios.");

            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
            return;
        }

        // Remover espaços acidentais do e-mail
        userEmail = userEmail.trim();

        // 2. Buscar o usuário no banco de dados
        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        User userToLogin = null;

        try {
            userToLogin = dao.findByEmail(userEmail);
        } catch (ModelException e) {
            System.out.println("Erro ao consultar usuário no banco:");
            e.printStackTrace();

            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
            return;
        }

        // 3. Verificar se o usuário existe
        if (userToLogin == null) {
            System.out.println("Login: usuário não encontrado para o e-mail informado.");

            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
            return;
        }

        // 4. Diagnóstico no Console
        System.out.println("Login: usuário encontrado.");
        System.out.println("E-mail encontrado: " + userToLogin.getEmail());
        System.out.println("Hash recuperado do banco: "
                + userToLogin.getPasswordHash());

        // 5. Verificar a senha
        String hashBanco = userToLogin.getPasswordHash();

        if (hashBanco == null || hashBanco.isBlank()) {
            System.out.println("Login: o hash da senha está vazio.");

            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
            return;
        }

        boolean senhaCorreta =
                PasswordEncryptor.checkPassword(userPW, hashBanco);

        System.out.println("Senha corresponde ao hash: " + senhaCorreta);

        if (senhaCorreta) {
            // 6. Criar a sessão e entrar no sistema
            HttpSession session = req.getSession();
            session.setAttribute("user_logged", userToLogin);

            System.out.println("Login realizado com sucesso.");

            resp.sendRedirect(contextPath + "/");
        } else {
            System.out.println("Login: senha não corresponde ao hash.");

            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
        }
    }
}
