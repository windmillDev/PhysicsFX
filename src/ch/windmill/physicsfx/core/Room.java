/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.core;

import java.util.ArrayList;

/**
 * This class represents a room in the engine. A room contains bodies. It can compute a time step.
 * That method will invoke all physics calculations like positionupdate, collisiondetection, 
 * impulse resolution etc. for all bodies in the room.
 * 
 * All distances should be given in meters. The distances will be multiplied with a factor of 10.
 * That means that 10 pixels are 1 meter.
 * 
 * @author Cyrill Jauner
 */
public class Room {
    public final static int PIXELSPERMETER = 10;    // factor to multiply with all pixel values
    private final ArrayList<Body> bodies;
    private final ArrayList<Collision> pairs;
    
    /**
     * Create a new room object.
     */
    public Room() {
        bodies = new ArrayList<>();
        pairs = new ArrayList<>();
    }
    
    /**
     * Get the list of bodies.
     * @return list of bodies
     */
    public ArrayList<Body> getBodies() {
        return bodies;
    }
    
    /**
     * Add the given body object to the bodies list.
     * @param b object to add to the room
     */
    public void addBody(final Body b) {
        bodies.add(b);
    }
    
    /**
     * Create a new aabb object and add it to the bodies list.
     * @param pos position vector
     * @param width of the aabb
     * @param height of the aabb
     */
    public void addAABB(final Vector2D pos, final int width, final int height) {
        Body b = new Body(pos);
        b.setAABBShape(width, height);
        bodies.add(b);
    }
    
    /**
     * Create a new circle object and add it to the bodies list.
     * @param pos position vector
     * @param width of the circle
     * @param height of the circle
     */
    public void addCircle(final Vector2D pos, final int width, final int height) {
        Body b = new Body(pos);
        b.setCircleShape(width, height);
        bodies.add(b);
    }
    
    /**
     * Compute the physics for the given delta time. This method invokes all necessary phyiscs methods.
     * @param deltaTime to calculate the translation
     */
    public void computeTimeStep(final double deltaTime) {
        updatePhysics(deltaTime);
        
        generateCollidedPairs();
        
        resolveCollisions();
        
        correctPositions();
    }
    
    /**
     * Updates the physical state of the bodies.
     * @param deltaTimeNanos time in nanoseconds
     */
    private void updatePhysics(final double deltaTimeSeconds) {
        for(Body b : bodies) {
            b.updatePosition(deltaTimeSeconds);
        }
    }
    
    /**
     * Generate pairs of collided bodies. It add all collided pairs to the collision list.
     */
    private void generateCollidedPairs() {
        Body b1, b2;
        pairs.clear();
        
        for(int i = 0; i < bodies.size(); i++) {    // iterate through all possible pairs
            b1 = bodies.get(i);
            
            for (int j = (i + 1); j < bodies.size(); j++) {
                if(i != j) {
                    b2 = bodies.get(j);
                    Collision c = new Collision(b1, b2);
                    
                    if(c.isCollision()) {   // check if the current bodies are collided
                        pairs.add(c);
                    }
                }
            }
        }
    }
    
    /**
     * Invoke the impulse resolution for all collided pairs.
     */
    private void resolveCollisions() {
        for(Collision c : pairs) {
            c.applyImpulse();
        }
    }
    
    /**
     * Correct the positions of the collided pairs. This method should be invoked after the
     * revolveCollision method.
     */
    private void correctPositions() {
        for(Collision c : pairs) {
            c.positionalCorrection();
        }
    }
}
