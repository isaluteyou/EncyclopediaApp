package encyclopediaApp.Repository;

import encyclopediaApp.Model.Article;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, ObjectId> {
    Optional<Article> findByTitle(String title);
}