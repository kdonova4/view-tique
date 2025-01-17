package learn.review_tique.data;

import learn.review_tique.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> findAll();

    Comment findById(int commentId);

    List<Comment> findByUserId(int userId);

    List<Comment> findByReviewId(int reviewId);

    Comment add(Comment comment);

    boolean update(Comment comment);

    boolean deleteById(int commentId);
}
