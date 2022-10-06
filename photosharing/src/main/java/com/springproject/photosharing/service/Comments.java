package com.springproject.photosharing.service;

import com.springproject.photosharing.model.Comment;
import com.springproject.photosharing.repo.CommentRepo;
import com.springproject.photosharing.service.interfaces.ICommentService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Comments implements ICommentService {

  @Autowired
  private CommentRepo commentRepo;

  @Override
  public void saveComment(Comment comment) {
    commentRepo.save(comment);
  }
}
