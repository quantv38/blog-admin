package com.doomdev.admin_blog.repositories;

import com.doomdev.admin_blog.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
