package encyclopediaApp.Service;

import encyclopediaApp.Events.ArticleEditedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserEditEventListener {

    @Autowired
    private UserService userService;

    @EventListener
    @Transactional
    public void handleArticleEditedEvent(ArticleEditedEvent event) {
        userService.updateUserEdits(event.getUsername());
    }
}