package com.springproject.photosharing.global;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.InputStream;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileStore {

  private final AmazonS3 s3;

  @Value("${amazonProperties.bucketName}")
  private String bucketName;

  @Autowired
  public FileStore(AmazonS3 s3) {
    this.s3 = s3;
  }

  public String save(
    Map<String, String> buckerFileNameS3,
    Map<String, String> fileSizeAndMime,
    InputStream inputStream
  ) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    // optionalMetadata.ifPresent(
    //   map -> {
    //     if (!map.isEmpty()) {
    //       map.forEach(objectMetadata::addUserMetadata);
    //     }
    //   }
    // );
    String bucketPath = buckerFileNameS3.get("bucketPath");
    String filename = buckerFileNameS3.get("fileNameInS3");
    System.out.println("bucketPath " + bucketPath);
    System.out.println("filename " + filename);
    objectMetadata.setContentLength(
      Long.parseLong(fileSizeAndMime.get("fileSize"))
    );
    objectMetadata.setContentType(fileSizeAndMime.get("mimeType"));
    // System.out.println(objectMetadata);
    try {
      String subfolder = bucketPath.split("/")[1];
      String keyPath = subfolder + "/" + filename;

      s3.putObject(bucketPath, filename, inputStream, objectMetadata);
      return "https://" + bucketName + ".s3.amazonaws.com/" + keyPath;
    } catch (AmazonServiceException e) {
      throw new IllegalStateException(e);
    }
  }
  // @Override
  // public List<Object> listObjectsInBucket(String bucket) {
  //   var items = s3
  //     .listObjectsV2(bucket)
  //     .getObjectSummaries()
  //     .stream()
  //     .parallel()
  //     .map(S3ObjectSummary::getKey)
  //     .map(key -> mapS3ToObject(bucket, key))
  //     .collect(Collectors.toList());

  //   log.info("Found " + items.size() + " objects in the bucket " + bucket);
  //   return (List<Object>) items;
  // }
}
