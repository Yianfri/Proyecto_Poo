package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Instructions {

    public static void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMaxWidth(400);

        Label label = new Label();
        label.setText(message);

        Button ok = new Button("De acuerdo");
        ok.setMaxWidth(80);
        ok.setCursor(Cursor.HAND);
        ok.setFont(new Font( 12));

        ok.setOnAction(e->{
            window.close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, ok);
        layout.setAlignment(Pos.CENTER);


        Scene scene = new Scene(layout,400,250);
        window.setScene(scene);
        window.showAndWait();
    }

}
