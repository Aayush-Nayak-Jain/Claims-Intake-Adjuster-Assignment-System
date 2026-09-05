public class SettledState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot approve claim " + (claim != null ? claim.getClaimId() : "") + ": claim is SETTLED (terminal state).");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot reject claim " + (claim != null ? claim.getClaimId() : "") + ": claim is SETTLED (terminal state).");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Claim " + (claim != null ? claim.getClaimId() : "") + " is already SETTLED.");
    }
}
