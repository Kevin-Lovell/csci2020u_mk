package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }



    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720, Color.WHITE);
        HBox hb = new HBox();

        GridPane gridPane = new GridPane();
       // gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(400, 0, 0, 515));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Rectangle boarder = new Rectangle();
        boarder.setFill(Color.LIGHTGRAY);
        boarder.setX(350);
        boarder.setY(120);
        boarder.setWidth(600);
        boarder.setHeight(450);
        boarder.setArcWidth(20);
        boarder.setArcHeight(20);

        Image logo = new Image("coldMail2.png");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(500);
        imageView.setFitHeight(250);
        imageView.setX(400);
        imageView.setY(100);

        imageView.setImage(logo);

        Label userLabel = new Label("Username");
        gridPane.add(userLabel, 1,1 );
        TextField userField = new TextField();
        userField.setPromptText("youremail@domain.com");
        GridPane.setHalignment(userField, HPos.LEFT);
        gridPane.add(userField, 2, 1);


        Label passLabel = new Label("Password");
        gridPane.add(passLabel, 1, 2);
        TextField passField = new PasswordField();
        passField.setPromptText("");
        GridPane.setHalignment(passField, HPos.LEFT);
        gridPane.add(passField, 2, 2);


        Button Login = new Button("Login");
        Login.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


            }
        });
        gridPane.add(Login, 2, 3);

        BorderPane layout = new BorderPane();
        layout.setTop(gridPane);

        root.getChildren().add(boarder);
        root.getChildren().add(gridPane);
        root.getChildren().add(imageView);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}






