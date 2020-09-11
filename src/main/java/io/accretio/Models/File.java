package io.accretio.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

public class File extends PanacheEntityBase  {
    public enum FileType {
        IMAGE,
        VIDEO,
        AUDIO,
        FILE,
            }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String objectId;
    private String mimeType;
    private String presignedUrl;
    private String fileName;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public void setPresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
