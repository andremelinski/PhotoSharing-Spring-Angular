package com.springproject.photosharing.controllers;

import com.springproject.photosharing.dto.SavingPhotoDto;
import com.springproject.photosharing.model.AppUser;
import com.springproject.photosharing.model.Post;
import com.springproject.photosharing.repo.AppUserRepo;
import com.springproject.photosharing.service.AccountService;
import com.springproject.photosharing.service.PostService;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private AppUserRepo appUserRepo;

  private AppUser user;
  private Post postModel;

  @PostMapping
  public ResponseEntity<Object> savePost(
    // @RequestParam("image") MultipartFile multipartFile,
    @RequestBody @Valid SavingPhotoDto savingPhotoDto
  ) {
    // if (multipartFile.isEmpty()) {
    //   return ResponseEntity
    //     .status(HttpStatus.BAD_REQUEST)
    //     .body("File not selected");
    // }
    try {
      String usernameRequest = savingPhotoDto.getUsername();
      user = appUserRepo.findByUsername(usernameRequest);
      if (user == null) {
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("user not found");
      }

      postModel =
        postService.savePost(
          user,
          savingPhotoDto,
          // multipartFile,
          usernameRequest
        );
      return ResponseEntity.status(HttpStatus.CREATED).body(postModel);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e);
    }
  }

  @PostMapping("/photo/upload")
  public ResponseEntity<Object> postCreation(
    @RequestParam("image") MultipartFile multipartFile
  ) {
    if (multipartFile.isEmpty()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("File not selected");
    }

    try {
      Post postInfo = postService.savePostImage(
        multipartFile,
        user.getUsername(),
        postModel
      );
      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(postInfo.toString());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e);
    }
  }

  @GetMapping("/list")
  public List<Post> getAllPosts() {
    return postService.postList();
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Object> getPostById(
    @PathVariable("postId") String postId
  ) {
    try {
      Post post = postService.getPostById(Long.parseLong(postId));
      if (post == null) {
        return new ResponseEntity<Object>("post not found", HttpStatus.OK);
      }
      return ResponseEntity.status(HttpStatus.OK).body(post);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @GetMapping("/{username}")
  public ResponseEntity<Object> getImagesFromUser(
    @PathVariable("username") String username
  ) {
    try {
      List<Post> usernamePosts = this.postService.findPostByUsername(username);
      if (usernamePosts.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(List.of());
      }
      return ResponseEntity.status(HttpStatus.OK).body(usernamePosts);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Object> deletePostById(
    @PathVariable("postId") String postId
  ) {
    try {
      Post post = postService.getPostById(Long.parseLong(postId));
      if (post == null) {
        return new ResponseEntity<Object>("post not found", HttpStatus.OK);
      }
      Post postDeleted = postService.deletePost(post);
      return ResponseEntity
        .status(HttpStatus.OK)
        .body("post deleted" + postDeleted);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @PostMapping("/{like}")
  public ResponseEntity<Object> likePost(
    @RequestBody HashMap<String, String> request
  ) {
    String postId = request.get("postId");
    String usernameRequest = request.get("username");
    try {
      AppUser user = appUserRepo.findByUsername(usernameRequest);
      Post post = postService.getPostById(Long.parseLong(postId));

      if (post == null || user == null) {
        return new ResponseEntity<Object>(
          "post not found or user",
          HttpStatus.NOT_FOUND
        );
      }

      List<Post> allPostLiked = user.getLikedPost();
      if (allPostLiked.contains(post)) {
        return new ResponseEntity<Object>(
          "photo already liked",
          HttpStatus.NOT_ACCEPTABLE
        );
      }
      post.setLikes(1);
      user.setLikedPost(post);
      accountService.simpleSaveUser(user);
      return new ResponseEntity<Object>("post liked", HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @PostMapping("/{unlike}")
  public ResponseEntity<Object> unlikePost(
    @RequestBody HashMap<String, String> request
  ) {
    String postId = request.get("postId");
    String usernameRequest = request.get("username");
    try {
      AppUser user = appUserRepo.findByUsername(usernameRequest);
      Post post = postService.getPostById(Long.parseLong(postId));

      if (post == null || user == null) {
        return new ResponseEntity<Object>(
          "post not found or user",
          HttpStatus.NOT_FOUND
        );
      }

      List<Post> allPostLiked = user.getLikedPost();
      if (!allPostLiked.contains(post)) {
        return new ResponseEntity<Object>(
          "photo not liked",
          HttpStatus.NOT_ACCEPTABLE
        );
      }
      post.setLikes(-1);
      allPostLiked.remove(post);
      accountService.simpleSaveUser(user);
      return new ResponseEntity<Object>("post was unliked", HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }

  @PostMapping("/comment")
  public ResponseEntity<Object> addComment(
    @RequestBody HashMap<String, String> request
  ) {
    String postId = request.get("postId");
    String usernameRequest = request.get("username");
    try {
      AppUser user = appUserRepo.findByUsername(usernameRequest);
      Post post = postService.getPostById(Long.parseLong(postId));

      if (post == null || user == null) {
        return new ResponseEntity<Object>(
          "post or user not found",
          HttpStatus.NOT_FOUND
        );
      }

      String comment = request.get("content");
      postService.saveComment(comment, post, usernameRequest);
      return new ResponseEntity<Object>(comment, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Confict: An error occour: " + e);
    }
  }
}
