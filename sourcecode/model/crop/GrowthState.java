package model.crop;

/**
 * Lifecycle stages a crop moves through.
 * Linear progression: SEED → SEEDLING → MATURE → HARVEST → ROTTEN.
 * DEAD is a state caused by max stress.
 */
public enum GrowthState {
    SEED,
    SEEDLING,
    MATURE,
    HARVEST,
    ROTTEN,
    DEAD
}
