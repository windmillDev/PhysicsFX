/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.core;

/**
 *
 * @author Cyrill Jauner
 */
public class AABB extends Shape {
    protected Vector2D min;
    protected Vector2D max;
    
    public AABB(final int width, final int height, final Vector2D upperLeft) {
        super(width, height, upperLeft);
        min = upperLeft;
        computeShape();
    }
    
    @Override
    public void computeShape() {
        min = upperLeft;
        max = new Vector2D(min.x + width, min.y + height);
    }

    @Override
    public double computeArea() {
        return (width * height);
    }
    
    public Vector2D getCenter() {
        return (new Vector2D(min.x +(width / 2), min.y +(height / 2)));
    }
}
