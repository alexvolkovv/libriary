package coursach.libriary;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class LibriaryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibriaryApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Библиотека");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setScene(scene);
        Image img = new Image("file:./icon.png");
        stage.getIcons().add(img);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!MainController.hasChanges) {
                    return;
                }

                event.consume();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Пожалуйста, подтвердите действие");
                alert.setHeaderText("Вы не сохранили изменения. Выйти?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    stage.close();
                }
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}