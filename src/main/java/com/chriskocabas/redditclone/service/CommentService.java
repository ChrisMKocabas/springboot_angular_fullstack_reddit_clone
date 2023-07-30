package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.Exceptions.CustomException;
import com.chriskocabas.redditclone.dto.CommentsDto;
import com.chriskocabas.redditclone.mapper.CommentMapper;
import com.chriskocabas.redditclone.model.Comment;
import com.chriskocabas.redditclone.model.NotificationEmail;
import com.chriskocabas.redditclone.model.Post;
import com.chriskocabas.redditclone.model.User;
import com.chriskocabas.redditclone.repository.ICommentRepository;
import com.chriskocabas.redditclone.repository.IPostRepository;
import com.chriskocabas.redditclone.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final ICommentRepository commentRepository;
    private final AuthService authService;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        User user = authService.getCurrentUser();
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()-> new CustomException("No posts found with post id: "+ commentsDto.getPostId()));

        Comment comment = commentMapper.map(commentsDto, post,user);
        commentRepository.save(comment);
        String POST_URL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/posts/"+commentsDto.getPostId()).toUriString();
        if (post.getNotificationStatus()) {
            String message = user.getUsername() +
                    " posted a response to your post. Click here to go to the post: " + POST_URL;
            sendCommentNotification(message, post.getUser(), user);
        }
    }

    private void sendCommentNotification(String message, User threadstarter, User commenter) {
        mailService.sendMail(new NotificationEmail(commenter.getUsername()+ " replied to your post", threadstarter.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CustomException("No posts found with post id: "+ postId));
        List<CommentsDto> allComments = commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
        return allComments;
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new CustomException("No user found with username: "+ userName));

        List<CommentsDto> userComments = commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());

        return userComments;

    }
}
