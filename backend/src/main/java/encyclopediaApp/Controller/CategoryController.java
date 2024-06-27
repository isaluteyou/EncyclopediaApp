package encyclopediaApp.Controller;

import encyclopediaApp.DTO.CategoryDetailDTO;
import encyclopediaApp.Model.Article;
import encyclopediaApp.Model.Category;
import encyclopediaApp.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<CategoryDetailDTO> getCategoryDetail(@PathVariable String categoryName) {
        Optional<CategoryDetailDTO> category = categoryService.getCategoryDetail(categoryName);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
