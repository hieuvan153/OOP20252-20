package model.soil;

import model.crop.Crop;

public interface Pest {
    void attackTargetCrop(Crop crop);

    /** Display name shown on the cell tooltip. */
    String getName();
}
