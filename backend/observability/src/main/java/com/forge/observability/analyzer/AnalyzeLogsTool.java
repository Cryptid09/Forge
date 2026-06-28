package com.forge.observability.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Tool: analyze-logs
 *
 * Parameters:
 *   logs   – raw log text to analyze (required)
 *   source – optional label for the log source
 */
@Component
public class AnalyzeLogsTool implements Tool {

    private final LogAnalyzer logAnalyzer;
    private final ObjectMapper objectMapper;

    public AnalyzeLogsTool(LogAnalyzer logAnalyzer, ObjectMapper objectMapper) {
        this.logAnalyzer = logAnalyzer;
        this.objectMapper = objectMapper;
    }

    @Override
    public com.forge.core.tool.ToolDescriptor getDescriptor() {
        return new com.forge.core.tool.ToolDescriptor(
            "analyze-logs",
            "Analyze Logs",
            "Analyses raw log text and produces structured findings.",
            "Observability",
            "1.0"
        );
    }

    @Override
    public ToolResult execute(ToolContext context) {
        Instant start = Instant.now();
        String logs = context.parameters().get("logs");

        if (logs == null || logs.isBlank()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "'logs' parameter is required", Duration.ZERO, -1);
        }

        try {
            AnalysisResult result = logAnalyzer.analyze(logs);
            String json = objectMapper.writeValueAsString(result);
            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, json, null, Duration.between(start, Instant.now()), 0);
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Analysis failed: " + e.getMessage(), Duration.between(start, Instant.now()), -1);
        }
    }
}
