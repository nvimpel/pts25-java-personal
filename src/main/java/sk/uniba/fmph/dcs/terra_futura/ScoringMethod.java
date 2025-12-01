package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ScoringMethod {
    private final int pointsPerCombination;
    private Optional<Integer> calculatedTotal = Optional.empty();
    private final Map<Resource, Integer> requiredResourceCounts;

    /**
     * @param requiredCombination - required combination of resources for which you get points
     * @param pointsPerCombination - how many points do you get per combination
     */
    public ScoringMethod(List<Resource> requiredCombination, int pointsPerCombination) {
        if (requiredCombination == null || requiredCombination.isEmpty()) {
            throw new IllegalArgumentException("Required combination cannot be null or empty.");
        }
        if (pointsPerCombination < 0) {
            throw new IllegalArgumentException("Points per combination must be non-negative.");
        }

        this.pointsPerCombination = pointsPerCombination;

        Map<Resource, Integer> counts = new java.util.HashMap<>();
        for (Resource resource : requiredCombination) {
            counts.put(resource, counts.getOrDefault(resource, 0) + 1);
        }
        this.requiredResourceCounts = counts;
    }

    /**
     * calculate how many points you get from all the resources at the end
     *
     * @param playerResources - total resources at the end
     */
    public void selectThisMethodAndCalculate(List<Resource> playerResources) {
        if (playerResources == null || playerResources.isEmpty()) {
            this.calculatedTotal = Optional.of(0);
            return;
        }

        Map<Resource, Integer> playerResourceCounts = new java.util.HashMap<>();
        for (Resource resource : playerResources) {
            playerResourceCounts.put(resource, playerResourceCounts.getOrDefault(resource, 0) + 1);
        }

        int possibleCombinations = Integer.MAX_VALUE;
        for (Map.Entry<Resource, Integer> requiredEntry : requiredResourceCounts.entrySet()) {
            Resource resourceType = requiredEntry.getKey();
            int requiredCount = requiredEntry.getValue();
            int playerCount = playerResourceCounts.getOrDefault(resourceType, 0);
            if (playerCount == 0 || playerCount < requiredCount) {
                possibleCombinations = 0;
                break;
            }
            int combinationsForThisResource = playerCount / requiredCount;
            possibleCombinations = Math.min(possibleCombinations, combinationsForThisResource);
        }

        int totalScore = possibleCombinations * pointsPerCombination;
        this.calculatedTotal = Optional.of(totalScore);
    }



    /**
     * returns a string representation of the final calculated score, or N/A if not calculated.
     *
     * @return the final calculated score as a String, or "not calculated".
     */
    public String state() {
        return calculatedTotal.map(Object::toString).orElse("not calculated");
    }

    /**
     * returns the final calculated score wrapped in an Optional.
     * The Optional will be empty if the score has not been calculated yet.
     *
     * @return an Optional containing the final calculated score, or an empty Optional.
     */
    public Optional<Integer> getFinalPoints() {
        return calculatedTotal;
    }

}
