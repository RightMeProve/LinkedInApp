package com.rightmeprove.linkedin.posts_service.service;

import com.rightmeprove.linkedin.posts_service.entity.PostLike;
import com.rightmeprove.linkedin.posts_service.exception.BadRequestException;
import com.rightmeprove.linkedin.posts_service.exception.ResourceNotFoundException;
import com.rightmeprove.linkedin.posts_service.repository.PostLikeRepository;
import com.rightmeprove.linkedin.posts_service.repository.PostsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;

    public void likePost(Long postId, Long userId){
        log.info("Attempting to like the post with id: {}",postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: " + postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id: {} liked successfully!",postId);
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        log.info("Attempting to dislike the post with id: {}",postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: " + postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(!alreadyLiked) throw new BadRequestException("Cannot Unlike the post.");

        postLikeRepository.deleteByUserIdAndPostId(userId,postId);
        log.info("Post with id: {}  unliked successfully",postId);

    }
}
