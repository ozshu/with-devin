package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MessageController {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @GetMapping("/message")
    public String getMessagePage(@RequestParam("message_id") Integer messageId, Model model) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            model.addAttribute("message", message.get());
        } else {
            model.addAttribute("error", "Message not found with ID: " + messageId);
        }
        return "message";
    }
    
    @GetMapping("/api/message")
    @ResponseBody
    public ResponseEntity<?> getMessageApi(@RequestParam("message_id") Integer messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            return ResponseEntity.ok(message.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
