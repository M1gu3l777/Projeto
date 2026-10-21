package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Comment;
import model.Post;
import model.User;
import model.ModelException;

import model.dao.DAOFactory;
import model.dao.CommentDAO;
import model.dao.UserDAO;
import model.dao.PostDAO;

@WebServlet(urlPatterns = {
    "/comments",
    "/comments/form",
    "/comments/save",
    "/comments/update",
    "/comments/delete"
})
public class CommentsController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CommentDAO commentDAO;
    private UserDAO userDAO;
    private PostDAO postDAO;


@Override
public void init() throws ServletException {
    commentDAO = new model.dao.MySQLCommentDAO();
    userDAO = new model.dao.MySQLUserDAO();
    postDAO = new model.dao.MySQLPostDAO();
}


    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getServletPath();

        try {
            switch (action) {

                case "/comments":
                    req.setAttribute(
                        "comments_list",
                        commentDAO.listAll()
                    );

                    req.getRequestDispatcher(
                        "/comments.jsp"
                    ).forward(req, resp);
                    break;

                case "/comments/form":
                    abrirFormulario(req, resp, null);
                    break;

                case "/comments/update":
                    int idEditar = Integer.parseInt(
                        req.getParameter("id")
                    );

                    Comment commentEditar =
                        commentDAO.findById(idEditar);

                    if (commentEditar == null) {
                        resp.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Comentário não encontrado."
                        );
                        return;
                    }

                    abrirFormulario(req, resp, commentEditar);
                    break;

                case "/comments/delete":
                    int idExcluir = Integer.parseInt(
                        req.getParameter("id")
                    );

                    Comment commentExcluir =
                        commentDAO.findById(idExcluir);

                    if (commentExcluir == null) {
                        resp.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Comentário não encontrado."
                        );
                        return;
                    }

                    commentDAO.delete(commentExcluir);

                    resp.sendRedirect(
                        req.getContextPath() + "/comments"
                    );
                    break;

                default:
                    resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                    );
                    break;
            }

        } catch (NumberFormatException e) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Identificador inválido."
            );

        } catch (ModelException e) {
            throw new ServletException(
                "Erro ao acessar os dados dos comentários.", e
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

                case "/comments/save":
                    salvarComentario(req);
                    break;

                case "/comments/update":
                    atualizarComentario(req);
                    break;

                default:
                    resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                    );
                    return;
            }

            resp.sendRedirect(
                req.getContextPath() + "/comments"
            );

        } catch (NumberFormatException e) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Verifique os identificadores informados."
            );

        } catch (ModelException e) {
            throw new ServletException(
                "Erro ao salvar o comentário.", e
            );
        }
    }

    private void abrirFormulario(
            HttpServletRequest req,
            HttpServletResponse resp,
            Comment comment)
            throws ServletException, IOException, ModelException {

        req.setAttribute("users", userDAO.listAll());
        req.setAttribute("posts", postDAO.listAll());

        if (comment != null) {
            req.setAttribute("comment", comment);
        }

        RequestDispatcher dispatcher =
            req.getRequestDispatcher("/comments_form.jsp");

        dispatcher.forward(req, resp);
    }

    private void salvarComentario(HttpServletRequest req)
            throws ModelException {

        String content = req.getParameter("content");

        int userId = Integer.parseInt(
            req.getParameter("user_id")
        );

        int postId = Integer.parseInt(
            req.getParameter("post_id")
        );

        User user = userDAO.findById(userId);
        Post post = postDAO.findById(postId);

        if (user == null || post == null) {
            throw new ModelException(
                "O usuário ou o post selecionado não existe."
            );
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);

        commentDAO.save(comment);
    }

    private void atualizarComentario(HttpServletRequest req)
            throws ModelException {

        int id = Integer.parseInt(
            req.getParameter("id")
        );

        String content = req.getParameter("content");

        int userId = Integer.parseInt(
            req.getParameter("user_id")
        );

        int postId = Integer.parseInt(
            req.getParameter("post_id")
        );

        Comment comment = commentDAO.findById(id);

        if (comment == null) {
            throw new ModelException(
                "Comentário não encontrado."
            );
        }

        User user = userDAO.findById(userId);
        Post post = postDAO.findById(postId);

        if (user == null || post == null) {
            throw new ModelException(
                "O usuário ou o post selecionado não existe."
            );
        }

        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);

        commentDAO.update(comment);
    }
}