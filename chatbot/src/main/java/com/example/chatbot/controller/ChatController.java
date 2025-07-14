
package com.example.chatbot.controller;

import com.example.chatbot.DTOs.ChatRequest;
import com.example.chatbot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired ChatService chatService;

    @PostMapping
    public ResponseEntity<String> handleChat(@RequestBody ChatRequest request) {
        String response = chatService.processMessage(request.getMessage());
        return ResponseEntity.ok(response);
    }
}
