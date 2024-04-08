package com.blogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogger.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

}
