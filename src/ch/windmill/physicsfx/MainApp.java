package ch.windmill.physicsfx;

import ch.windmill.physicsfx.view.RoomViewController;
import ch.windmill.physicsfx.view.RootLayoutController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*
 * http://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 */

/**
 *
 * @author Cyrill Jauner
 */
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Physics FX");
        initRootLayout();
        showRoomView();
    }
    
    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
        try {
            rootLayout = (BorderPane) loader.load();
            
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            RootLayoutController controller = loader.getController();
            
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void showRoomView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/RoomView.fxml"));
        try {
            AnchorPane roomView = (AnchorPane) loader.load();
            rootLayout.setCenter(roomView);
            
            RoomViewController controller = loader.getController();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
