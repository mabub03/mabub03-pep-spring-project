package com.example.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // register for an account
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Account account) {
        try {
            accountService.register(account);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Sucessfully registered");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Username already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage());
            }
        }
    }

    // login to an acount
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        try {
            Account verifiedAccount = accountService.login(account.getUsername(), account.getPassword());

            if (verifiedAccount != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(verifiedAccount);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // get all messages from a specific user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(messages);
    }
    
    // Get all messages
    @GetMapping("/messages")
    public @ResponseBody List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // Get message by message Id
    @GetMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
    }

    // Create a message
    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.addMessage(message);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        
    }

    // Update a message
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Message newMessageText) {
        try {
            boolean isUpdated = messageService.updateMessageById(messageId, newMessageText);
            if (isUpdated) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(1 + " Row(s) Updated");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        
    }

    // Delete a message
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable int messageId) {
        boolean isDeleted = messageService.deleteMessageById(messageId);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(1 + " Row(s) Updated");
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
    }

}
