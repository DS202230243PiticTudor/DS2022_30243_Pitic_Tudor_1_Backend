package com.ds.management.services;

import com.ds.management.domain.dtos.ChatMessageDTO;
import com.ds.management.domain.dtos.ChatMessageSendDTO;
import com.ds.management.domain.dtos.IndividualChatRequestDTO;
import com.ds.management.domain.entities.IndividualChat;
import com.ds.management.domain.entities.Message;
import com.ds.management.domain.entities.Person;
import com.ds.management.domain.repositories.IndividualChatRepository;
import com.ds.management.domain.repositories.MessageRepository;
import com.ds.management.domain.repositories.PersonRepository;
import com.ds.management.exception.domain.ConnectionToSamePersonException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ChatService {
    private final IndividualChatRepository individualChatRepository;
    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChatService(
            IndividualChatRepository individualChatRepository,
            MessageRepository messageRepository,
            PersonRepository personRepository,
            ModelMapper modelMapper
    ) {
        this.individualChatRepository = individualChatRepository;
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    public List<UUID> getIndividualChatsIds(UUID personId) {
        Person person = this.getPersonIfExists(personId);
        List<UUID> individualChatIdList = new ArrayList<>();
        for(Map.Entry<UUID, IndividualChat> set : person.getIndividualChatMap().entrySet()) {
            individualChatIdList.add(set.getValue().getId());
        }
        return individualChatIdList;
    }

    /**
     * Check if people engaged in the chat exist, then check if the chat already exists, if so, fetch it;
     * else create new chat
     * @param dto contains id of sender and peer
     * @return list of all messages in chat if exists, else empty list
     */
    public List<ChatMessageDTO> onConnected(IndividualChatRequestDTO dto) throws ConnectionToSamePersonException {
        if(dto.getPeerId().equals(dto.getSenderId())) {
            throw new ConnectionToSamePersonException("Person cannot connect to itself. Id: " + dto.getPeerId());
        }
        Person person = this.getPersonIfExists(dto.getSenderId());
        Person peer = this.getPersonIfExists(dto.getPeerId());

        IndividualChat individualChat;
        if (person.getIndividualChatMap().containsKey(dto.getPeerId())) {
            individualChat = person.getIndividualChatMap().get(dto.getPeerId());
        } else {
            Set<Person> personSet = new HashSet<>();
            personSet.add(person);
            personSet.add(peer);
            individualChat = IndividualChat.builder()
                    .personSet(personSet)
                    .messages(new ArrayList<>())
                    .build();
            this.individualChatRepository.save(individualChat);
            person.getIndividualChatMap().put(dto.getPeerId(), individualChat);
            peer.getIndividualChatMap().put(dto.getSenderId(), individualChat);
            this.personRepository.save(person);
            this.personRepository.save(peer);
        }
        List<ChatMessageDTO> messageDTOList = new ArrayList<>();
        if (!individualChat.getMessages().isEmpty()){
            individualChat.getMessages().forEach(message -> {
                ChatMessageDTO messageDTO = this.modelMapper.map(message, ChatMessageDTO.class);
                messageDTO.setIndividualChatId(individualChat.getId());
                messageDTOList.add(messageDTO);
            });
        }
        return messageDTOList;
    }

    public ChatMessageDTO sendMessage(ChatMessageSendDTO dto) {
        Person person = this.getPersonIfExists(dto.getSenderId());
        Person peer = this.getPersonIfExists(dto.getPeerId());
        IndividualChat individualChat = person.getIndividualChatMap().get(peer.getId());
        if(individualChat == null) {
            throw new EntityNotFoundException(IndividualChat.class.getSimpleName() + " with of person " + person.getUsername() +
                    " with peer " + peer.getUsername());
        }
        Message message = Message.builder()
                .content(dto.getContent())
                .recipientId(dto.getPeerId())
                .sentAt(LocalDateTime.now())
                .individualChat(individualChat)
                .seen(false)
                .build();
        messageRepository.save(message);
        individualChat.getMessages().add(message);
        individualChatRepository.save(individualChat);
        ChatMessageDTO messageDTO = this.modelMapper.map(message, ChatMessageDTO.class);
        messageDTO.setIndividualChatId(individualChat.getId());
        return messageDTO;
    }

    private Person getPersonIfExists(UUID senderId) throws EntityNotFoundException {
        Optional<Person> personOptional = this.personRepository.findById(senderId);
        if(personOptional.isEmpty()) {
            throw new EntityNotFoundException(Person.class.getSimpleName() + " with id: " + senderId);
        }
        return personOptional.get();
    }
}
