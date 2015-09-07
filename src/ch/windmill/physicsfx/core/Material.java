/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.core;

/**
 *
 * @author jaunerc
 */
public enum Material {
    ROCK(0.6, 0.1),
    BOUNCYBALL(0.3, 0.8);
    
    private final double density;
    private final double restitution;
    
    Material(final double density, final double restitution) {
        this.density = density;
        this.restitution = restitution;
    }
    
    public double getDensity() {
        return density;
    }
    
    public double getRestitution() {
        return restitution;
    }
}
