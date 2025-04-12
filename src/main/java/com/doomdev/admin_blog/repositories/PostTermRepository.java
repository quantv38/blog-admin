package com.doomdev.admin_blog.repositories;

import com.doomdev.admin_blog.entities.PostTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTermRepository extends JpaRepository<PostTerm, Long> {
}
