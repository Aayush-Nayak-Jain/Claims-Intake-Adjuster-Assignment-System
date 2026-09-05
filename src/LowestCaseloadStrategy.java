import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LowestCaseloadStrategy implements ClaimAssignmentStrategy {

    @Override
    public Optional<Adjuster> assign(Claim claim, List<Adjuster> eligible) {
        if (claim == null || eligible == null || eligible.isEmpty()) {
            return Optional.empty();
        }

        return eligible.stream()
                .filter(adj -> adj.specializes(claim.getClaimType()) && adj.hasCapacity())
                .min(Comparator.comparingInt(Adjuster::getCurrentCaseload)
                        .thenComparing(Adjuster::getAdjusterId));
    }
}
