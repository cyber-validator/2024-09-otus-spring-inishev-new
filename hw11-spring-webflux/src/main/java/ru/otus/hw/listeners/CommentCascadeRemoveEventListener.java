package ru.otus.hw.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CommentCascadeRemoveEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Object> event) {
//        super.onAfterDelete(event);
        if (Objects.equals(event.getType(), Book.class)) {
            String id = event.getSource().getString("_id");

            var comments = mongoOperations.findAllAndRemove(query(where("book.$id").is(id)), Comment.class);

            System.out.println();
            //book
//            mongoOperations.save(((User) source).getEmailAddress());
        }
    }

}
