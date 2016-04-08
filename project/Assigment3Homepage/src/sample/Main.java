package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
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

    Stage emailStage = new Stage();
    Group emailLayout = new Group();
    Scene scene3 = new  Scene(emailLayout, 1280, 720);

    private ConnectionThread connectionThread = null;
    private ObservableList<email> emails = FXCollections.observableArrayList();
    ListView<String> list = new ListView<String>();
    TextArea emailWindow = new TextArea();

    private BorderPane borderPane;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ColdMail - Homepage");
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
        userField.setPrefWidth(300);
        userField.setPromptText("youremail@domain.com");
        GridPane.setHalignment(userField, HPos.LEFT);
        gridPane.add(userField, 2, 1);


        Label passLabel = new Label("Password");
        gridPane.add(passLabel, 1, 2);
        TextField passField = new PasswordField();
        passField.setPromptText("");
        GridPane.setHalignment(passField, HPos.LEFT);
        gridPane.add(passField, 2, 2);
        userField.setText("csci2020utest2@gmail.com");
        passField.setText("thisclassisgood");
        Button Login = new Button("Login");
        Login.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                primaryStage.close();
                final String username = userField.getText();
                final String password = passField.getText();
                //final String username = "csci2020utest@gmail.com";
                //final String password = "thisclassisgood";
                email(username, password);

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

    public void email(String username, String password){

        connectionThread = new ConnectionThread(username,password);
        Thread cT = new Thread(connectionThread);
        cT.start();

        stage.setTitle("Mail "+username+" ColdMail");

        stage.setScene(scene2);
        stage.setResizable(false);
        stage.show();

        TextArea emailWindow = new TextArea();
        emailWindow.setPrefRowCount(39);
        emailWindow.setPrefColumnCount(39);
        emailWindow.setWrapText(true);
        emailWindow.setTranslateX(700);
        emailWindow.setTranslateY(45);

        Image bottomBanner = new Image("coldMail3.png");
        ImageView bannerImage = new ImageView();
        bannerImage.setY(345);
        bannerImage.setImage(bottomBanner);

        ListView<String> list = new ListView<String>();
        list.setItems(connectionThread.getAllMail());
        list.setPrefWidth(550);
        list.setPrefHeight(615);
        list.setTranslateX(80);
        list.setTranslateY(45);

//        Label messageNum = new Label("Inbox (" +" messages)");
//        number.add(messageNum, 0, 0);

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = list.getSelectionModel().getSelectedIndex();
                //System.out.println("selected " + index);

                Thread t1 = new Thread(new Runnable() {
                    public void run() {
                        emailWindow.setText(connectionThread.mailBody(index));
                    }
                });
                t1.start();
            }
        });

        Rectangle listBorder = new Rectangle();
        listBorder.setFill(Color.LIGHTGRAY);
        listBorder.setX(85);
        listBorder.setY(40);
        listBorder.setWidth(550);
        listBorder.setHeight(615);
        listBorder.setArcWidth(10);
        listBorder.setArcHeight(10);



        Rectangle emailBorder = new Rectangle();
        emailBorder.setFill(Color.LIGHTGRAY);
        emailBorder.setX(705);
        emailBorder.setY(40);
        emailBorder.setWidth(535);
        emailBorder.setHeight(615);
        emailBorder.setArcWidth(10);
        emailBorder.setArcHeight(10);


        Label newLabel = new Label("Compose");
        Label replyLabel = new Label("Reply");
        Label deleteLabel = new Label("Delete");
        Label exitLabel = new Label("Exit");

        newLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                emailPage(username, password);
                stage.close();
            }
        });

        replyLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {

            }
        });

        deleteLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {

            }
        });


        exitLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                System.exit(0);
            }
        });


        Menu newMenu = new Menu();
        newMenu.setGraphic(newLabel);
        Menu replyMenu = new Menu();
        replyMenu.setGraphic(replyLabel);
        Menu deleteMenu = new Menu();
        deleteMenu.setGraphic(deleteLabel);
        Menu exitMenu = new Menu();
        exitMenu.setGraphic(exitLabel);


        //layout.getChildren().add(envelopeImage);
        layout.getChildren().add(bannerImage);
        layout.getChildren().add(listBorder);
        layout.getChildren().add(list);
        layout.getChildren().add(emailBorder);
        layout.getChildren().add(emailWindow);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(newMenu);
        menuBar.getMenus().add(replyMenu);
        menuBar.getMenus().add(deleteMenu);
        menuBar.getMenus().add(exitMenu);
        // borderPane.setTop(menuBar);
        layout.getChildren().add(menuBar);

    }

    public void emailPage(String username, String password){
        emailStage.setTitle("ColdMail - Compose Email");
        emailStage.setScene(scene3);
        emailStage.setResizable(false);
        emailStage.show();

        Label fontLabel = new Label("Font");
        Label fontSizeLabel = new Label("Font Size");
        Label boldLabel = new Label("B");
        boldLabel.setStyle(boldLabel.getStyle() + "-fx-font-weight: bold;");
        Label italicsLabel = new Label("I");
        italicsLabel.setStyle(italicsLabel.getStyle() + "-fx-font-style: italic;");
        Label normalLabel = new Label("N");
        Label alignLeftLabel = new Label("Align Left");
        Label alignRightLabel= new Label("Align Right");

        Image bottomBanner = new Image("coldMail3.png");
        ImageView bannerImage = new ImageView();
        bannerImage.setY(345);
        bannerImage.setImage(bottomBanner);



        Rectangle emailBorder = new Rectangle();
        emailBorder.setFill(Color.LIGHTGRAY);
        emailBorder.setX(30);
        emailBorder.setY(80);
        emailBorder.setWidth(1230);
        emailBorder.setHeight(620);
        emailBorder.setArcWidth(10);
        emailBorder.setArcHeight(10);

        GridPane composeSection = new GridPane();
        composeSection.setGridLinesVisible(true);
        composeSection.setPadding(new Insets(100, 0, 0, 50));
        composeSection.setVgap(10);
        composeSection.setHgap(10);


        Label toLabel = new Label("To:");
        toLabel.setTranslateX(80);
        toLabel.setTranslateY(112);


        TextField toField = new TextField();
        toField.setPrefWidth(1050);
        toField.setTranslateX(150);
        toField.setTranslateY(110);


        Label subjectLabel = new Label("Subject:");
        subjectLabel.setTranslateX(80);
        subjectLabel.setTranslateY(152);


        TextField subjectField = new TextField();
        subjectField.setPrefWidth(1050);
        subjectField.setTranslateX(150);
        subjectField.setTranslateY(150);


        Label messageLabel = new Label("Message:");
        messageLabel.setTranslateX(80);
        messageLabel.setTranslateY(192);

        TextArea messageField = new TextArea();
        messageField.setPrefWidth(1050);
        messageField.setPrefHeight(400);
        messageField.setWrapText(true);
        messageField.setTranslateX(150);
        messageField.setTranslateY(190);
        //composeSection.add(messageField, 2, 3);

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                emailStage.close();
                email(username, password);
            }
        });
        back.setTranslateX(1205);
        back.setTranslateY(0);
        back.setPrefWidth(75);
        back.setPrefHeight(25);

        Menu fontMenu = new Menu();
        fontMenu.setGraphic(fontLabel);


        Menu fontSizeMenu = new Menu();
        fontSizeMenu.setGraphic(fontSizeLabel);

        MenuItem twelve = new MenuItem("12");
        twelve.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                messageField.setStyle("-fx-font-size: 12px ;");
            }
        });

        MenuItem thirteen = new MenuItem("13");
        thirteen.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                messageField.setStyle("-fx-font-size: 13px ;");
            }
        });

        MenuItem fourteen = new MenuItem("14");
        fourteen.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                messageField.setStyle("-fx-font-size: 14px;");
            }
        });

        fontSizeMenu.getItems().add(twelve);
        fontSizeMenu.getItems().add(thirteen);
        fontSizeMenu.getItems().add(fourteen);


        Menu boldMenu = new Menu();
        boldMenu.setGraphic(boldLabel);

        Menu italicsMenu = new Menu();
        italicsMenu.setGraphic(italicsLabel);

        Menu normalMenu = new Menu();
        normalMenu.setGraphic(normalLabel);

        Menu alignLeftMenu = new Menu();
        alignLeftMenu.setGraphic(alignLeftLabel);

        Menu alignRightMenu = new Menu();
        alignRightMenu.setGraphic(alignRightLabel);



        boldLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                messageField.setStyle(messageField.getStyle() + "-fx-font-weight: bold;");
            }
        });

        italicsLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                messageField.setStyle(messageField.getStyle() + "-fx-font-style: italic;");
            }
        });

        normalLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                messageField.setStyle(messageField.getStyle() + "-fx-font-weight: normal;");
                messageField.setStyle(messageField.getStyle() + "-fx-font-style: normal;");
            }
        });


        alignLeftLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                messageField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }
        });

        alignRightLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                messageField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            }
        });

        Button send = new Button("Send");
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent ee) {
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

            }

        });
        send.setTranslateX(500);
        send.setTranslateY(630);
        send.setPrefWidth(150);
        send.setPrefHeight(50);

        Button discard = new Button("Discard");
        discard.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent ee) {
                toField.clear();
                subjectField.clear();
                messageField.clear();
            }

        });

        discard.setTranslateX(700);
        discard.setTranslateY(630);
        discard.setPrefWidth(150);
        discard.setPrefHeight(50);



        MenuBar OptionsMenu = new MenuBar();
        OptionsMenu.getMenus().add(fontMenu);
        OptionsMenu.getMenus().add(fontSizeMenu);
        OptionsMenu.getMenus().add(boldMenu);
        OptionsMenu.getMenus().add(italicsMenu);
        OptionsMenu.getMenus().add(normalMenu);
        OptionsMenu.getMenus().add(alignLeftMenu);
        OptionsMenu.getMenus().add(alignRightMenu);


        emailLayout.getChildren().add(OptionsMenu);
        emailLayout.getChildren().add(emailBorder);
        emailLayout.getChildren().add(messageLabel);
        emailLayout.getChildren().add(toLabel);
        emailLayout.getChildren().add(subjectLabel);
        emailLayout.getChildren().add(toField);
        emailLayout.getChildren().add(subjectField);
        emailLayout.getChildren().add(messageField);
        emailLayout.getChildren().add(bannerImage);
        emailLayout.getChildren().add(back);
        emailLayout.getChildren().add(send);
        emailLayout.getChildren().add(discard);



    }

}




