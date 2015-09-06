package ch.windmill.physicsfx.core;

/**
 * Represents a basic body. A body has vectors for position, velocity and acceleration.
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
     * Create a new body. force will be set to zero.
     * @param pos the position of the figure
     * @param velocity the velocity of the figure
     */
    public Body(final Vector2D pos, final Vector2D velocity) {
        this(pos, velocity, new Vector2D(0,0));
    }

    /**
     * Create a new body.
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
    }

    public Shape getShape() {
        return shape;
    }
    
    public double getShapeWidth() {
        return shape.width;
    }
    
    public double getShapeHeight() {
        return shape.height;
    }
    
    public double getRestitution() {
        return material.getRestitution();
    }
    
    public double getMass() {
        return massData.getMass();
    }
    
    public double getInversedMass() {
        return massData.getInv_mass();
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void setAABBShape(final int width, final int height) {
        shape = new AABB(width, height, pos);
        computeMass();
    }
    
    public void setCircleShape(final int width, final int height) {
        shape = new Circle(width, height, pos);
        computeMass();
    }
    
    private void computeMass() {
        massData = new MassData(1, shape.computeArea());
    }
    
    /**
     * Updates the position vector based on the velocity and acceleration.
     * @param frameDuration time value for the current movement
     */
    public void updatePosition(final double frameDuration) {
        // update the position
        pos.x += ((velocity.x * frameDuration)) * Room.PIXELSPERMETER;
        pos.y += ((velocity.y * frameDuration)) * Room.PIXELSPERMETER;
        shape.computeShape();
    }
}
