package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /*
     * Persist a new message
     * @param message, a transient message entity.
     * @return a persisted message entity.
     */
    public Message addMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            throw new IllegalArgumentException("Message text can not be empty");
        }

        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message Length can't be more than 255 Characters");
        }

        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("User does not exist");
        }

        return messageRepository.save(message);
    }

    /*
     * return all persisted message entities
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /*
     * return all persisted message entities by messageId
     * @param messageId, the Id of the message to retrieve
     */
    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    /*
     * Update a message
     * @param messageId, the id of the message that will be altered
     * @param newMessageText, the new text of the message to update the current message with
     */
    @Transactional
    public boolean updateMessageById(Integer messageId, Message newMessage) {
        String newMessageText = newMessage.getMessageText();

        if (newMessageText == null || newMessageText.isBlank()) {
            throw new IllegalArgumentException("Message text can not be empty");
        }

        if (newMessageText.length() > 255) {
            throw new IllegalArgumentException("Message Length can not be more than 255 Characters");
        }

        return messageRepository.findById(messageId).map(message -> {
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return true;
        }).orElseThrow(() -> new IllegalArgumentException("Message can not be found"));
    }

    /*
     * Delete Message
     * @param messageId
     */
    @Transactional
    public boolean deleteMessageById(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }

        return false;
    }

    /*
     * return all messages by account Id
     * @param accountId, the account's Id
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
