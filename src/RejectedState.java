public class RejectedState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot approve a claim that has been REJECTED.");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Claim is already REJECTED.");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot settle a claim that has been REJECTED.");
    }
}
