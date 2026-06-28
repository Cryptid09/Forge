package com.forge.observability.terminal;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Tool: execute-command
 *
 * Parameters:
 *   sessionId    – ID of an existing TerminalSession (required)
 *   command      – shell command to execute (required)
 */
@Component
public class ExecuteCommandTool implements Tool {

    private final TerminalSessionRegistry sessionRegistry;

    public ExecuteCommandTool(TerminalSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public com.forge.core.tool.ToolDescriptor getDescriptor() {
        return new com.forge.core.tool.ToolDescriptor(
            "execute-command",
            "Execute Command",
            "Executes a shell command within a terminal session.",
            "Terminal",
            "1.0"
        );
    }

    @Override
    public ToolResult execute(ToolContext context) {
        Instant start = Instant.now();
        Map<String, String> params = context.parameters();

        String sessionId = params.get("sessionId");
        String command   = params.get("command");

        if (command == null || command.isBlank()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "'command' parameter is required", Duration.ZERO, -1);
        }

        TerminalSession session = null;
        String cwd = System.getProperty("user.home");

        if (sessionId != null) {
            session = sessionRegistry.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Terminal session not found: " + sessionId));
            if (session.getStatus() != TerminalSessionStatus.ACTIVE) {
                return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Session " + sessionId + " is closed", Duration.ZERO, -1);
            }
            cwd = session.getWorkingDirectory();
        }

        try {
            // Intercept 'cd' to update session working directory
            if (command.trim().startsWith("cd ") && session != null) {
                String target = command.trim().substring(3).trim();
                File newDir = target.startsWith("/") ? new File(target) : new File(cwd, target);
                if (newDir.isDirectory()) {
                    session.setWorkingDirectory(newDir.getCanonicalPath());
                    session.touch();
                    session.addToHistory(command);
                    return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, "Changed directory to " + newDir.getCanonicalPath(), null, Duration.between(start, Instant.now()), 0);
                } else {
                    return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "cd: no such directory: " + target, Duration.between(start, Instant.now()), -1);
                }
            }

            List<String> cmdList = new ArrayList<>();
            cmdList.add("/bin/sh");
            cmdList.add("-c");
            cmdList.add(command);

            ProcessBuilder pb = new ProcessBuilder(cmdList)
                    .directory(new File(cwd))
                    .redirectErrorStream(true);
            pb.environment().putAll(session != null ? session.getEnvironment() : Map.of());

            Process process = pb.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            Duration duration = Duration.between(start, Instant.now());

            if (session != null) {
                session.touch();
                session.addToHistory(command);
            }

            if (exitCode == 0) {
                return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, output.toString(), null, duration, exitCode);
            } else {
                return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Command exited with code " + exitCode + ":\n" + output, duration, exitCode);
            }

        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Execution error: " + e.getMessage(), Duration.between(start, Instant.now()), -1);
        }
    }
}
