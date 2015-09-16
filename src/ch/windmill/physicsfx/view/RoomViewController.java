/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.view;

import ch.windmill.physicsfx.MainApp;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Cyrill Jauner
 */
public class RoomViewController {
    public final static int FPS = 60;
    
    @FXML
    private Canvas canvas;
    
    private GraphicsContext gc;
    private Scene scene;
    private Room room;
    
    private MainApp mainApp;
    
    /**
     * Initializes the controller class. This method is auto called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        room = new Room();  // create new room
        setOnCanvasClick();
        
        // set ground
        Body ground = new Body(new Vector2D(0, 300));
        ground.setAABBShape(600, 20);
        ground.setInfinityMass(true);
        room.addBody(ground);
        //aLotOfBodiesTest();
        
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLUE);
       
        startAnimationTimer();
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setFillColor(Color c) {
        gc.setFill(c);
    }

    public void setStrokeColor(Color c) {
        gc.setStroke(c);
    }
    
    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    /**private void initBodies() {
        Body aabb = new Body(new Vector2D(300, 100), new Vector2D(0, 0));
        Body aabb2 = new Body(new Vector2D(10, 350), new Vector2D(0, 0));
        Body circle = new Body(new Vector2D(300,0), new Vector2D(0, 5));
        Body circle2 = new Body(new Vector2D(100,100), new Vector2D(0, 5));
        Body circle3 = new Body(new Vector2D(400,100), new Vector2D(-5, 0));
        
        aabb.setAABBShape(100, 50);
        aabb2.setAABBShape(450, 20);
        aabb2.setInfinityMass(true);
        circle.setCircleShape(45, 45);
        circle2.setCircleShape(45, 45);
        circle3.setCircleShape(45, 45);
        
        room.addBody(aabb);
        room.addBody(circle);
        room.addBody(circle2);
        room.addBody(circle3);
        
        for(Body b : room.getBodies()) {
            b.setForce(new Vector2D(0, 0.981));
        }
        room.addBody(aabb2);
    }*/
    
    /**private void aLotOfBodiesTest() {
        int x = 0;
        int y = 0;
        int count = 20;
        for(int i = 0; i < count; i++) {
            Body b = new Body(new Vector2D(x, y));
            b.setAABBShape(40, 40);
            b.setForce(new Vector2D(0, 0.981));
            
            if(x < 500) {
                x += 50;
            } else {
                x = 0;
                y += 60;
            }
            room.addBody(b);
        }
        
    }*/
    
    /**
     * Start the animation timer loop. The animation timer will calculate the time delta between the last execution and now.
     * This delta will be used as parameter for the computeTimeStep method.
     */
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
    
    /**
     * Render the canvas. It overwrites the old drawing with an filled rectangle first. After that, it
     * draws each body of the room object with his shape to the canvas.
     */
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
    
    /**
     * Set a mouse click listener for the canvas object. Every click on the canvas will add an new body to the room.
     * The click position is the start position for the shape.
     */
    private void setOnCanvasClick() {
        canvas.setOnMouseClicked((MouseEvent event) -> {
            ControlViewController control = mainApp.getControlViewController();
            Body b = new Body(new Vector2D(event.getX(), event.getY()));
            
            if(control.getShapeType() == ShapeType.AABB) {
                b.setAABBShape(40, 40);
            } else {
                b.setCircleShape(40, 40);
            }
            
            b.setForce(new Vector2D(0, 0.981));
            b.setMaterial(control.getShapeMaterial());
            
            room.addBody(b);
        });
    }
}
