package com.example.springbatchexample.service;

import com.example.springbatchexample.entity.Member;
import com.example.springbatchexample.entity.Post;
import com.example.springbatchexample.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글을 저장하는 메소드
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    // 모든 게시글을 조회하는 메소드
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    // 미게시된 게시글을 조회하는 메소드
    public List<Post> findUnpublishedPosts() {
        return postRepository.findByPublishedFalse();
    }

}

