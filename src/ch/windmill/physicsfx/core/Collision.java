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
public class Collision {
    private Body b1, b2;
    private double penetration;
    private Vector2D normal;
    
    public Collision(final Body b1, final Body b2) {
        this.b1 = b1;
        this.b2 = b2;
        penetration = 0;
        normal = new Vector2D();
    }
    
    public void setPenetration(final double penetration) {
        this.penetration = penetration;
    }
    
    public void setNormal(final Vector2D normal) {
        this.normal = normal;
    }
    
    /**
     * Check if the two bodies are collided. It checks if the shapes of the bodies
     * are overlapped. The method detects the correct shape subclass and casts 
     * automatically.
     * @return if the bodies are collided.
     */
    public boolean isCollision() {
        Shape s1 = b1.getShape();
        Shape s2 = b2.getShape();
        
        if(s1 instanceof AABB) {
            if(s2 instanceof AABB) {
                return AABBvsAABB((AABB) s1, (AABB) s2);
            } else if(s2 instanceof Circle) {
                return AABBvsCircle((AABB) s1, (Circle) s2);
            }
        } else if(s1 instanceof Circle) {
            if(s2 instanceof Circle) {
                return CirclevsCircle((Circle) s1, (Circle) s2);
            } else if(s2 instanceof AABB) {
                return AABBvsCircle((AABB) s2, (Circle) s1);
            }
        }
        
        return false;
    }
    
    private boolean AABBvsAABB(final AABB a1, final AABB a2) {
        boolean res = false;
        Vector2D n = Vector2D.sub(a1.getCenter(), a2.getCenter());  // calculate the vector from the middle position of r1 to the middle position of r2.
        
        // calculate half extents along x axis for each body
        double a1_extent = (a1.max.x - a1.min.x) / 2;
        double a2_extent = (a2.max.x - a2.min.x) / 2;
        
        double x_overlap = a1_extent + a2_extent - Math.abs(n.x);   // calculate overlap on x axis
        
        if(x_overlap > 0) { // SAT test x axis
            // calculate half extents along y axis for each body
            a1_extent = (a1.max.y - a1.min.y) / 2;
            a2_extent = (a2.max.y - a2.min.y) / 2;
            
            double y_overlap = a1_extent + a2_extent - Math.abs(n.y);   // calculate overlap on y axis
            
            if(y_overlap > 0) { // SAT test y axis
                // find out the axis with least penetration
                // set normal vector and penetration scalar
                if(x_overlap < y_overlap) {
                    if(n.x < 0) {
                        normal = new Vector2D(-1, 0);
                    } else {
                        normal = new Vector2D(1, 0);
                    }
                    penetration = x_overlap;
                } else {
                    if(n.y < 0) {
                        normal = new Vector2D(0, -1);
                    } else {
                        normal = new Vector2D(0, 1);
                    }
                    penetration = y_overlap;
                }
                res = true;
            }
        }
        
        return res;
    }
    
    private boolean CirclevsCircle(final Circle c1, final Circle c2) {
        boolean res = false;
        double r = c1.radius + c2.radius;
        Vector2D n = Vector2D.sub(c2.center, c1.center);    // calculate the vector from the middle position of c1 to the middle position of c2.
        
        if(n.lengthSquared() <= (r * r)) {
            penetration = r - n.length();
            normal = n;
            normal.normalize();
            res = true;
        }
        
        return res;
    }
    
    private boolean AABBvsCircle(final AABB a, final Circle c) {
        boolean res = false;
        boolean inside = false;
        Vector2D difference = Vector2D.sub(c.center, a.getCenter());
        Vector2D closest = new Vector2D();
        
        // calculate half extents for each axis
        double x_extent = (a.max.x - a.min.x) / 2;
        double y_extent = (a.max.y - a.min.y) / 2;
        
        // clamp point to edges of the aabb
        closest.x = clamp(difference.x, -x_extent, x_extent);
        closest.y = clamp(difference.y, -y_extent, y_extent);
        
        // circle center is inside the aabb, so we need to clamp the circle's center
        // to the closest edge
        if(Vector2D.equals(difference, closest)) {
            inside = true;
            
            if(Math.abs(difference.x) > Math.abs(difference.y)) {   // find closest axis
                if(closest.x > 0) {
                    closest.x = x_extent;
                } else {
                    closest.x = -x_extent;
                }
            } else {
                if(closest.y > 0) {
                    closest.y = y_extent;
                } else {
                    closest.y = -y_extent;
                }
            }
        }
        
        normal = Vector2D.sub(difference, closest); // calculate distance from circle center to the closest point
        double d = normal.lengthSquared();

        if (d <= (c.radius * c.radius)) {
            res = true;
            d = Math.sqrt(d);   // get the length
            penetration = c.radius - d;
            normal.normalize();

            if (inside) {
                normal = Vector2D.multiply(normal, -1);
            }
        }

        return res;
    }
    
    /**
     * Limiting a position to an area.
     * @param d value to check
     * @param min low bound
     * @param max high bound
     * @return 
     */
    private double clamp(final double d, final double min, final double max) {
        return Math.max(Math.min(d, max), min);
    }
    
    public void applyImpulse() {
        Vector2D relVelocity = Vector2D.sub(b2.velocity, b1.velocity);  // calculate the relative velocity
        double scalarAlongNormal = Vector2D.dot(relVelocity, normal);   // calculate relative velocity in terms of the normal direction
        //System.out.println("normal: "+normal.x+" ; "+normal.y);
        if(scalarAlongNormal > 0) {
            return;
        }
        
        double e = Math.min(b1.getRestitution(), b2.getRestitution());  // get the lower restitution
        double j = -(1 + e) * scalarAlongNormal;
        System.out.println("j before: "+j);
        j /= 1 / b1.getMass() + 1 / b2.getMass();
        System.out.println("j: "+j);
        
        // apply impulse
        Vector2D impulse = Vector2D.multiply(normal, j);
        b1.velocity = Vector2D.sub(b1.velocity, Vector2D.multiply(impulse, b1.getInversedMass()));
        b2.velocity = Vector2D.add(b2.velocity, Vector2D.multiply(impulse, b2.getInversedMass()));
    
        System.out.println("vel b1: "+ b1.velocity.y);
        System.out.println("vel b2: "+ b2.velocity.y);
    }
}
