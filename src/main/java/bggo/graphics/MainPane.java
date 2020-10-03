package bggo.graphics;

import bggo.MainClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jsunsoft.http.HttpRequest;
import com.jsunsoft.http.HttpRequestBuilder;
import com.jsunsoft.http.ResponseDeserializer;
import com.jsunsoft.http.ResponseHandler;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.nashorn.internal.parser.JSONParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainPane extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        BorderPane rootBorderPane = new BorderPane();
        HBox horizontal = new HBox();





        TextField tokenField = new TextField();
        tokenField.setPromptText("Token");

        TextField firstPairElement = new TextField();
        firstPairElement.setPromptText("Session id of first profile");


        TextField secondPairElement = new TextField();
        secondPairElement.setPromptText("Session id of second profile");


        VBox.setMargin(tokenField, new Insets(30, 0,0,30));


        VBox twitterVBox = new VBox();


        Text text = new Text("Bot");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 18));


        TextField linkTextField = new TextField();
        linkTextField.setPromptText("Link");

        TextField countOfCoins = new TextField();
        countOfCoins.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d+\\.+\\d*")) {
                    countOfCoins.setText(newValue.replaceAll("[^\\d+\\.+\\d*]", ""));
                }
            }
        });
        countOfCoins.setPromptText("Count of coins");

        VBox.setMargin(text, new Insets(10, 0, 15, 25));
        VBox.setMargin(firstPairElement, new Insets(0, 0, 10, 25));
        VBox.setMargin(secondPairElement, new Insets(0, 0, 10, 25));
        VBox.setMargin(linkTextField, new Insets(0, 0, 10, 25));
        VBox.setMargin(countOfCoins, new Insets(0, 0, 10, 25));

        twitterVBox.getChildren().addAll(text, firstPairElement, secondPairElement, linkTextField, countOfCoins);

        BorderPane confirmAction = new BorderPane();

        Button confirmActionButton = new Button("Run");
        confirmActionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String firstPair = firstPairElement.getText();
                String secondPair = secondPairElement.getText();
                if(!linkTextField.getText().equals("") && !countOfCoins.getText().equals("") && Double.parseDouble(countOfCoins.getText()) >= 0){
                    try {
                        firstPair = firstPair.replaceAll("\"", "");
                        secondPair = secondPair.replaceAll("\"", "");
                        String[] dat = {firstPair, secondPair, linkTextField.getText(), countOfCoins.getText()};
                        for (String el : dat) {
                            System.out.println("DATA - " + el);
                        }
                        MainClass.main(dat);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });


        BorderPane.setMargin(confirmActionButton, new Insets(0, 0, 0, 30));

        confirmAction.setCenter(confirmActionButton);


        horizontal.getChildren().add(twitterVBox);
        horizontal.getChildren().add(getSeperator(horizontal));
        horizontal.getChildren().add(confirmAction);



        rootBorderPane.setLeft(horizontal);
        Scene scene = new Scene(rootBorderPane, 400, 200);
        scene.setRoot(rootBorderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    private Separator getSeperator(HBox horizontal){
        Separator separator = new Separator();
        separator.setPrefSize(50, horizontal.getHeight());
        separator.setOrientation(Orientation.VERTICAL);
        return separator;
    }
}
