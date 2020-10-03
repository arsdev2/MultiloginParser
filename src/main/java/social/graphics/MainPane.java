package social.graphics;

import com.google.gson.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import social.MainClass;
import utils.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MainPane extends Application {
    private static final double SCREEN_WIDTH = 900;
    private static final double SCREEN_HEIGHT =450;
    private TextArea logTextField = null;
    private JsonObject object = new JsonObject(), twitter = new JsonObject(), facebook = new JsonObject(), telegram = new JsonObject();
    private JsonArray actions =  new JsonArray(), twitterArray = new JsonArray(), facebookArray = new JsonArray(), telegramArray = new JsonArray();
    private ArrayList<Group> groupsList = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        twitter.add("twitter", twitterArray);
        facebook.add("facebook", facebookArray);
        telegram.add("telegram", telegramArray);
        actions.add(twitter);
        actions.add(facebook);
        actions.add(telegram);
        object.add("actions", actions);

        BorderPane rootBorderPane = new BorderPane();
        Scene scene = new Scene(rootBorderPane, SCREEN_WIDTH, SCREEN_HEIGHT);

        HBox horizontal = new HBox();

        HBox logHorizontal = new HBox();

        logTextField = new TextArea();
        logHorizontal.setPrefHeight(SCREEN_HEIGHT - horizontal.getHeight() - 30);
        logTextField.setPrefHeight(SCREEN_HEIGHT - horizontal.getHeight() - 30);
        logTextField.setEditable(false);
        logTextField.setMouseTransparent(true);
        logTextField.setFocusTraversable(false);
        logTextField.setFont(Font.font("Consolas"));

        logTextField.setPrefWidth(SCREEN_WIDTH);

        HBox.setMargin(logTextField, new Insets(0, 25, 200, 25));

        logHorizontal.getChildren().addAll(logTextField);

        VBox twitterVBox = new VBox();

        Text text = new Text("Twitter");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        final TextField twitterTextField = new TextField();
        twitterTextField.setPromptText("Текст репоста");
        twitterTextField.setVisible(false);

        ChoiceBox actionTypeSelect = new ChoiceBox(FXCollections.observableArrayList("Подписка", "Ретвит"));
        actionTypeSelect.setValue("Подписка");
        actionTypeSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String value = actionTypeSelect.getItems().get((Integer) number2).toString();
                if(value.equals("Подписка")){
                    twitterTextField.setVisible(false);
                    VBox.setMargin(twitterTextField, new Insets(0, 0, 0, 0));
                }else{
                    twitterTextField.setVisible(true);
                    VBox.setMargin(twitterTextField, new Insets(0, 0, 10, 25));
                }
            }
        });

        TextField linkTextField = new TextField();
        linkTextField.setPromptText("Линк");

        TextField twEmpty1 = new TextField();
        twEmpty1.setVisible(false);

        TextField twEmpty2 = new TextField();
        twEmpty2.setVisible(false);

        Button twitterAddButton = new Button("Добавить");

        VBox.setMargin(text, new Insets(10, 0, 15, 25));
        VBox.setMargin(actionTypeSelect, new Insets(0, 0, 10, 25));
        VBox.setMargin(twitterAddButton, new Insets(0, 0, 10, 25));
        VBox.setMargin(twitterTextField, new Insets(0, 0, 0, 0));
        VBox.setMargin(twEmpty1, new Insets(0, 0, 0, 0));
        VBox.setMargin(twEmpty2, new Insets(0, 0, 0, 0));
        VBox.setMargin(linkTextField, new Insets(0, 0, 10, 25));

        twitterVBox.getChildren().addAll(text, actionTypeSelect, linkTextField, twitterTextField,twEmpty1, twEmpty2, twitterAddButton);

        twitterAddButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!linkTextField.getText().equals("")) {
                    if (actionTypeSelect.getValue().toString().equals("Подписка")) {
                        JsonObject actionObject = new JsonObject();
                        actionObject.add("link", new JsonPrimitive(linkTextField.getText()));
                        actionObject.add("type", new JsonPrimitive("twSub"));
                        twitterArray.add(actionObject);
                        System.out.println(object.toString());
                        System.out.println("SUBSCRIBTION");
                    } else {
                        JsonObject actionObject = new JsonObject();
                        actionObject.add("link", new JsonPrimitive(linkTextField.getText()));
                        actionObject.add("text", new JsonPrimitive(twitterTextField.getText()));
                        actionObject.add("type", new JsonPrimitive("twRepost"));
                        twitterArray.add(actionObject);
                        System.out.println(object.toString());
                        System.out.println("Retweet");
                    }
                    log("Success add task");
                }else{
                    log("Please type link!");
                }
            }
        });

        VBox facebook = new VBox();

        Text facebookLabelText = new Text("Facebook");
        facebookLabelText.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField facebookRepostText = new TextField();
        facebookRepostText.setPromptText("Текст репоста(если есть)");
        facebookRepostText.setVisible(false);

        ChoiceBox facebookTypeChoice = new ChoiceBox(FXCollections.observableArrayList("Подписка", "Репост"));
        facebookTypeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.toString();
            if(!value.equals("Репост")){
                facebookRepostText.setVisible(false);
                VBox.setMargin(facebookRepostText, new Insets(0, 0, 0, 0));
            }else{
                facebookRepostText.setVisible(true);
                VBox.setMargin(facebookRepostText, new Insets(0, 0, 10, 5));
            }
        });
        facebookTypeChoice.setValue("Подписка");

        TextField facebookLinkField = new TextField();
        facebookLinkField.setPromptText("Линк");

        Button confirmFacebookButton = new Button("Добавить");

        TextField fbEmpty1 = new TextField();
        fbEmpty1.setVisible(false);
        TextField fbEmpty2 = new TextField();
        fbEmpty2.setVisible(false);

        VBox.setMargin(facebookLabelText, new Insets(10, 0, 15, 5));
        VBox.setMargin(facebookTypeChoice, new Insets(0, 0, 10, 5));
        VBox.setMargin(facebookLinkField, new Insets(0, 0, 10, 5));
        VBox.setMargin(facebookRepostText, new Insets(0, 0, 0, 0));
        VBox.setMargin(fbEmpty1, new Insets(0, 0, 0, 0));
        VBox.setMargin(fbEmpty2, new Insets(0, 0, 0, 0));
        VBox.setMargin(confirmFacebookButton, new Insets(0, 0, 10, 5));

        facebook.getChildren().addAll(facebookLabelText, facebookTypeChoice,facebookLinkField, facebookRepostText,fbEmpty1, fbEmpty2,  confirmFacebookButton);

        confirmFacebookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!facebookLinkField.getText().equals("")) {
                    if (facebookTypeChoice.getValue().toString().equals("Подписка")) {
                        JsonObject actionObject = new JsonObject();
                        actionObject.add("type", new JsonPrimitive("groupSub"));
                        actionObject.add("link", new JsonPrimitive(facebookLinkField.getText()));
                        facebookArray.add(actionObject);
                    } else {
                        JsonObject actionObject = new JsonObject();
                        actionObject.add("type", new JsonPrimitive("repost"));
                        actionObject.add("link", new JsonPrimitive(facebookLinkField.getText()));
                        actionObject.add("text", new JsonPrimitive(facebookRepostText.getText()));
                        facebookArray.add(actionObject);
                    }
                    System.out.println(object.toString());
                    log("Success add task!");
                }else{
                    log("Please enter value in link field");
                }
            }
        });


        VBox telegram = new VBox();

        Text telegramTitle = new Text("Telegram");
        telegramTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField telegramTextBot = new TextField();
        telegramTextBot.setPromptText("Текст для бота");
        telegramTextBot.setVisible(false);

        TextField telegramBotButtonCount = new TextField();
        telegramBotButtonCount.setPromptText("Номер сообщения");
        telegramBotButtonCount.setVisible(false);

        ChoiceBox box = new ChoiceBox(FXCollections.observableArrayList("Подписка", "Бот", "Бот(кнопка)"));
        box.setValue("Подписка");
        box.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String value = newValue.toString();
                if(!value.contains("Бот")){
                    telegramTextBot.setVisible(false);
                    VBox.setMargin(telegramTextBot, new Insets(0, 0, 0, 0));
                }else{
                    telegramTextBot.setVisible(true);
                    VBox.setMargin(telegramTextBot, new Insets(0, 0, 10, 5));
                }
                if(!value.equals("Бот(кнопка)")){
                    telegramBotButtonCount.setVisible(false);
                    VBox.setMargin(telegramBotButtonCount, new Insets(0, 0, 0, 0));
                }else{
                    telegramBotButtonCount.setVisible(true);
                    VBox.setMargin(telegramBotButtonCount, new Insets(0, 0, 10, 5));
                }
            }
        });

        TextField telegramLinkSubscribtion = new TextField();
        telegramLinkSubscribtion.setPromptText("Линк");

        Button telegramAddButton = new Button("Добавить");



        VBox.setMargin(telegramTitle, new Insets(10, 0, 15, 5));
        VBox.setMargin(box, new Insets(0, 0, 10, 5));
        VBox.setMargin(telegramLinkSubscribtion, new Insets(0, 0, 10, 5));
        VBox.setMargin(telegramTextBot, new Insets(0, 0, 10, 5));
        VBox.setMargin(telegramBotButtonCount, new Insets(0, 0, 10, 5));
        VBox.setMargin(telegramAddButton, new Insets(0, 0, 10, 5));

        telegram.getChildren().addAll(telegramTitle, box, telegramLinkSubscribtion, telegramTextBot,telegramBotButtonCount, telegramAddButton);

        telegramAddButton.setOnAction((ActionEvent event)-> {
            JsonObject actionObject = new JsonObject();
            if(telegramLinkSubscribtion.getText().equals("")){
                System.out.println("return");
                log("Please fill link field!");
                return;
            }
            actionObject.add("link", new JsonPrimitive(telegramLinkSubscribtion.getText()));
            String selectedOperation = box.getSelectionModel().getSelectedItem().toString();
            if(selectedOperation.equals("Подписка")){
                actionObject.add("type", new JsonPrimitive("channelSub"));
            }else if(selectedOperation.contains("Бот")){
                actionObject.add("type", new JsonPrimitive("botSend"));
                JsonObject com = new JsonObject();
                if(selectedOperation.equals("Бот")){
                    com.add("type", new JsonPrimitive("text"));
                }else{
                    com.add("type", new JsonPrimitive("botBtnClick"));
                    com.add("num", new JsonPrimitive(Integer.parseInt(telegramBotButtonCount.getText())));
                }
                com.add("value", new JsonPrimitive(telegramTextBot.getText()));
                actionObject.add("com", com);
            }
            telegramArray.add(actionObject);
            log("Success add task!");
            System.out.println(object);
        });
        BorderPane confirmAction = new BorderPane();

        TextField tokenValue = new TextField();
        tokenValue.setPromptText("Токен");


        HBox hBox = new HBox();
        VBox accountsVBox = new VBox();


        Text configLabel = new Text("Configuration");
        configLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));


        HBox accountsHBox = new HBox();

        ChoiceBox accountsTagList = new ChoiceBox();


        Button selectUserFromFroup = new Button("Выбрать ползователей");
        selectUserFromFroup.setOnAction((handler)->{
            String groupName = "";
            if(accountsTagList.getSelectionModel().getSelectedItem() != null){
                groupName = accountsTagList.getSelectionModel().getSelectedItem().toString();
            }else{
                log("Group not found!");
                return;
            }
            Group selectedGroup = null;
            for(Group group : groupsList){
                if(group.name.equals(groupName)){
                    selectedGroup = group;
                }
            }
            if(selectedGroup == null){
                log("Group not found!");
                return;
            }
            ArrayList<String> usersNamesList = new ArrayList<>();
            for(User user : selectedGroup.accounts){
                usersNamesList.add(user.name);
            }
            SelectUserFromGroupPane pane = new SelectUserFromGroupPane(selectedGroup, scene, rootBorderPane, usersNamesList.toArray(new String[0]));
            scene.setRoot(pane);
        });

        HBox.setMargin(selectUserFromFroup, new Insets(0, 0, 0, 10));
        accountsHBox.getChildren().addAll(accountsTagList, selectUserFromFroup);


        Button tokenConfirm = new Button("OK");
        tokenConfirm.setOnAction((handle)->{
            if(tokenValue.getText().equals("")){
                log("Enter token!");
                return;
            }
            String accountJsonData = Common.getRequest("https://api.multiloginapp.com/v1/tag/list?token=" + tokenValue.getText());
            JsonObject accountJsonObject = new JsonParser().parse(accountJsonData).getAsJsonObject();
            if(accountJsonObject.get("status") != null && accountJsonObject.get("status").getAsString().equals("ERROR")){
                log("Error - " + accountJsonObject.get("value").getAsString());
                return;
            }
            JsonArray accountsDataJsonArray = accountJsonObject.getAsJsonArray("data");

            accountsTagList.getItems().clear();
            groupsList.clear();
            for(JsonElement accountJson : accountsDataJsonArray){
                JsonObject accountDataJsonObject = accountJson.getAsJsonObject();
                String name = accountDataJsonObject.get("name").getAsString();
                String sid = accountDataJsonObject.get("sid").getAsString();
                String userAccountJsonBody = getUsersByTagId(tokenValue.getText(), sid);
                JsonArray userAccountJsonArray;
                try {
                    userAccountJsonArray = new JsonParser().parse(userAccountJsonBody).getAsJsonObject().get("data").getAsJsonArray();
                }catch (Exception e){
                    System.out.println(userAccountJsonBody);
                    return;
                }
                ArrayList<User> usersList = new ArrayList<>();
                for(JsonElement userAccountJsonElement : userAccountJsonArray){
                    User user = new User(userAccountJsonElement.getAsJsonObject().get("name").getAsString(), userAccountJsonElement.getAsJsonObject().get("sid").getAsString(), true);
                    usersList.add(user);
                }
                groupsList.add(new Group(sid, name, usersList));
            }
            for(Group group : groupsList){
                accountsTagList.getItems().add(group.name);
            }
            accountsTagList.getSelectionModel().selectFirst();
        });
        tryToLoadToken(tokenValue, tokenConfirm);


        VBox.setMargin(configLabel, new Insets(10, 0, 10, 0));
        VBox.setMargin(tokenValue, new Insets(10, 0, 10, 0));
        VBox.setMargin(tokenConfirm, new Insets(10, 0, 10, 0));
        VBox.setMargin(accountsHBox, new Insets(10, 0, 10, 0));

        accountsVBox.getChildren().addAll(configLabel, tokenValue, tokenConfirm, accountsHBox);

        Button confirmActionButton = new Button("Запустить");
        confirmActionButton.setOnAction((handle)-> {
                Thread thread = new Thread(()->{
                    String data = object.toString();
                    twitterArray = new JsonArray();
                    facebookArray = new JsonArray();
                    telegramArray = new JsonArray();
                    actions = new JsonArray();
                    object = new JsonObject();
                    JsonObject twObj = new JsonObject();
                    twObj.add("twitter" , twitterArray);
                    JsonObject fbObj = new JsonObject();
                    fbObj.add("facebook" , facebookArray);
                    JsonObject tgObj = new JsonObject();
                    tgObj.add("telegram" , telegramArray);
                    actions.add(twObj);
                    actions.add(fbObj);
                    actions.add(tgObj);
                    object.add("actions", actions);
                    if(accountsTagList.getSelectionModel().getSelectedItem() == null){
                        log("Select at least one group!");
                        return;
                    }
                    String tagName = accountsTagList.getSelectionModel().getSelectedItem().toString();
                    Group neededGroup = null;
                    for(Group group : groupsList){
                        if(group.name.equals(tagName)){
                            neededGroup = group;
                            break;
                        }
                    }
                    if(neededGroup == null){
                        log("Profile not found!");
                        return;
                    }
                    for(User user : neededGroup.accounts){
                        if(user.selected) {
                            String[] args = {user.sid, data};
                            MainClass.main(args);
                        }
                    }
                    log("Running tasks!");
                });
                thread.start();
            });

        Button resetButton = new Button("Сбросить задания");
        resetButton.setOnAction((handle)->{
            twitterArray = new JsonArray();
            facebookArray = new JsonArray();
            telegramArray = new JsonArray();
            actions = new JsonArray();
            object = new JsonObject();
            JsonObject twObj = new JsonObject();
            twObj.add("twitter" , twitterArray);
            JsonObject fbObj = new JsonObject();
            fbObj.add("facebook" , facebookArray);
            JsonObject tgObj = new JsonObject();
            tgObj.add("telegram" , telegramArray);
            actions.add(twObj);
            actions.add(fbObj);
            actions.add(tgObj);
            object.add("actions", actions);
        });

        HBox.setMargin(confirmActionButton, new Insets(0, 10, 10, 0));
        HBox.setMargin(resetButton, new Insets(0, 0, 10, 0));

        hBox.getChildren().addAll(confirmActionButton, resetButton);

        BorderPane.setMargin(confirmActionButton, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(tokenValue, new Insets(30, 0, 0, 0));

        confirmAction.setBottom(hBox);
        confirmAction.setCenter(accountsVBox);

        horizontal.getChildren().add(twitterVBox);
        horizontal.getChildren().add(getSeperator(horizontal));
        horizontal.getChildren().add(facebook);
        horizontal.getChildren().add(getSeperator(horizontal));
        horizontal.getChildren().add(telegram);
        horizontal.getChildren().add(getSeperator(horizontal));
        horizontal.getChildren().add(confirmAction);


        rootBorderPane.setLeft(horizontal);
        rootBorderPane.setBottom(logHorizontal);
        String[] randomWords = new String[256];
        for(int i = 0;i<256;i++){
            StringBuilder word = new StringBuilder();
            char[] lett = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            for(int a = 0;a<10;a++){
                word.append(lett[ThreadLocalRandom.current().nextInt(0, lett.length)]);
            }
            randomWords[i] = word.toString();
        }
       // SelectUserFromGroupPane selectUserFromGroupPane = new SelectUserFromGroupPane(randomWords);
        scene.setRoot(rootBorderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void tryToLoadToken(TextField tokenField, Button confirm){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(System.getProperty("user.home") + "\\.multiloginapp.com\\app.properties")));
            tokenField.setText(properties.get("token").toString());
            confirm.fire();
        } catch (IOException e) {
            log("Failed to find token on your system!");
        }
    }

    private void log(String message){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message = dateFormat.format(date)+ "> " + message;
        logTextField.setText(message + "\n" + logTextField.getText());
    }

    private Separator getSeperator(HBox horizontal){
        Separator separator = new Separator();
        separator.setPrefSize(50, horizontal.getHeight());
        separator.setOrientation(Orientation.VERTICAL);
        return separator;
    }

    private String getUsersByTagId(String token, String sid){
       String result = Common.getRequest(String.format("https://api.multiloginapp.com/v1/tag/profile/list?token=%s&tagId=%s", token, sid));
       return result.contains("ERROR") ? "{\"status\":\"ERROR\", \"value\":\"Unathorized\"}" : result;
    }
}
