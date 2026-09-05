import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Claim {
    private String claimId;
    private String policyholderId;
    private String policyId;
    private ClaimType claimType;
    private double claimedAmount;
    private String description;
    private ClaimStatus currentStatus;
    private ClaimState currentState;
    private String currentAssigneeId;
    private final List<StatusEvent> history;

    public Claim(String claimId, String policyholderId, String policyId, ClaimType claimType, double claimedAmount, String description) {
        this.claimId = claimId;
        this.policyholderId = policyholderId;
        this.policyId = policyId;
        this.claimType = claimType;
        this.claimedAmount = claimedAmount;
        this.description = description;
        this.currentStatus = ClaimStatus.FILED;
        this.currentState = new FiledState();
        this.currentAssigneeId = null;
        this.history = new ArrayList<>();
        
        // Record initial FILED status event
        addHistoryEvent(new StatusEvent(LocalDateTime.now(), ClaimStatus.FILED, null));
    }

    public String getClaimId() {
        return claimId;
    }

    public String getPolicyholderId() {
        return policyholderId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public double getClaimedAmount() {
        return claimedAmount;
    }

    public String getDescription() {
        return description;
    }

    public ClaimStatus getCurrentStatus() {
        return currentStatus;
    }

    public ClaimState getCurrentState() {
        return currentState;
    }

    public String getCurrentAssigneeId() {
        return currentAssigneeId;
    }

    public void setCurrentAssigneeId(String currentAssigneeId) {
        this.currentAssigneeId = currentAssigneeId;
    }

    public List<StatusEvent> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void addHistoryEvent(StatusEvent event) {
        if (event != null) {
            this.history.add(event);
        }
    }

    public void transitionTo(ClaimState newState, ClaimStatus newStatus, String adjusterId) {
        this.currentState = newState;
        this.currentStatus = newStatus;
        if (adjusterId != null) {
            this.currentAssigneeId = adjusterId;
        }
        addHistoryEvent(new StatusEvent(LocalDateTime.now(), newStatus, this.currentAssigneeId));
    }

    // State pattern delegates
    public void approve(String adjusterId) {
        this.currentState.approve(this, adjusterId);
    }

    public void reject(String adjusterId) {
        this.currentState.reject(this, adjusterId);
    }

    public void settle(String adjusterId) {
        this.currentState.settle(this, adjusterId);
    }

    public double calculatePayout(Policy policy) {
        if (policy == null || (currentStatus != ClaimStatus.APPROVED && currentStatus != ClaimStatus.SETTLED)) {
            return 0.0;
        }
        double afterDeductible = claimedAmount - policy.getDeductible();
        double payout = Math.min(afterDeductible, policy.getCoverageLimit());
        return Math.max(payout, 0.0);
    }
}
