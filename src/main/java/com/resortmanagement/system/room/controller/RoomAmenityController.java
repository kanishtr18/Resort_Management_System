package com.resortmanagement.system.room.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.resortmanagement.system.room.dto.request.RoomAmenityCreateRequest;
import com.resortmanagement.system.room.dto.response.RoomAmenityResponse;
import com.resortmanagement.system.room.service.RoomAmenityService;

@RestController
@RequestMapping("/api/room-amenities")
public class RoomAmenityController {

    private final RoomAmenityService service;

    public RoomAmenityController(RoomAmenityService service) {
        this.service = service;
    }

    @GetMapping
    public List<RoomAmenityResponse> getAll(){
        return service.getAll();
    }
    
    @PostMapping
    public RoomAmenityResponse create(@RequestBody RoomAmenityCreateRequest request) {
        return service.create(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
