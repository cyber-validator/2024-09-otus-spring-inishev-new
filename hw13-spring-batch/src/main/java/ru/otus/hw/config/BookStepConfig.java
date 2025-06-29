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
import ru.otus.hw.converter.JpaToMongoBookConverter;
import ru.otus.hw.entity.jpa.JpaBook;
import ru.otus.hw.entity.mongo.MongoBook;

@Component
public class BookStepConfig {

    public static final int CHUNK_SIZE = 100;

    public static final String BOOKS = "books";

    @Bean
    public Step bookStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         @Qualifier("bookItemReader") ItemReader<JpaBook> itemReader,
                         @Qualifier("bookItemProcessor") ItemProcessor<JpaBook, MongoBook> itemProcessor,
                         @Qualifier("bookItemWriter") ItemWriter<MongoBook> itemWriter) {
        return new StepBuilder("bookStep", jobRepository)
                .<JpaBook, MongoBook>chunk(CHUNK_SIZE, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean("bookItemReader")
    public ItemReader<JpaBook> itemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<JpaBook>()
                .name("books")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT b FROM JpaBook b LEFT JOIN FETCH b.author LEFT JOIN FETCH b.genres")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean("bookItemProcessor")
    public ItemProcessor<JpaBook, MongoBook> itemProcessor(JpaToMongoBookConverter bookConverter) {
        return bookConverter::convert;
    }

    @Bean("bookItemWriter")
    public ItemWriter<MongoBook> itemWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<MongoBook>()
                .template(mongoOperations)
                .collection(BOOKS)
                .build();
    }

}
