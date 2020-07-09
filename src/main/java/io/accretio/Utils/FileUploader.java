package io.accretio.Utils;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import io.minio.http.Method;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;


/*@ApplicationScoped
@Path("/minio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)*/
public class FileUploader {

    InputStream inputStream;

    public String addImage(String img) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException {
        final String imgName = UUID.randomUUID().toString();
        // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.

        MinioClient minioClient = new MinioClient("http://b8db4ca7e65f.ngrok.io", "openvidu", "openvidu");

        // Check if the bucket already exists.
        boolean isExist = minioClient.bucketExists("toutou");
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // Make a new bucket called asiatrip to hold a zip file of photos.
            minioClient.makeBucket("toutou");
        }

        java.nio.file.Path tempFile = Files.createTempFile(imgName, ".jpg");

        if (img.contains("base64")) {
            String[] newImage = img.split(",");

            inputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(newImage[1]));

        } else {


            inputStream = new ByteArrayInputStream(Base64.getMimeDecoder().decode(img));
        }


        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);


        PutObjectOptions options = new PutObjectOptions(Files.size(tempFile), -1);

        minioClient.putObject("toutou", imgName + ".jpg", tempFile.toString(), options);
        Files.delete(tempFile);

        String url = minioClient.getPresignedObjectUrl(Method.GET, "toutou", imgName + ".jpg", 144 * 60 * 60, null);


        System.out.println("File is successfully uploaded ");


        return url;
    }


}