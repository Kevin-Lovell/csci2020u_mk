package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    Group root = new Group();
    Scene scene = new Scene(root, 1280, 720, Color.WHITE);
    GridPane gridPane = new GridPane();
    GridPane number = new GridPane();

    Stage stage = new Stage();
    Group layout = new Group();
    Scene scene2 = new Scene(layout, 1280, 720);
    private ConnectionThread connectionThread = null;

    ListView<String> list = new ListView<String>();
    TextArea emailWindow = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        OpenHomepage(primaryStage);
    }

    public void OpenHomepage(Stage primaryStage){
        // gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(400, 0, 0, 450));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Rectangle border = new Rectangle();
        border.setFill(Color.LIGHTGRAY);
        border.setX(350);
        border.setY(120);
        border.setWidth(600);
        border.setHeight(450);
        border.setArcWidth(20);
        border.setArcHeight(20);

        Image logo = new Image("coldMail2.png");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(500);
        imageView.setFitHeight(250);
        imageView.setX(400);
        imageView.setY(150);

        imageView.setImage(logo);

        Label userLabel = new Label("Username");
        gridPane.add(userLabel, 1,1 );
        TextField userField = new TextField();
        userField.setText("csci2020utest@gmail.com");
        userField.setPrefWidth(300);
        userField.setPromptText("youremail@domain.com");
        GridPane.setHalignment(userField, HPos.LEFT);
        gridPane.add(userField, 2, 1);


        Label passLabel = new Label("Password");
        gridPane.add(passLabel, 1, 2);
        TextField passField = new PasswordField();
        passField.setPromptText("");
        passField.setText("thisclassisgood");
        GridPane.setHalignment(passField, HPos.LEFT);
        gridPane.add(passField, 2, 2);

        Button Login = new Button("Login");
        Login.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                primaryStage.close();
                final String username = userField.getText();
                final String password = passField.getText();
                email(stage, "csci2020utest2@gmail.com", "thisclassisgood");

            }
        });
        gridPane.add(Login, 2, 3);


        root.getChildren().add(border);
        root.getChildren().add(gridPane);
        root.getChildren().add(imageView);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void email(Stage stage, String username, String password){
        connectionThread = new ConnectionThread(username,password);
        Thread cT = new Thread(connectionThread);
        cT.start();

        stage.setScene(scene2);
        stage.show();

        ListView<String> list = new ListView<String>();

        list.setItems(connectionThread.getAllMail());
        list.setPrefWidth(600);
        list.setPrefHeight(500);
        list.setTranslateX(35);
        list.setTranslateY(150);

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = list.getSelectionModel().getSelectedIndex();
                System.out.println("selected " + index);

                emailWindow.setText(messageContent.get(index));
            }
        });

        Image bottomBanner = new Image("coldMail3.png");
        ImageView bannerImage = new ImageView();
        bannerImage.setY(345);
        bannerImage.setImage(bottomBanner);

//        Image envelope = new Image("coldMail1.png");
//        ImageView envelopeImage = new ImageView();
//        envelopeImage.setFitWidth(600);
//        envelopeImage.setFitHeight(300);
//        envelopeImage.setX(285);
//        envelopeImage.setY(-50);
//        envelopeImage.setImage(envelope);
//
        GridPane composeSection = new GridPane();

        // composeSection.setGridLinesVisible(true);
        composeSection.setPadding(new Insets(50, 0, 0, 700));
        composeSection.setVgap(10);
        composeSection.setHgap(10);

        Rectangle composeBorder = new Rectangle();
        composeBorder.setFill(Color.LIGHTGRAY);
        composeBorder.setX(685);
        composeBorder.setY(35);
        composeBorder.setWidth(575);
        composeBorder.setHeight(615);
        composeBorder.setArcWidth(10);
        composeBorder.setArcHeight(10);

        Label toLabel = new Label("To:");
        composeSection.add(toLabel, 1,1 );

        TextField toField = new TextField();
        toField.setPrefWidth(400);
        composeSection.add(toField, 2, 1);

        Label subjectLabel = new Label("Subject:");
        composeSection.add(subjectLabel, 1,2 );

        TextField subjectField = new TextField();
        subjectField.setPrefWidth(400);
        composeSection.add(subjectField, 2, 2);


        Label messageLabel = new Label("Message:");
        composeSection.add(messageLabel, 1,3 );
        composeSection.setValignment(messageLabel, VPos.TOP);

        TextArea messageField = new TextArea();
        messageField.setPrefRowCount(27);
        messageField.setPrefColumnCount(33);
        messageField.setWrapText(true);
        composeSection.add(messageField, 2, 3);

        Button delete = new Button("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


            }
        });
        delete.setTranslateX(30);
        delete.setTranslateY(40);
        delete.setPrefWidth(150);
        delete.setPrefHeight(50);

        Button view = new Button("View");
        view.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


            }
        });
        view.setTranslateX(185);
        view.setTranslateY(40);
        view.setPrefWidth(150);
        view.setPrefHeight(50);

        Button reply = new Button("Reply");
        reply.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {


            }
        });

        reply.setVisible(false);
        reply.setTranslateX(340);
        reply.setTranslateY(40);
        reply.setPrefWidth(150);
        reply.setPrefHeight(50);

        Button send = new Button("Send");
//        send.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent ee) {
//                try {
//                    Message message = new MimeMessage(session);
//                    message.setFrom(new InternetAddress(username));
//                    message.setRecipients(Message.RecipientType.TO,
//                            InternetAddress.parse(toField.getText()));
//                    message.setSubject(subjectField.getText());
//                    message.setText(messageField.getText());
//
//                    Transport.send(message);
//
//                    System.out.println("Message Sent");
//
//                } catch (MessagingException e) {
//                    throw new RuntimeException(e);
//                }
//
//                toField.clear();
//                subjectField.clear();
//                messageField.clear();
//
//            }
//        });
        send.setTranslateX(940);
        send.setTranslateY(580);
        send.setPrefWidth(150);
        send.setPrefHeight(50);

       // layout.getChildren().add(envelopeImage);
        layout.getChildren().add(bannerImage);
        layout.getChildren().add(list);
        //layout.getChildren().add(composeBorder);
        //layout.getChildren().add(composeSection);
        layout.getChildren().add(delete);
        layout.getChildren().add(view);
        layout.getChildren().add(reply);
        //layout.getChildren().add(send);
        layout.getChildren().add(number);
        layout.getChildren().add(emailWindow);

        stage.setScene(scene2);
        stage.show();
    }


}








