package com.springproject.photosharing.repo;

import com.springproject.photosharing.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
  @Query("SELECT p FROM Post p order by p.postedDate DESC")
  public List<Post> findAll();

  @Query(
    "SELECT p FROM Post p WHERE p.username=:username order by p.postedDate DESC"
  )
  public List<Post> findPostByUsername(@Param("username") String username);

  @Query("SELECT p FROM Post p WHERE p.id=:postId")
  public Post findPostById(@Param("postId") Long id);

  @Modifying
  @Query("DELETE Post WHERE id=:postId")
  public void deletePostById(@Param("postId") Long id);
}
