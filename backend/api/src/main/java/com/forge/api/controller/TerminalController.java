package com.forge.api.controller;

import com.forge.api.dto.TerminalSessionDto;
import com.forge.core.job.Job;
import com.forge.observability.terminal.TerminalService;
import com.forge.observability.terminal.TerminalSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/terminal")
@CrossOrigin("*")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @PostMapping("/session")
    public ResponseEntity<TerminalSessionDto> createSession(@RequestBody(required = false) Map<String, String> body) {
        String cwd = body != null ? body.getOrDefault("workingDirectory", null) : null;
        TerminalSession session = terminalService.createSession(cwd);
        return ResponseEntity.ok(toDto(session));
    }

    @GetMapping("/session")
    public List<TerminalSessionDto> listSessions() {
        return terminalService.listActiveSessions().stream().map(this::toDto).toList();
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<TerminalSessionDto> getSession(@PathVariable String id) {
        try {
            return ResponseEntity.ok(toDto(terminalService.getSession(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity<Void> closeSession(@PathVariable String id) {
        terminalService.closeSession(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/session/{id}/command")
    public ResponseEntity<?> executeCommand(@PathVariable String id,
                                            @RequestBody Map<String, String> body) {
        String command = body.get("command");
        if (command == null || command.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "'command' is required"));
        }
        try {
            Job job = terminalService.executeCommand(id, command);
            return ResponseEntity.ok(Map.of(
                    "jobId", job.getId(),
                    "status", job.getStatus().name(),
                    "output", job.getOutput() != null ? job.getOutput() : ""
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private TerminalSessionDto toDto(TerminalSession s) {
        return new TerminalSessionDto(
                s.getSessionId(), s.getStatus().name(),
                s.getWorkingDirectory(), s.getCreatedAt(),
                s.getLastActivity(), s.getCommandHistory()
        );
    }
}
