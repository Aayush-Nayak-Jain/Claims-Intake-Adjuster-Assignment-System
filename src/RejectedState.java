public class RejectedState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot approve claim " + (claim != null ? claim.getClaimId() : "") + ": claim is REJECTED (terminal state).");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Claim " + (claim != null ? claim.getClaimId() : "") + " is already REJECTED.");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot settle claim " + (claim != null ? claim.getClaimId() : "") + ": claim is REJECTED (terminal state).");
    }
}
