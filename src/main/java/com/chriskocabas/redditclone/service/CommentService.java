package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.Exceptions.CustomException;
import com.chriskocabas.redditclone.dto.CommentsDto;
import com.chriskocabas.redditclone.mapper.CommentMapper;
import com.chriskocabas.redditclone.model.*;
import com.chriskocabas.redditclone.repository.ICommentRepository;
import com.chriskocabas.redditclone.repository.IPostRepository;
import com.chriskocabas.redditclone.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final ICommentRepository commentRepository;
    private final AuthService authService;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Transactional
    public void save(CommentsDto commentsDto) {
        User user = authService.getCurrentUser();
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()-> new CustomException("No posts found with post id: "+ commentsDto.getPostId()));

        Comment comment = commentMapper.map(commentsDto, post,user);
        commentRepository.save(comment);
        String POST_URL = "https://zealous-wave-027e5c910.3.azurestaticapps.net/#/view-post/"+ commentsDto.getPostId();
        if (post.getNotificationStatus()) {
            String message = user.getUsername() +
                    " posted a response to your post. Click here to go to the post: " + POST_URL;
            sendCommentNotification(message, post.getUser(), user);
        }
    }

    private void sendCommentNotification(String message, User threadstarter, User commenter) {
        mailService.sendMail(new NotificationEmail(commenter.getUsername()+ " replied to your post", threadstarter.getEmail(),threadstarter.getUsername(), message));
    }
    @Transactional
    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CustomException("No posts found with post id: "+ postId));
        List<CommentsDto> allComments = commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
        return allComments;
    }
    @Transactional
    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new CustomException("No user found with username: "+ userName));

        List<CommentsDto> userComments = commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());

        return userComments;

    }
    @Transactional
    public void update(CommentsDto commentsDto) {
        User user = authService.getCurrentUser();

        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
            Comment comment = commentRepository.findById(commentsDto.getId()).orElseThrow(
                    ()->new CustomException("Comment doesn't exist!"));
            comment.setText(commentsDto.getText());
            commentRepository.save(comment);
        }

        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()-> new CustomException("No posts found with post id: "+ commentsDto.getPostId()));

        Comment comment = commentRepository.findCommentByUserAndPostAndId(user, post, commentsDto.getId()).orElseThrow(()->
                new CustomException("Comment doesn't exist or insufficient privileges!"));

        comment.setText(commentsDto.getText());
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(CommentsDto commentsDto) {
        User user = authService.getCurrentUser();

        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
            Comment comment = commentRepository.findById(commentsDto.getId()).orElseThrow(
                    ()->new CustomException("Comment doesn't exist!"));
            commentRepository.delete(comment);
        }

        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()-> new CustomException("No posts found with post id: "+ commentsDto.getPostId()));

        Comment comment = commentRepository.findCommentByUserAndPostAndId(user,post,commentsDto.getId()).orElseThrow(()->
                new CustomException("Comment doesn't exist or insufficient privileges!"));

        commentRepository.delete(comment);
    }


}
