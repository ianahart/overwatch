package com.hart.overwatch.comment;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.comment.request.CreateCommentRequest;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final TopicService topicService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService,
            TopicService topicService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.topicService = topicService;
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find comment with the id %d", commentId)));
    }


    public void createComment(CreateCommentRequest request) {
        User user = userService.getUserById(request.getUserId());
        Topic topic = topicService.getTopicById(request.getTopicId());
        String content = Jsoup.clean(request.getContent(), Safelist.none());

        Comment comment = new Comment(content, user, topic);

        commentRepository.save(comment);
    }
}
