package io.accretio.Minio;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.accretio.Minio.Config.ApplicationPropertyConfiguration;
import io.minio.MinioClient;


@Singleton
public class MinioClientProvider {

    public static String documentBucketName = "documents";
    public static String prviewBucketName = "converted";

    @Inject
    ApplicationPropertyConfiguration propertyConfiguration;


    MinioClient minioClient;

    @Produces
    MinioClient createClient() {

        if (minioClient==null){
            try {
                minioClient = new MinioClient(propertyConfiguration.minioUrl(), propertyConfiguration.minioServerKey(), propertyConfiguration.minioSecret());

            } catch (Exception e) {
                return null;
            }
        }
        return minioClient;

    }



}
