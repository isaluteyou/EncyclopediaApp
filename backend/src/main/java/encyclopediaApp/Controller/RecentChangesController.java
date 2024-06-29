package encyclopediaApp.Controller;

import encyclopediaApp.DTO.RecentChanges;
import encyclopediaApp.DTO.RecentChangesByDate;
import encyclopediaApp.Service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recent-changes")
public class RecentChangesController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<RecentChangesByDate>> getRecentChanges() {
        List<RecentChangesByDate> recentChanges = articleService.getRecentChanges();
        return ResponseEntity.ok(recentChanges);
    }
}
