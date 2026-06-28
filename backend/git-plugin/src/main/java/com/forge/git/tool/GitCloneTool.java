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
public class GitCloneTool implements Tool {

    @Override
    public ToolDescriptor getDescriptor() {
        return new ToolDescriptor(
            "git-clone",
            "Git Clone",
            "Clones a remote git repository to a local path.",
            "Source Control",
            "1.0"
        );
    }

    @Override
    public ToolResult execute(ToolContext context) {
        Instant start = Instant.now();
        Map<String, String> params = context.parameters();
        String remoteUrl = params.get("remoteUrl");
        String localPath = params.get("localPath");

        if (remoteUrl == null || remoteUrl.isBlank()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "'remoteUrl' is required", Duration.ZERO, -1);
        }
        if (localPath == null || localPath.isBlank()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "'localPath' is required", Duration.ZERO, -1);
        }

        File targetDir = new File(localPath);
        if (targetDir.exists() && targetDir.isDirectory() && targetDir.list() != null && targetDir.list().length > 0) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Target directory is not empty: " + localPath, Duration.ZERO, -1);
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("git", "clone", remoteUrl, localPath);
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
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Failed to execute git clone: " + e.getMessage(), Duration.between(start, Instant.now()), -1);
        }
    }
}
