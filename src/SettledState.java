public class SettledState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot approve a claim that has already been SETTLED.");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot reject a claim that has already been SETTLED.");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Claim is already SETTLED.");
    }
}
