package com.example.demo.task;

import com.example.demo.user.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity @Table(name="tasks")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor @Builder
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String titulo;
    @Column(length = 4000) private String descricao;
    @Column(nullable = false) private Instant dataCriacao;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;

}
