package io.accretio.Minio;


import java.util.Map;

public interface MinioFileService {

    Map<String,String> getFileMetaData(String fileId);
    String presignedGetObject(String bucketName,String fileId);




}