package encyclopediaApp.Repository;

import encyclopediaApp.Model.ArchivedArticle;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchivedArticleRepository extends MongoRepository<ArchivedArticle, ObjectId> {
}
