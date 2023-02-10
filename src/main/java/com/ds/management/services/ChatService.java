package com.ds.management.services;

import com.ds.management.domain.dtos.ChatMessageDTO;
import com.ds.management.domain.dtos.IndividualChatRequestDTO;
import com.ds.management.domain.entities.IndividualChat;
import com.ds.management.domain.entities.Message;
import com.ds.management.domain.entities.Person;
import com.ds.management.domain.repositories.IndividualChatRepository;
import com.ds.management.domain.repositories.MessageRepository;
import com.ds.management.domain.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {
    private IndividualChatRepository individualChatRepository;
    private MessageRepository messageRepository;
    private PersonRepository personRepository;

    @Autowired
    public ChatService(
            IndividualChatRepository individualChatRepository,
            MessageRepository messageRepository,
            PersonRepository personRepository
    ) {
        this.individualChatRepository = individualChatRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }

    public List<ChatMessageDTO> onConnected(IndividualChatRequestDTO dto) {
        Optional<Person> personOptional = this.personRepository.findById(dto.getSenderId());
        if(personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + dto.getSenderId());
        }
        Person person = personOptional.get();
        IndividualChat individualChat;
        if (person.getIndividualChatMap().containsKey(dto.getPeerID())) {
            individualChat = person.getIndividualChatMap().get(dto.getPeerID());
        }
        return null;
    }
}
