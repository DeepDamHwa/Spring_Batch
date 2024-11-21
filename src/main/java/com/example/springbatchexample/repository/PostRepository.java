package com.example.springbatchexample.repository;

import com.example.springbatchexample.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPublishedFalse();  // 미게시된 게시글을 찾는 쿼리 메소드
}
