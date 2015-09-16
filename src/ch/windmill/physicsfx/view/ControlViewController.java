/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.windmill.physicsfx.view;

import ch.windmill.physicsfx.MainApp;
import ch.windmill.physicsfx.core.Material;
import ch.windmill.physicsfx.core.Room;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Cyrill Jauner
 */
public class ControlViewController {
    
    @FXML
    private RadioButton rbAABB;
    
    @FXML
    private RadioButton rbCircle;
    
    @FXML
    private Text countBodies;
    
    @FXML
    private Text fps;
    
    @FXML
    private ChoiceBox<Material> materialChoice;
    
    @FXML
    private ToggleButton gravityOn;
    
    @FXML
    private ToggleButton gravityOff;
    
    private MainApp mainApp;
    
    /**
     * Initializes the controller class. This method is auto called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // initialize choice box
        materialChoice.setItems(FXCollections.observableArrayList(
                Material.BOUNCYBALL, Material.ROCK
        ));
        materialChoice.setValue(Material.BOUNCYBALL);
    }
    
    private void initGravityGroup() {
        ToggleGroup group = gravityOn.getToggleGroup();
        Room room = (mainApp.getRoomViewController()).getRoom();
        
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(gravityOff.isSelected()) {
                    room.setGravity(false);
                } else if(gravityOn.isSelected()) {
                    room.setGravity(true);
                } else {
                    gravityOn.setSelected(true);
                }
            }
        });
    }
    
    public void setMainApp(final MainApp mainApp) {
        this.mainApp = mainApp;
        createInfoThread();
        
        initGravityGroup();
    }
    
    /**
     * Create a thread to get informations about the engine. This thread will write into the information tab.
     * It renew the infos every 1000 milliseconds.
     */
    private void createInfoThread() {
        new Thread(() -> {
            while(true) {
                RoomViewController control = mainApp.getRoomViewController();
                setCountBodiesText(""+(control.getRoom()).getBodies().size());
                setFPSText(""+RoomViewController.FPS);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    public void setCountBodiesText(final String text) {
        countBodies.setText(text);
    }
    
    public void setFPSText(final String text) {
        fps.setText(text);
    }
    
    public ShapeType getShapeType() {
        ShapeType type = ShapeType.AABB;
        if(rbCircle.isSelected()) {
            type = ShapeType.CIRCLE;
        }
        return type;
    }
    
    public Material getShapeMaterial() {
        return materialChoice.getValue();
    }
}
