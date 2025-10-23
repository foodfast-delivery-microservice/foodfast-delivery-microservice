package com.example.demo.interfaces.rest;

import com.example.demo.infracstructor.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("test/rabbitMQ")
public class TestController {


    private final MessageProducer messageProducer;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/send")
    public String sendTestMessage(@RequestParam("msg") String message) {
        messageProducer.sendMessage(message);
        return "Message sent: " + message;
    }
}