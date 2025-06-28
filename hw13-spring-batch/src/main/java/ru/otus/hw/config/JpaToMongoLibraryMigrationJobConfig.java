package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.listener.JobCompletionNotificationListener;
import ru.otus.hw.listener.PrepareMongoDatabase;

@Configuration
@RequiredArgsConstructor
public class JpaToMongoLibraryMigrationJobConfig {

    private static final String JOB_NAME = "postgresToMongoLibraryMigrationJob";

    @Bean
    public Job migrateLibraryJob(JobRepository jobRepository,
                                 Step bookStep,
                                 Step commentStep,
                                 JobCompletionNotificationListener after,
                                 PrepareMongoDatabase before) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .listener(before)
                .listener(after)
                .start(bookStep)
                .next(commentStep)
                .build();
    }

}
