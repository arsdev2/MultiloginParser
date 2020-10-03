package social.graphics;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.util.SortedMap;
import java.util.TreeMap;

public class SelectUserFromGroupPane extends ScrollPane {

    public SelectUserFromGroupPane(Group group, Scene scene, BorderPane rootBorderPane, String... names) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        SortedMap<String, String>  dataBackup = new TreeMap<>();
        int column = 0, row = 0;
        for(String name : names){
            HBox hBox = new HBox();
            Text text = new Text(name);
            CheckBox checkBox = new CheckBox();
            try {
                checkBox.setSelected(group.getUserByName(name).selected);
            }catch (NullPointerException e){
                checkBox.setSelected(true);
            }
            checkBox.setOnAction((handler)->{
                group.getUserByName(name).selected = checkBox.isSelected();
            });
            HBox.setMargin(checkBox, new Insets(0, 10, 0 ,10));
            HBox.setMargin(text, new Insets(0, 10, 0, 10));
            hBox.getChildren().addAll(checkBox, text);
            if(column > 5){
                row++;
                column = 0;
            }
            column++;
            grid.add(hBox, column, row);
        }
        BorderPane borderPane = new BorderPane();
        Button okButton = new Button("OK");
        okButton.setOnAction((handler)->{
            scene.setRoot(rootBorderPane);
        });
        borderPane.setCenter(okButton);
        grid.add(borderPane, 0, row+1);
        setContent(grid);
    }
}
