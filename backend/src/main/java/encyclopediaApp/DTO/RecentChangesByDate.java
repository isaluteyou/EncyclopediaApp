package encyclopediaApp.DTO;

import java.time.LocalDate;
import java.util.List;

public class RecentChangesByDate {
    private LocalDate date;
    private List<RecentChanges> changes;

    public RecentChangesByDate(LocalDate date, List<RecentChanges> changes) {
        this.date = date;
        this.changes = changes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<RecentChanges> getChanges() {
        return changes;
    }

    public void setChanges(List<RecentChanges> changes) {
        this.changes = changes;
    }
}
