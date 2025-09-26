package com.example.demo.task;

import com.example.demo.task.dto.TaskRequest;
import com.example.demo.task.dto.TaskResponse;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository repo, UserRepository userRepo){
        this.repo = repo;
        this.userRepo = userRepo;
    }

    private User current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }

        String username = auth.getName();
        if (username == null || "anonymousUser".equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }

    public List<TaskResponse> listAll(){
        return repo.findByOwner(current()).stream().map(this::toDto).toList();
    }

    public TaskResponse create(TaskRequest req){
        var t = Task.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                .dataCriacao(Instant.now())
                .dataVencimento(req.dataVencimento())
                .status(req.status() == null ? TaskStatus.PENDENTE : req.status())
                .owner(current())
                .build();
        return toDto(repo.save(t));
    }

    public TaskResponse update(Long id, TaskRequest req){
        var t = findOwned(id);
        t.setTitulo(req.titulo());
        t.setDescricao(req.descricao());
        t.setDataVencimento(req.dataVencimento());
        t.setStatus(req.status());
        return toDto(repo.save(t));
    }

    public void delete(Long id){
        var t = findOwned(id);
        repo.delete(t);
    }

    public TaskResponse markDone(Long id){
        var t = findOwned(id);
        t.setStatus(TaskStatus.CONCLUIDA);
        return toDto(repo.save(t));
    }

    private Task findOwned(Long id){
        var t = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Task não encontrada"));
        if (!t.getOwner().getUsername().equals(current().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Task não pertence ao usuário");
        }
        return t;
    }

    private TaskResponse toDto(Task t){
        return new TaskResponse(t.getId(), t.getTitulo(), t.getDescricao(),
                t.getDataCriacao(), t.getDataVencimento(), t.getStatus());
    }

}
