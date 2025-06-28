package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.listener.JobCompletionNotificationListener;

@Configuration
@RequiredArgsConstructor
public class JpaToMongoLibraryMigrationJobConfig {

    private static final String JOB_NAME = "postgresToMongoLibraryMigrationJob";

    @Bean
    public Job migrateLibraryJob(JobRepository jobRepository,
                                 Step bookStep,
                                 Step commentStep,
                                 JobCompletionNotificationListener listener) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .listener(listener)
                .start(bookStep)
                .next(commentStep)
                .build();
    }

}
