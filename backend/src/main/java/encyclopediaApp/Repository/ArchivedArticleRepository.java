package encyclopediaApp.Repository;

import encyclopediaApp.Model.ArchivedArticle;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchivedArticleRepository extends MongoRepository<ArchivedArticle, ObjectId> {
    List<ArchivedArticle> findByTitle(String title);
}
