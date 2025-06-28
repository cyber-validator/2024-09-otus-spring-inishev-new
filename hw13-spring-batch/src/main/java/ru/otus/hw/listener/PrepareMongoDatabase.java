package ru.otus.hw.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import static ru.otus.hw.config.BookStepConfig.BOOKS;
import static ru.otus.hw.config.CommentStepConfig.COMMENTS;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrepareMongoDatabase implements JobExecutionListener {

    private final MongoOperations mongoOperations;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        mongoOperations.dropCollection(BOOKS);
        mongoOperations.dropCollection(COMMENTS);
        log.info("Mongo db preparation is done!");
    }

}
