package io.accretio.Minio.Config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@ConfigProperties (prefix = "espace_collaborative")
public interface ApplicationPropertyConfiguration {

    @ConfigProperty(name = "application.name")
    String applicationName(); 

    @ConfigProperty(name = "minio.url")
    String minioUrl();

    @ConfigProperty(name = "minio.server.key")
    String minioServerKey();

    @ConfigProperty(name = "minio.server.secret")
    String minioSecret();

}