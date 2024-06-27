package encyclopediaApp.Service;

import encyclopediaApp.DTO.CategoryDetailDTO;
import encyclopediaApp.Model.Article;
import encyclopediaApp.Model.Category;
import encyclopediaApp.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleService articleService;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<CategoryDetailDTO> getCategoryDetail(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            List<Article> articles = articleService.getArticlesByCategory(name);
            return Optional.of(new CategoryDetailDTO(category.get().getName(), category.get().getDescription(), articles));
        } else {
            return Optional.empty();
        }
    }
}
