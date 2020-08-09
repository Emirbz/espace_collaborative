package io.accretio.Services;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Message;
import io.accretio.Repository.MessageRepository;
import io.accretio.Utils.FileUploader;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.XmlParserException;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    public List<Message> getMessages() {
        return messageRepository.listAll();
    }

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

    public void addMessage(Message message) {
        if (message.getType().toString().equals("IMAGE")) {
            try {
                message.setFile(new FileUploader().addImage(message.getFile()));
            } catch (InvalidPortException | InvalidEndpointException | IOException | InvalidKeyException
                    | NoSuchAlgorithmException | InsufficientDataException | InvalidExpiresRangeException
                    | InvalidResponseException | InternalException | XmlParserException | InvalidBucketNameException
                    | ErrorResponseException | RegionConflictException e) {
                e.printStackTrace();
            }
        }

        messageRepository.persist(message);

    }
    @Transactional
    public Message addMessageEventBus(Message message) {
        LOG.info("Message Persisted from evnetBus");

        messageRepository.persist(message);
        return  message;

    }



    public List<Message> findByRoom(int id) {

        return messageRepository.find("room_id", Sort.by("timestamp", Sort.Direction.Ascending), id).list();
    }

    public List<Message> findImagesByRoom(int id) {

        return messageRepository
                .find("type=?1 and room_id=?2", Sort.by("timestamp", Sort.Direction.Ascending), Message.type.IMAGE, id)
                .list();
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);

    }

    public Message getSigneMessage(long id) {
        return messageRepository.findById(id);
    }

}
