package pl.jordii.mccratessystem.database.model;

import java.awt.*;

public enum ParticlesAnimationType {
    BOUNDING_BOX("BoundingBox");

    private String name;

    ParticlesAnimationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
