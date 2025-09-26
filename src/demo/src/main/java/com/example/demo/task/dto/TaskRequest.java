package com.example.demo.task.dto;

import java.time.LocalDate;
import com.example.demo.task.TaskStatus;

public record TaskRequest(String titulo, String descricao, LocalDate dataVencimento, TaskStatus status) {

}
