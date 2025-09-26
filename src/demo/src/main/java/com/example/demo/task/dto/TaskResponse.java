package com.example.demo.task.dto;

import java.time.Instant;
import java.time.LocalDate;
import com.example.demo.task.TaskStatus;

public record TaskResponse(Long id, String titulo, String descricao, Instant dataCriacao, LocalDate dataVencimento, TaskStatus status) {

}
