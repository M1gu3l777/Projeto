
package model;

import java.time.LocalDateTime;

public class Comment {

    private Integer id;
    private String content;
    private LocalDateTime commentDate;
    private User user;
    private Post post;

    public Comment() {
    }

    public Comment(Integer id, String content, LocalDateTime commentDate,
                   User user, Post post) {
        this.id = id;
        this.content = content;
        this.commentDate = commentDate;
        this.user = user;
        this.post = post;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
