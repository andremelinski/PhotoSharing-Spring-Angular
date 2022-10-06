package com.springproject.photosharing.service;

import com.springproject.photosharing.dto.SavingPhotoDto;
import com.springproject.photosharing.global.FileStore;
import com.springproject.photosharing.model.AppUser;
import com.springproject.photosharing.model.Comment;
import com.springproject.photosharing.model.Post;
import com.springproject.photosharing.repo.PostRepo;
import com.springproject.photosharing.service.interfaces.IPostService;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.activation.MimetypesFileTypeMap;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Async
public class PostService implements IPostService {

  @Value("${amazonProperties.bucketName}")
  private String bucketName;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private PostRepo postRepo;

  @Autowired
  private Comments commentService;

  private static final Collection<String> allowedMimes = new ArrayList<String>(
    Arrays.asList("image/jpeg", "image/png", "image/jpg")
  );

  @Override
  public Post savePost(
    AppUser user,
    SavingPhotoDto savingPhotoDto,
    // MultipartFile multipartFile,
    String postImageName
  ) {
    Post post = new Post();
    String caption = savingPhotoDto.getCaption();
    String location = savingPhotoDto.getLocation();
    Date postedDate = new Date();
    post.setName(postImageName);
    post.setCaption(caption);
    post.setLocation(location);
    post.setPostedDate(postedDate);
    post.setUsername(user.getUsername());
    post.setUserImageId(user.getId());
    user.setPost(post);
    try {
      // String s3url = this.savePostImage(multipartFile, user.getUsername());
      // post.setPostUrl(s3url);
      return postRepo.save(post);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<Post> postList() {
    return postRepo.findAll();
  }

  @Override
  public Post getPostById(Long id) {
    return postRepo.findPostById(id);
  }

  @Override
  public List<Post> findPostByUsername(String username) {
    return postRepo.findPostByUsername(username);
  }

  @Override
  public Post deletePost(Post post) {
    postRepo.deletePostById(post.getId());
    return post;
  }

  @Override
  public Post savePostImage(
    MultipartFile multipartFile,
    String username,
    Post postInfo
  ) {
    Map<String, String> imageProps = this.getImageProperties(multipartFile);
    Map<String, String> buckerFileNameS3 =
      this.setBuckerFileNameS3(username, imageProps.get("fileName"));
    System.out.println(buckerFileNameS3);

    try {
      InputStream inputStream = multipartFile.getInputStream();
      String s3url = fileStore.save(buckerFileNameS3, imageProps, inputStream);
      System.out.println(s3url);
      // String s3url = this.savePostImage(multipartFile, user.getUsername());
      postInfo.setPostUrl(s3url);
      return postRepo.save(postInfo);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public String saveComment(String postComment, Post post, String username) {
    try {
      Comment comment = new Comment();
      comment.setContent(postComment);
      comment.setUsername(username);
      comment.setPostedDate(new Date());
      post.setComments(comment);
      commentService.saveComment(comment);
      return postComment;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private Map<String, String> setBuckerFileNameS3(
    String username,
    String fileName
  ) {
    Map<String, String> s3Infometadata = new HashMap<>();

    String bucketPath = String.format("%s/%s", bucketName, username);
    String fileNameInS3 = String.format("%s-%s", UUID.randomUUID(), fileName);

    s3Infometadata.put("bucketPath", bucketPath);
    s3Infometadata.put("fileNameInS3", fileNameInS3);
    return s3Infometadata;
  }

  private Map<String, String> getImageProperties(MultipartFile multipartFile) {
    MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    Map<String, String> metadata = new HashMap<>();

    String file = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    System.out.println(file + "file");
    String mimeType = fileTypeMap.getContentType(file);
    long fileSize = multipartFile.getSize();

    if (mimeType.isBlank() || !allowedMimes.contains(mimeType)) {
      throw new IllegalStateException("Mime type not allowed" + mimeType);
    }

    metadata.put("mimeType", mimeType);
    metadata.put("fileSize", String.valueOf(fileSize));
    metadata.put("fileName", file);
    return metadata;
  }
}
