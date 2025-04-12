package com.doomdev.admin_blog.repositories;

import com.doomdev.admin_blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
