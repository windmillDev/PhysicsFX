/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.core;

import java.util.ArrayList;

/**
 *
 * @author Cyrill Jauner
 */
public class Room {
    public final static int PIXELSPERMETER = 10;
    private ArrayList<Body> bodies;
    private ArrayList<Collision> pairs;
    
    public Room() {
        bodies = new ArrayList<>();
        pairs = new ArrayList<>();
    }
    
    public ArrayList<Body> getBodies() {
        return bodies;
    }
    
    public void addBody(final Body b) {
        bodies.add(b);
    }
    
    public void addAABB(final Vector2D pos, final int width, final int height) {
        Body b = new Body(pos);
        b.setAABBShape(width, height);
        bodies.add(b);
    }
    
    public void addCircle(final Vector2D pos, final int width, final int height) {
        Body b = new Body(pos);
        b.setCircleShape(width, height);
        bodies.add(b);
    }
    
    public void computeTimeStep(final double deltaTime) {
        updatePhysics(deltaTime);
        
        generateCollidedPairs();
        //System.out.println("pairs: "+pairs.size());
        resolveCollisions();
        
        correctPositions();
    }
    
    /**
     * Update the physical attributes of the bodies.
     * @param deltaTimeNanos delta time since the last physics update.
     */
    private void updatePhysics(final double deltaTimeSeconds) {
        for(Body b : bodies) {
            b.updatePosition(deltaTimeSeconds);
        }
    }
    
    /**
     * Generate pairs of collided bodies.
     */
    private void generateCollidedPairs() {
        Body b1, b2;
        pairs.clear();
        
        for(int i = 0; i < bodies.size(); i++) {
            b1 = bodies.get(i);
            
            for (int j = (i + 1); j < bodies.size(); j++) {
                if(i != j) {
                    b2 = bodies.get(j);
                    Collision c = new Collision(b1, b2);
                    
                    if(c.isCollision()) {
                        pairs.add(c);
                    }
                }
            }
        }
    }
    
    private void resolveCollisions() {
        for(Collision c : pairs) {
            c.applyImpulse();
        }
    }
    
    private void correctPositions() {
        for(Collision c : pairs) {
            c.positionalCorrection();
        }
    }
}
