package ch.windmill.physicsfx.core;

/**
 * This class represents mass values for a body. It contains the raw mass value and the inverse mass value.
 * These values will be calculated based on the density and volume of the body.
 * 
 * If infinite mass is needed, set the mass value on 0.0.
 * 
 * Created by jaunerc on 30.08.15.
 */
public class MassData {
    private final double mass;
    private double inv_mass;
    
    /**
     * Create new mass data object. Use the are instead of volume for a 2D body.
     * The inversed mass will be calculated automatically. 
     * @param density of the body
     * @param volume of the body
     */
    public MassData(final double density, final double volume) {
        mass = density * volume;

        calcInversedMass();
    }
    
    /**
     * Calculate the inversed mass value based on the raw mass data.
     * The inversed mass is defined by 1 / mass.
     */
    private void calcInversedMass() {
        if(mass == 0) {
            inv_mass = 0;
        } else {
            inv_mass = 1 / mass;
        }
    }
    
    /**
     * Get the raw mass value.
     * @return raw mass
     */
    public double getMass() {
        return mass;
    }
    
    /**
     * Get the inversed mass value.
     * @return inversed mass
     */
    public double getInv_mass() {
        return inv_mass;
    }
}
