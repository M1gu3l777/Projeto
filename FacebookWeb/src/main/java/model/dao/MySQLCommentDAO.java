
package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.Comment;
import model.ModelException;
import model.Post;
import model.User;

public class MySQLCommentDAO implements CommentDAO {

    @Override
    public boolean save(Comment comment) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "INSERT INTO comments "
                + "(content, user_id, post_id) "
                + "VALUES (?, ?, ?);";

        db.prepareStatement(sql);
        db.setString(1, comment.getContent());
        db.setInt(2, comment.getUser().getId());
        db.setInt(3, comment.getPost().getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean update(Comment comment) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "UPDATE comments "
                + "SET content = ?, user_id = ?, post_id = ? "
                + "WHERE id = ?;";

        db.prepareStatement(sql);
        db.setString(1, comment.getContent());
        db.setInt(2, comment.getUser().getId());
        db.setInt(3, comment.getPost().getId());
        db.setInt(4, comment.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Comment comment) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "DELETE FROM comments WHERE id = ?;";

        db.prepareStatement(sql);
        db.setInt(1, comment.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public List<Comment> listAll() throws ModelException {

        DBHandler db = new DBHandler();

        List<Comment> comments = new ArrayList<>();

        String sql = "SELECT id, content, comment_date, "
                + "user_id, post_id "
                + "FROM comments "
                + "ORDER BY comment_date DESC;";

        db.createStatement();
        db.executeQuery(sql);

        while (db.next()) {
            comments.add(createComment(db));
        }

        return comments;
    }

    @Override
    public Comment findById(int id) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "SELECT id, content, comment_date, "
                + "user_id, post_id "
                + "FROM comments WHERE id = ?;";

        db.prepareStatement(sql);
        db.setInt(1, id);
        db.executeQuery();

        if (db.next()) {
            return createComment(db);
        }

        return null;
    }

    private Comment createComment(DBHandler db)
            throws ModelException {

        Comment comment = new Comment();

        comment.setId(db.getInt("id"));
        comment.setContent(db.getString("content"));
        comment.setCommentDate(
            db.getDateTime("comment_date")
        );

        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        User user = userDAO.findById(db.getInt("user_id"));
        comment.setUser(user);

        PostDAO postDAO = DAOFactory.createDAO(PostDAO.class);
        Post post = postDAO.findById(db.getInt("post_id"));
        comment.setPost(post);

        return comment;
    }
}
