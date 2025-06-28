package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.converter.JpaToMongoCommentConverter;
import ru.otus.hw.entity.jpa.JpaComment;
import ru.otus.hw.entity.mongo.MongoComment;

@Component
public class CommentStepConfig {

    public static final int CHUNK_SIZE = 50;

    public static final String COMMENTS = "comments";

    @Bean
    public Step commentStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            @Qualifier("commentItemReader") ItemReader<JpaComment> itemReader,
                            @Qualifier("commentItemProcessor") ItemProcessor<JpaComment, MongoComment> itemProcessor,
                            @Qualifier("commentItemWriter") ItemWriter<MongoComment> itemWriter) {
        return new StepBuilder("commentStep", jobRepository)
                .<JpaComment, MongoComment>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean("commentItemReader")
    public ItemReader<JpaComment> itemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<JpaComment>()
                .name("comments")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM JpaComment c")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean("commentItemProcessor")
    public ItemProcessor<JpaComment, MongoComment> itemProcessor(JpaToMongoCommentConverter commentConverter) {
        return commentConverter::convert;
    }

    @Bean("commentItemWriter")
    public ItemWriter<MongoComment> itemWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<MongoComment>()
                .template(mongoOperations)
                .collection(COMMENTS)
                .build();
    }

}
