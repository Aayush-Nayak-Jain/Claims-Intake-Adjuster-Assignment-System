import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class ClaimsManager {
    private final Map<String, Policy> policies;
    private final Map<String, Adjuster> adjusters;
    private final Map<String, Claim> claims;
    private final Queue<Claim> queuedClaims;
    private int claimCounter = 1;
    private ClaimAssignmentStrategy defaultStrategy = new LowestCaseloadStrategy();

    public ClaimsManager() {
        this.policies = new HashMap<>();
        this.adjusters = new HashMap<>();
        this.claims = new HashMap<>();
        this.queuedClaims = new ArrayDeque<>();
        initializeData();
    }

    private synchronized String generateClaimId() {
        return String.format("CLM%03d", claimCounter++);
    }

    public void initializeData() {
        policies.clear();
        adjusters.clear();
        claims.clear();
        queuedClaims.clear();
        claimCounter = 1;

        // 3 sample policies (Auto, Health, Property)
        Policy pol1 = new AutoPolicy("POL001", 500.0, 50000.0, true);
        Policy pol2 = new HealthPolicy("POL002", 1000.0, 100000.0, true);
        Policy pol3 = new PropertyPolicy("POL003", 2000.0, 200000.0, true);

        policies.put(pol1.getPolicyId(), pol1);
        policies.put(pol2.getPolicyId(), pol2);
        policies.put(pol3.getPolicyId(), pol3);

        // 3 sample adjusters with specializations & max caseloads
        Adjuster adj1 = new Adjuster("ADJ001", 5);
        adj1.addSpecialization(ClaimType.AUTO);
        adj1.addSpecialization(ClaimType.HEALTH);

        Adjuster adj2 = new Adjuster("ADJ002", 5);
        adj2.addSpecialization(ClaimType.PROPERTY);

        Adjuster adj3 = new Adjuster("ADJ003", 3);
        adj3.addSpecialization(ClaimType.AUTO);

        adjusters.put(adj1.getAdjusterId(), adj1);
        adjusters.put(adj2.getAdjusterId(), adj2);
        adjusters.put(adj3.getAdjusterId(), adj3);
    }

    public ClaimAssignmentStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    public void setDefaultStrategy(ClaimAssignmentStrategy defaultStrategy) {
        if (defaultStrategy != null) {
            this.defaultStrategy = defaultStrategy;
        }
    }

    public String fileClaim(String policyId, String policyholderId, ClaimType type, double claimedAmount, String description) {
        // Step 1: Does policy exist?
        Policy policy = policies.get(policyId);
        if (policy == null) {
            return "Error: Policy " + policyId + " does not exist.";
        }

        // Step 2: Is policy active?
        if (!policy.isActive()) {
            return "Error: Policy " + policyId + " is inactive or expired.";
        }

        // Step 3: Does policy cover this claim type?
        if (!policy.covers(type)) {
            return "Error: Policy " + policyId + " does not cover " + type + " claims.";
        }

        // Step 4: All validation checks passed -> create Claim object
        String claimId = generateClaimId();
        Claim claim = new Claim(claimId, policyholderId, policyId, type, claimedAmount, description);
        claims.put(claimId, claim);

        // Immediately attempt automatic adjuster assignment via strategy
        boolean assigned = assignClaim(claim, defaultStrategy);

        if (assigned) {
            return "Success: Claim " + claimId + " filed successfully and assigned to " + claim.getCurrentAssigneeId() + " (Status: UNDER_REVIEW).";
        } else {
            return "Success: Claim " + claimId + " filed successfully. No eligible adjuster available; claim queued (Status: FILED).";
        }
    }

    public boolean assignClaim(Claim claim, ClaimAssignmentStrategy strategy) {
        if (claim == null) {
            return false;
        }
        if (strategy == null) {
            strategy = defaultStrategy;
        }

        List<Adjuster> eligible = adjusters.values().stream()
                .filter(adj -> adj.specializes(claim.getClaimType()))
                .collect(Collectors.toList());

        Optional<Adjuster> assigned = strategy.assign(claim, eligible);

        if (assigned.isPresent()) {
            Adjuster adjuster = assigned.get();
            claim.setCurrentAssigneeId(adjuster.getAdjusterId());
            adjuster.incrementCaseload();
            claim.transitionTo(new UnderReviewState(adjuster.getAdjusterId()), ClaimStatus.UNDER_REVIEW, adjuster.getAdjusterId());
            return true;
        } else {
            if (!queuedClaims.contains(claim)) {
                queuedClaims.offer(claim);
            }
            return false;
        }
    }

    public int retryQueuedClaims(ClaimAssignmentStrategy strategy) {
        if (strategy == null) {
            strategy = defaultStrategy;
        }
        int assignedCount = 0;
        int size = queuedClaims.size();
        for (int i = 0; i < size; i++) {
            Claim claim = queuedClaims.poll();
            if (claim == null) break;
            boolean assigned = assignClaim(claim, strategy);
            if (assigned) {
                assignedCount++;
            }
        }
        return assignedCount;
    }

    public Map<String, Policy> getPolicies() {
        return Collections.unmodifiableMap(policies);
    }

    public Map<String, Adjuster> getAdjusters() {
        return Collections.unmodifiableMap(adjusters);
    }

    public Map<String, Claim> getClaims() {
        return Collections.unmodifiableMap(claims);
    }

    public Queue<Claim> getQueuedClaims() {
        return queuedClaims;
    }

    public Policy getPolicy(String policyId) {
        return policies.get(policyId);
    }

    public Adjuster getAdjuster(String adjusterId) {
        return adjusters.get(adjusterId);
    }

    public Claim getClaim(String claimId) {
        return claims.get(claimId);
    }

    public void addPolicy(Policy policy) {
        if (policy != null) {
            policies.put(policy.getPolicyId(), policy);
        }
    }

    public void addAdjuster(Adjuster adjuster) {
        if (adjuster != null) {
            adjusters.put(adjuster.getAdjusterId(), adjuster);
        }
    }

    public void printSeedSummary() {
        System.out.println("=== Policies Loaded (" + policies.size() + ") ===");
        for (Policy p : policies.values()) {
            System.out.println("  ID: " + p.getPolicyId() + " | Type: " + p.getClass().getSimpleName()
                    + " | Deductible: $" + p.getDeductible() + " | Limit: $" + p.getCoverageLimit()
                    + " | Active: " + p.isActive());
        }
        System.out.println("\n=== Adjusters Loaded (" + adjusters.size() + ") ===");
        for (Adjuster a : adjusters.values()) {
            System.out.println("  ID: " + a.getAdjusterId() + " | Specs: " + a.getSpecializations()
                    + " | Caseload: " + a.getCurrentCaseload() + "/" + a.getMaxCaseload());
        }
    }
}


