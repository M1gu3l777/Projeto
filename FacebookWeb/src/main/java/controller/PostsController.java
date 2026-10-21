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
import model.Post;
import model.User;
import model.dao.DAOFactory;
import model.dao.PostDAO;
import model.dao.UserDAO;

@WebServlet(urlPatterns = {
    "/posts",
    "/posts/form",
    "/posts/save",
    "/posts/update",
    "/posts/delete"
})
public class PostsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getServletPath();

        try {
            switch (action) {
                case "/posts":
                    loadPosts(req);
                    forward(req, resp, "/posts.jsp");
                    break;

                case "/posts/form":
                    loadUsers(req);
                    forward(req, resp, "/posts_form.jsp");
                    break;

                case "/posts/update":
                    loadPost(req);
                    forward(req, resp, "/posts_form.jsp");
                    break;

                case "/posts/delete":
                    deletePost(req);
                    redirect(req, resp, "/posts");
                    break;

                case "/posts/save":
                    resp.sendError(
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                        "Use o formulário para salvar o post."
                    );
                    break;

                default:
                    resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                    );
                    break;
            }
        } catch (ModelException e) {
            throw new ServletException(
                "Erro ao processar posts.", e
            );
        } catch (IllegalArgumentException e) {
            throw new ServletException(
                "Dados inválidos para o post.", e
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getServletPath();

        try {
            switch (action) {
                case "/posts/save":
                case "/posts/update":
                    saveOrUpdatePost(req);
                    redirect(req, resp, "/posts");
                    break;

                case "/posts/delete":
                    deletePost(req);
                    redirect(req, resp, "/posts");
                    break;

                default:
                    resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                    );
                    break;
            }
        } catch (ModelException e) {
            throw new ServletException(
                "Não foi possível processar o post.", e
            );
        } catch (IllegalArgumentException e) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    private void loadPosts(HttpServletRequest req)
            throws ModelException {

        PostDAO dao = DAOFactory.createDAO(PostDAO.class);
        List<Post> posts = dao.listAll();

        req.setAttribute("posts_list", posts);
    }

    private void loadUsers(HttpServletRequest req)
            throws ModelException {

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);

        req.setAttribute(
            "users_list",
            dao.listAll()
        );
    }

    private void loadPost(HttpServletRequest req)
            throws ModelException, ServletException {

        String idStr = req.getParameter("postId");

        if (idStr == null || idStr.isBlank()) {
            throw new ServletException(
                "ID do post não informado."
            );
        }

        int id = Integer.parseInt(idStr);

        PostDAO postDAO = DAOFactory.createDAO(PostDAO.class);
        Post post = postDAO.findById(id);

        if (post == null) {
            throw new ServletException(
                "Post não encontrado."
            );
        }

        req.setAttribute("post", post);
        loadUsers(req);
    }

    private void saveOrUpdatePost(HttpServletRequest req)
            throws ModelException {

        String idStr = req.getParameter("post_id");
        String content = req.getParameter("content");
        String userIdStr = req.getParameter("user_id");

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                "Informe o conteúdo do post."
            );
        }

        if (userIdStr == null || userIdStr.isBlank()) {
            throw new IllegalArgumentException(
                "Selecione um usuário para o post."
            );
        }

        int userId = Integer.parseInt(userIdStr);

        boolean atualizar = idStr != null
                && !idStr.isBlank()
                && !"0".equals(idStr);

        Post post;

        PostDAO dao = DAOFactory.createDAO(PostDAO.class);

        if (atualizar) {
            int id = Integer.parseInt(idStr);
            post = dao.findById(id);

            if (post == null) {
                throw new IllegalArgumentException(
                    "Post não encontrado."
                );
            }
        } else {
            post = new Post();
        }

        User user = new User(userId);

        post.setContent(content.trim());
        post.setUser(user);

        if (atualizar) {
            dao.update(post);
        } else {
            dao.save(post);
        }
    }

    private void deletePost(HttpServletRequest req)
            throws ModelException {

        String idStr = req.getParameter("postId");

        if (idStr == null || idStr.isBlank()) {
            throw new IllegalArgumentException(
                "ID do post não informado."
            );
        }

        int id = Integer.parseInt(idStr);

        PostDAO dao = DAOFactory.createDAO(PostDAO.class);
        Post post = dao.findById(id);

        if (post != null) {
            dao.delete(post);
        }
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

        resp.sendRedirect(
            req.getContextPath() + path
        );
    }
}
