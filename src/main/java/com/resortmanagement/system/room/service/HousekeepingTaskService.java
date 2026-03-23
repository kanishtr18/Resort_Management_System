package com.resortmanagement.system.room.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.resortmanagement.system.hr.entity.Employee;
import com.resortmanagement.system.hr.repository.EmployeeRepository;
import com.resortmanagement.system.room.dto.request.HousekeepingTaskCreateRequest;
import com.resortmanagement.system.room.dto.request.HousekeepingTaskUpdateRequest;
import com.resortmanagement.system.room.dto.response.HousekeepingTaskResponse;
import com.resortmanagement.system.room.entity.HousekeepingTask;
import com.resortmanagement.system.room.entity.Room;
import com.resortmanagement.system.room.mapper.HousekeepingTaskMapper;
import com.resortmanagement.system.room.repository.HousekeepingTaskRepository;
import com.resortmanagement.system.room.repository.RoomRepository;

@Service
public class HousekeepingTaskService {

    private final HousekeepingTaskRepository repo;
    private final RoomRepository roomRepo;
    private final EmployeeRepository empRepo;

    public HousekeepingTaskService(HousekeepingTaskRepository repo,
                                   RoomRepository roomRepo,
                                   EmployeeRepository empRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
        this.empRepo = empRepo;
    }

    public HousekeepingTaskResponse create(HousekeepingTaskCreateRequest req) {

        Room room = roomRepo.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        HousekeepingTask task = new HousekeepingTask();
        task.setRoom(room);
        task.setScheduledAt(req.getScheduledAt());
        task.setPriority(req.getPriority());
        task.setStatus(req.getStatus());
        task.setNotes(req.getNotes());

        if(req.getStaffId() != null) {
            Employee emp = empRepo.findByIdAndDeletedFalse(req.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            task.setStaff(emp);
        }

        return HousekeepingTaskMapper.toResponse(repo.save(task));
    }

    public List<HousekeepingTaskResponse> getAll() {
        return HousekeepingTaskMapper.toResponseList(repo.findByDeletedFalse());
    }

    public HousekeepingTaskResponse update(UUID id, HousekeepingTaskUpdateRequest req) {

        HousekeepingTask task = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        HousekeepingTaskMapper.update(task, req);

        return HousekeepingTaskMapper.toResponse(repo.save(task));
    }

    public void delete(UUID id) {

        HousekeepingTask task = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setDeleted(true);
        repo.save(task);
    }
    public List<HousekeepingTaskResponse> getByStaff(UUID staffId) {
    return HousekeepingTaskMapper.toResponseList(
        repo.findByStaffIdAndDeletedFalse(staffId)
    );
}
}
