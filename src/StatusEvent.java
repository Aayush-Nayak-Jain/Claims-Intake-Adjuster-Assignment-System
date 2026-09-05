import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatusEvent {
    private final LocalDateTime timestamp;
    private final ClaimStatus status;
    private final String assignedAdjusterId;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatusEvent(LocalDateTime timestamp, ClaimStatus status, String assignedAdjusterId) {
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.status = status;
        this.assignedAdjusterId = assignedAdjusterId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public String getAssignedAdjusterId() {
        return assignedAdjusterId;
    }

    @Override
    public String toString() {
        String formattedTime = timestamp.format(FORMATTER);
        String assigneeInfo = (assignedAdjusterId != null && !assignedAdjusterId.isEmpty())
                ? "assigned to " + assignedAdjusterId
                : "no assignee";
        return String.format("[%s] %s (%s)", formattedTime, status, assigneeInfo);
    }
}
