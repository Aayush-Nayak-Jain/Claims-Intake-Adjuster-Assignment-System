public class ApprovedState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Claim " + (claim != null ? claim.getClaimId() : "") + " is already APPROVED.");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot reject claim " + (claim != null ? claim.getClaimId() : "") + " after it has been APPROVED.");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        validateAssignedAdjuster(claim, adjusterId);
        claim.transitionTo(new SettledState(), ClaimStatus.SETTLED, adjusterId);
    }

    private void validateAssignedAdjuster(Claim claim, String adjusterId) {
        String currentAssignee = claim.getCurrentAssigneeId();
        if (currentAssignee == null || adjusterId == null || !adjusterId.equals(currentAssignee)) {
            throw new IllegalArgumentException("Unauthorized: Only the assigned adjuster (" + currentAssignee + ") can settle this claim.");
        }
    }
}
