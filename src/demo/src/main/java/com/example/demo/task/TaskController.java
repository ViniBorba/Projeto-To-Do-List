package com.example.demo.task;

import com.example.demo.task.dto.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/tasks") @Tag(name="Tarefas")

public class TaskController {
    private final TaskService service;
    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public List<TaskResponse> list(){
        return service.listAll();
    }

    @PostMapping
    public TaskResponse create(@RequestBody TaskRequest req){
        return service.create(req);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody TaskRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id); 
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/done")
    public TaskResponse done(@PathVariable Long id){
        return service.markDone(id);
    }

    //@PatchMapping("/{id}/status")
    //public TaskResponse changeStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest req) {
    //    return service.changeStatus(id, req.status());
    //}

}
