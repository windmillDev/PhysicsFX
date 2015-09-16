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
public class Circle extends Shape {
    protected Vector2D center;
    protected double radius;
    
    public Circle(final int width, final int height, final Vector2D upperLeft) {
        super(width, height, upperLeft);
        computeShape();
    }
    
    @Override
    public void computeShape() {
        radius = height / 2;
        Vector2D translate = new Vector2D(radius, radius);
        center = Vector2D.add(upperLeft, translate);
    }

    @Override
    public double computeArea() {
        return (radius * radius * Math.PI);
    }
    
}
