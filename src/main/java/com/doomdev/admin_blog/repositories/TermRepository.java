package com.doomdev.admin_blog.repositories;

import com.doomdev.admin_blog.entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
