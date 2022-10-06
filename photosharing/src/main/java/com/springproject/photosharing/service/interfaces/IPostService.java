package com.springproject.photosharing.service.interfaces;

import com.springproject.photosharing.dto.SavingPhotoDto;
import com.springproject.photosharing.model.AppUser;
import com.springproject.photosharing.model.Post;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IPostService {
  public Post savePost(
    AppUser user,
    SavingPhotoDto savingPhotoDto,
    // MultipartFile multipartFile,
    String postImageName
  );

  public List<Post> postList();

  public Post getPostById(Long id);

  public List<Post> findPostByUsername(String username);

  public Post deletePost(Post post);

  public Post savePostImage(
    MultipartFile multipartFile,
    String userName,
    Post postInfo
  );
}
