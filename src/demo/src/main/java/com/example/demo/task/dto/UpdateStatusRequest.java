package com.example.demo.task.dto;

import com.example.demo.task.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull(message = "O status não pode ser nulo")TaskStatus status){
    
}

        


