package com.forge.git.tool;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
public class GitFetchTool implements Tool {

    @Override
    public ToolDescriptor getDescriptor() {
        return new ToolDescriptor(
            "git-fetch",
            "Git Fetch",
            "Fetches latest changes from the remote repository without merging.",
            "Source Control",
            "1.0"
        );
    }

    @Override
    public ToolResult execute(ToolContext context) {
        Instant start = Instant.now();
        Map<String, String> params = context.parameters();
        String localPath = params.get("localPath");

        if (localPath == null || localPath.isBlank()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "'localPath' is required", Duration.ZERO, -1);
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("git", "fetch");
            pb.directory(new File(localPath));
            pb.redirectErrorStream(true);
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

            if (exitCode == 0) {
                return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, output.toString(), null, duration, exitCode);
            } else {
                return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, output.toString(), duration, exitCode);
            }
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Failed to execute git fetch: " + e.getMessage(), Duration.between(start, Instant.now()), -1);
        }
    }
}
