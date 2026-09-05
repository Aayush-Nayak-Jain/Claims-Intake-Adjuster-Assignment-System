public class UnderReviewState implements ClaimState {
    private final String assignedAdjusterId;

    public UnderReviewState() {
        this.assignedAdjusterId = null;
    }

    public UnderReviewState(String assignedAdjusterId) {
        this.assignedAdjusterId = assignedAdjusterId;
    }

    public String getAssignedAdjusterId() {
        return assignedAdjusterId;
    }

    @Override
    public void approve(Claim claim, String adjusterId) {
        validateAssignedAdjuster(claim, adjusterId);
        claim.transitionTo(new ApprovedState(), ClaimStatus.APPROVED, adjusterId);
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        validateAssignedAdjuster(claim, adjusterId);
        claim.transitionTo(new RejectedState(), ClaimStatus.REJECTED, adjusterId);
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot settle claim directly from UNDER_REVIEW state.");
    }

    private void validateAssignedAdjuster(Claim claim, String adjusterId) {
        String currentAssignee = claim.getCurrentAssigneeId();
        if (currentAssignee == null || adjusterId == null || !adjusterId.equals(currentAssignee)) {
            throw new IllegalArgumentException("Unauthorized: Only the assigned adjuster (" + currentAssignee + ") can modify this claim.");
        }
    }
}
