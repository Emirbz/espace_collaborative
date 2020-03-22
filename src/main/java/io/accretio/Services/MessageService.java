package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Message;
import io.accretio.Models.Room;
import io.accretio.Repository.MessageRepository;
import io.accretio.Repository.RoomRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MessageService {


    @Inject
    private MessageRepository messageRepository;


    public List<Message> getMessages(){
        return messageRepository.listAll();
    }

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);



    public void addMessage(Message message){

        messageRepository.persist(message);


    }


    public  void deleteMessage(Message message)
    {
        messageRepository.delete(message);

    }



}
