package ch.windmill.physicsfx.core;

/**
 * Represents a basic body. A body has vectors for position, velocity and acceleration forces.
 * The materiel defines the density and restitution of the body. To make the body visible in a room,
 * it will need a shape. Per default it will have an aabb shape.
 *
 * Created by jaunerc on 15.08.15.
 */
public class Body {
    
    public Vector2D pos;
    protected Vector2D velocity;
    
    private Vector2D force;
    private MassData massData;
    private Shape shape;
    private Material material;

    /**
     * Create a new body. Velocity and force will be set to zero.
     * @param pos the position of the figure
     */
    public Body(final Vector2D pos) {
        this(pos, new Vector2D(0,0), new Vector2D(0,0));
    }

    /**
     * Create a new body. Force will be set to zero.
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     */
    public Body(final Vector2D pos, final Vector2D velocity) {
        this(pos, velocity, new Vector2D(0,0));
    }

    /**
     * Create a new body. The following values will be set by default.
     * - mass as infinite
     * - material as <code>Material.BOUNCYBALL</code>
     * - shape as an aabb with the value of 1 for width and height at position 0;0
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     * @param force the acceleration of the figure
     */
    public Body(final Vector2D pos, final Vector2D velocity, final Vector2D force) {
        this.pos = pos;
        this.velocity = velocity;
        this.force = force;
        massData = new MassData(0, 0);
        material = Material.BOUNCYBALL;
        shape = new AABB(1, 1, new Vector2D(0, 0));
    }
    
    /**
     * Get the shape.
     * @return the shape object.
     */
    public Shape getShape() {
        return shape;
    }
    
    /**
     * Get the width of the shape.
     * @return width of the shape
     */
    public double getShapeWidth() {
        return shape.width;
    }
    
    /**
     * Get the height of the shape.
     * @return height of the shape
     */
    public double getShapeHeight() {
        return shape.height;
    }
    
    /**
     * Get the restitution of the bodys material.
     * @return restitution of the material
     */
    public double getRestitution() {
        return material.getRestitution();
    }
    
    /**
     * Get the raw mass of body.
     * @return raw mass of body
     */
    public double getMass() {
        return massData.getMass();
    }
    
    /**
     * Get the inversed mass value.
     * @return inversed mass value.
     */
    public double getInversedMass() {
        return massData.getInv_mass();
    }
    
    /**
     * Set a velocity vector.
     * @param velocity 2D vector
     */
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }
    
    /**
     * Set a force vector.
     * @param force 2D vector
     */
    public void setForce(final Vector2D force) {
        this.force = force;
    }
    
    /**
     * Set the material value.
     * @param material value
     */
    public void setMaterial(final Material material) {
        this.material = material;
    }
    
    /**
     * Creates a new aabb shape with the given parameters. Save it to the shape field.
     * @param width of the aabb
     * @param height of the aabb
     */
    public void setAABBShape(final int width, final int height) {
        shape = new AABB(width, height, pos);
        computeMass();
    }
    
    /**
     * Creates a new circle shape with the given parameters. Save it to the shape field.
     * @param width of the circle
     * @param height of the circle
     */
    public void setCircleShape(final int width, final int height) {
        shape = new Circle(width, height, pos);
        computeMass();
    }
    
    /**
     * Set the mass infinite. If the parameter is false, the mass will be computed normal.
     * @param isInfinity if the mass is infinite
     */
    public void setInfinityMass(final boolean isInfinity) {
        if(isInfinity) {
            massData.recalcMass(0, 0);
        } else {
            computeMass();
        }
    }
    
    /**
     * Compute the mass based on the density and volume. This method invokes the recalcMass method
     * of the massData field.
     */
    private void computeMass() {
        massData.recalcMass(material.getDensity(), shape.computeArea());
    }
    
    /**
     * Updates the position vector based on the velocity and acceleration.
     * @param frameDuration time value for the current movement
     */
    public void updatePosition(final double frameDuration) {
        velocity.x += (force.x * frameDuration) * Room.PIXELSPERMETER;
        velocity.y += (force.y * frameDuration) * Room.PIXELSPERMETER;
        
        // update the position
        pos.x += (velocity.x * frameDuration) * Room.PIXELSPERMETER;
        pos.y += (velocity.y * frameDuration) * Room.PIXELSPERMETER;
        shape.computeShape();
    }
    
    
    public void setPosition(final Vector2D pos) {
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        shape.computeShape();
    }
}
