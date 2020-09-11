package io.accretio.Minio;


import java.util.Date;

public class UrlCacheEntry {


    private final long createdAt;
    private int expireIn;
    private String url;


    public UrlCacheEntry(){
        createdAt=new Date().getTime();
    }

    public boolean hasExpired() {
        long effectiveExpiracy=createdAt+ (long) expireIn;
        return !(effectiveExpiracy-new Date().getTime()>0);
    }

    public String getUrl() {
        return url;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn=expireIn;
    }


    public void setUrl(String url) {
        this.url=url;
    }

}
