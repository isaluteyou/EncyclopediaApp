package encyclopediaApp.Service;

import encyclopediaApp.Model.ArchivedArticle;
import encyclopediaApp.Repository.ArchivedArticleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArchivedArticleService {
    @Autowired
    ArchivedArticleRepository archivedArticleRepository;

    public List<ArchivedArticle> getAllArchivedArticles() {
        return archivedArticleRepository.findAll();
    }

    public Optional<ArchivedArticle> getArchivedArticleById(ObjectId id) {
        return archivedArticleRepository.findById(id);
    }

    public List<ArchivedArticle> getArchivedArticlesByTitle(String title) {
        return archivedArticleRepository.findByTitle(title);
    }
}
