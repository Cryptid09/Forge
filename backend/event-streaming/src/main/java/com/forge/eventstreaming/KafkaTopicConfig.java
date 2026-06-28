package com.forge.eventstreaming;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic jobsTopic() {
        return TopicBuilder.name("jobs").partitions(3).replicas(1).build();
    }
    
    @Bean
    public NewTopic jobsDlqTopic() {
        return TopicBuilder.name("jobs-dlq").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic workflowsTopic() {
        return TopicBuilder.name("workflows").partitions(3).replicas(1).build();
    }
    
    @Bean
    public NewTopic workflowsDlqTopic() {
        return TopicBuilder.name("workflows-dlq").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic toolsTopic() {
        return TopicBuilder.name("tools").partitions(3).replicas(1).build();
    }
    
    @Bean
    public NewTopic toolsDlqTopic() {
        return TopicBuilder.name("tools-dlq").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic auditTopic() {
        return TopicBuilder.name("audit").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name("notifications").partitions(1).replicas(1).build();
    }
    
    @Bean
    public NewTopic notificationsDlqTopic() {
        return TopicBuilder.name("notifications-dlq").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic metricsTopic() {
        return TopicBuilder.name("metrics").partitions(1).replicas(1).build();
    }
}
