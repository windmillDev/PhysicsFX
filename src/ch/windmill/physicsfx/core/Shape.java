/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.core;

/**
 * Represents a shape to draw to a canvas element.
 * @author Cyrill Jauner
 */
public abstract class Shape {
    
    protected double width;
    protected double height;
    protected Vector2D upperLeft;
    
    public Shape(final int width, final int height, final Vector2D upperLeft) {
        this.width = width;
        this.height = height;
        this.upperLeft = upperLeft;
    }
    
    public abstract void computeShape();
    
    public abstract double computeArea();

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
