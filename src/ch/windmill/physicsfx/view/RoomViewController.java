/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.view;

import ch.windmill.physicsfx.core.AABB;
import ch.windmill.physicsfx.core.Body;
import ch.windmill.physicsfx.core.Circle;
import ch.windmill.physicsfx.core.Room;
import ch.windmill.physicsfx.core.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Cyrill Jauner
 */
public class RoomViewController {
    private final static int FPS = 60;
    
    @FXML
    private Canvas canvas;
    
    private GraphicsContext gc;
    private Scene scene;
    private Room room;
    
    /**
     * Initilizes the controller class. This method is auto called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        
        room = new Room();  // create new room
        initBodies();       // initialize bodies
        
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLUE);
        
        startAnimationTimer();
        //canvasTest();
    }
    
    private void initBodies() {
        Body aabb = new Body(new Vector2D(300, 100), new Vector2D(0, 0));
        Body aabb2 = new Body(new Vector2D(10, 350), new Vector2D(0, 0));
        Body circle = new Body(new Vector2D(300,0), new Vector2D(0, 5));
        Body circle2 = new Body(new Vector2D(100,100), new Vector2D(0, 5));
        Body circle3 = new Body(new Vector2D(300,100), new Vector2D(-5, 0));
        
        aabb.setAABBShape(100, 50);
        aabb2.setAABBShape(400, 20);
        aabb2.setInfinityMass(true);
        circle.setCircleShape(45, 45);
        circle2.setCircleShape(45, 45);
        circle3.setCircleShape(45, 45);
        
        //room.addBody(aabb);
        room.addBody(circle);
        //room.addBody(circle2);
        //room.addBody(circle3);
        
        for(Body b : room.getBodies()) {
            b.setForce(new Vector2D(0, 0.981));
        }
        room.addBody(aabb2);
        //room.addBody(aabb);
    }
    
    private void canvasTest() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        final long startNanoTime = System.nanoTime();
        
        Rectangle rect = new Rectangle();
        
        new AnimationTimer() {
            double posX = 40;
            double posY = 40;
            int width = 100;
            int height = 50;
            double deltaTime = startNanoTime;
            
            @Override
            public void handle(long now) {
                double t = (now - startNanoTime) / 1000000000.0;
                deltaTime = (now - deltaTime) / 1000000000.0;
                
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.BLUE);
                
                double dX = (0.1 * deltaTime);
                double dY = (0.1 * deltaTime);
                
                posX += (10 * deltaTime);
                posY += (11 * deltaTime);
                gc.strokeRect(posX, posY, width, height);
                gc.fillOval(0, 0, 10, 10);
                System.out.println("x: "+posX+" y: "+posY+" delta x: "+dX+" delta y: "+dY);
                deltaTime = now;
            }
        }.start();
    }
    
    public void startAnimationTimer() {
        final long startNanos = System.nanoTime();
        
        new AnimationTimer() {
            double preferedDelta = (double) 1 / FPS;
            double accumulator = 0;
            double realDelta = startNanos;
            
            @Override
            public void handle(long now) {
                accumulator += (now - realDelta) / 1000000000.0;
                realDelta = now;
                
                while(accumulator >= preferedDelta) {
                    room.computeTimeStep(preferedDelta); // compute the physic calculations
                    accumulator -= preferedDelta;   // decrement the accu
                }
                
                render();
            }
        }.start();
    }
    
    private void render() {
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());   // overwrite with background
        
        for(Body b : room.getBodies()) {
            
            if(b.getShape() instanceof AABB) {
                gc.strokeRect(b.pos.x, b.pos.y, b.getShapeWidth(), b.getShapeHeight());
            } else if(b.getShape() instanceof Circle) {
                gc.strokeOval(b.pos.x, b.pos.y, b.getShapeWidth(), b.getShapeHeight());
            }
        }
    }
}
