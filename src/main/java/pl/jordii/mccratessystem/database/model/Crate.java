package pl.jordii.mccratessystem.database.model;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.List;

public class Crate {

    private String crateName;
    private String displayName;
    private List<ItemStack> items;
    private boolean status;
    private ParticlesAnimationType particlesAnimationType;
    private String particlesColor;

    public Crate(String crateName, String displayName, List<ItemStack> items, boolean status, ParticlesAnimationType particlesAnimationType, String particlesColor) {
        this.crateName = crateName;
        this.displayName = displayName;
        this.items = items;
        this.status = status;
        this.particlesAnimationType = particlesAnimationType;
        this.particlesColor = particlesColor;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCrateName() {
        return crateName;
    }

    public void setCrateName(String crateName) {
        this.crateName = crateName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public ParticlesAnimationType getParticlesAnimationType() {
        return particlesAnimationType;
    }

    public void setParticlesAnimationType(ParticlesAnimationType particlesAnimationType) {
        this.particlesAnimationType = particlesAnimationType;
    }

    public String getParticlesColor() {
        return particlesColor;
    }

    public void setParticlesColor(String particlesColor) {
        this.particlesColor = particlesColor;
    }
}
