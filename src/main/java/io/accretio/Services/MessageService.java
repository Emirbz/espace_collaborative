package io.accretio.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.accretio.Config.LoggingFilter;
import io.accretio.Minio.MinioFileService;
import io.accretio.Models.File;
import io.accretio.Models.Message;
import io.accretio.Repository.MessageRepository;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    @Inject
    MinioFileService minioFileService;

    private ObjectMapper objectMapper;

    public List<Message> getMessages() {
        return messageRepository.listAll();
    }

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

    public void addMessage(Message message) {
        /*if (message.getType().toString().equals("IMAGE")) {
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
*/
    }

    @Transactional
    public Message addMessageEventBus(Message message) {
        LOG.info("Message Persisted from evnetBus");

        messageRepository.persist(message);
        LOG.info("Type : "+message.getType());
        if (!(message.getType().equals(Message.type.TEXT) && (message.getType().equals(Message.type.SONDAGE))))  {
            LOG.info("MAHOUCH TEXT");
            setMessageMetaData(message);
        }
        return message;

    }


    public List<Message> findByRoom(int id) {
        this.objectMapper = new ObjectMapper();

        List<Message> messageList = messageRepository.find("room_id", Sort.by("timestamp", Sort.Direction.Ascending), id).list();
        messageList.stream().filter(m -> !m.getType().equals(Message.type.TEXT)).forEach(this::setMessageMetaData);
        return messageList;
    }

    public List<Message> findImagesByRoom(int id) {

        return messageRepository
                .find("type=?1 and room_id=?2", Sort.by("timestamp", Sort.Direction.Ascending), Message.type.IMAGE, id)
                .list();
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);

    }

    public void setMessageMetaData(Message message)
    {
        Map<String,String> stringStringMap = minioFileService.getFileMetaData(message.getFile());
        LOG.info("---------- META DATA --------- ");
        LOG.info(message.getFile());
        LOG.info(stringStringMap);
        message.setMetaData(objectMapper.convertValue(minioFileService.getFileMetaData(message.getFile()), File.class));

    }

    public Message getSigneMessage(long id) {
        return messageRepository.findById(id);
    }

}
