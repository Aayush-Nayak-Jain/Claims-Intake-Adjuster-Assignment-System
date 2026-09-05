import java.util.List;
import java.util.Optional;

public interface ClaimAssignmentStrategy {
    Optional<Adjuster> assign(Claim claim, List<Adjuster> eligible);
}
