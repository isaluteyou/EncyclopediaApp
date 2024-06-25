package encyclopediaApp.Repository;

import encyclopediaApp.Model.Article;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, ObjectId> {
    Optional<Article> findByTitle(String title);
    List<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
    List<Article> findByEditHistoryUsername(String username);
}