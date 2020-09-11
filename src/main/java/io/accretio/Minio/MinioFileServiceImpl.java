package io.accretio.Minio;


import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MinioFileServiceImpl implements MinioFileService {

    Map<String, UrlCacheEntry> urlsCache;

    @Inject
    MinioClient minioClient;

    @PostConstruct
    public void init() {

        urlsCache = new HashMap<>();

    }


    @Override
    public Map<String, String> getFileMetaData(String fileId) {

        Map<String, String> metadata = new HashMap<>();
        if (fileId != null && !fileId.isEmpty()) {
            ObjectStat objectStat;
            try {

                objectStat = minioClient.statObject(MinioClientProvider.documentBucketName, fileId);

                if (objectStat.httpHeaders().containsKey("content-disposition") || (objectStat.httpHeaders().containsKey("Content-Disposition"))) {

                    String contentDispositionKey = "content-disposition";

                    if (objectStat.httpHeaders().containsKey("Content-Disposition")) {
                        contentDispositionKey = "Content-Disposition";
                    }
                    String contentDispositionHeader = objectStat.httpHeaders().get(contentDispositionKey).get(0);

                    String fileName = getFileNameFromContentDispositionHeader(contentDispositionHeader);

                    metadata.put("fileName", fileName);
                }

                String contentTypeKey = "content-type";
                if (objectStat.httpHeaders().containsKey("Content-Type")) {
                    contentTypeKey = "Content-Type";
                }
                String mimeType = objectStat.httpHeaders().get(contentTypeKey).toArray()[0].toString();

                metadata.put("mimeType", mimeType);


              /*  String thumbnail = "";
                if (fileId.contains(".")) {
                    thumbnail = fileId.replace(fileId.substring(fileId.lastIndexOf(".")), "_thumb.webp");
                } else {
                    thumbnail = fileId + "thumb.webp";
                }
                metadata.put("thumbnail", presignedGetObject(MinioClientProvider.documentBucketName, thumbnail));*/

                metadata.put("presignedUrl", presignedGetObject(MinioClientProvider.documentBucketName, fileId));

            } catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | ErrorResponseException | InternalException | InvalidResponseException | NullPointerException | IOException | NoResponseException | XmlPullParserException | InvalidArgumentException e) {
                e.printStackTrace();
            }
        }
        metadata.put("objectId", fileId);
        return metadata;
    }

    private String getFileNameFromContentDispositionHeader(String contentDispositionHeader) {

        String[] headerParts = contentDispositionHeader.split(";");

        for (String name : headerParts) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                return tmp[1].trim().replaceAll("\"", "");

            }
        }
        return null;
    }

    @Override
    public String presignedGetObject(String bucketName, String fileId) {

      /*  ObjectStat objectStat = null;
        String webpImageId = FilenameUtils.removeExtension(fileId) + "_webp.webp";
        try {
            objectStat = minioClient.statObject(MinioClientProvider.documentBucketName, webpImageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (objectStat != null) {
            fileId = webpImageId;
        }*/

        int expireIn = (60 * 60 * 10);
        String shareLinkUrl = null;
        String urlCacheKey = bucketName + "-" + fileId;

        if ((urlsCache.containsKey(urlCacheKey)) && (!urlsCache.get(urlCacheKey).hasExpired())) {
            Logger.getLogger(MinioFileService.class).info("FOUND " + fileId + " IN CACHE");
            UrlCacheEntry entry = urlsCache.get(urlCacheKey);
            return entry.getUrl();

        } else {

            try {
                UrlCacheEntry urlCacheEntry = new UrlCacheEntry();

                shareLinkUrl = minioClient.presignedGetObject(bucketName, fileId, expireIn);
                urlCacheEntry.setExpireIn(expireIn);
                urlCacheEntry.setUrl(shareLinkUrl);
                urlsCache.put(urlCacheKey, urlCacheEntry);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return shareLinkUrl;
    }

}
