/*
CommunicationController.java
Purpose:
 - Log or send communications (email/SMS) to guests; store sent communications for audit.
Endpoints:
 - POST /api/communications/send -> send message (email/sms) and store record
 - GET /api/communications?guestId=...
Responsibilities:
 - Use Integration module (email/SMS provider) to actually send messages.
 - Store communication in DB for audit trail (Communication entity).
File: support/controller/CommunicationController.java
*/
package com.resortmanagement.system.support.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resortmanagement.system.support.dto.request.CommunicationCreateRequest;
import com.resortmanagement.system.support.dto.response.CommunicationResponse;
import com.resortmanagement.system.support.service.CommunicationService;

@RestController
@RequestMapping("/api/communications")
public class CommunicationController {

    private final CommunicationService service;

    public CommunicationController(CommunicationService service) {
        this.service = service;
    }

    @PostMapping
    public CommunicationResponse create(@RequestBody CommunicationCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<CommunicationResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
