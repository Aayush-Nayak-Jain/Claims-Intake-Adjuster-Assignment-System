public interface ClaimState {
    void approve(Claim claim, String adjusterId);
    void reject(Claim claim, String adjusterId);
    void settle(Claim claim, String adjusterId);
}
