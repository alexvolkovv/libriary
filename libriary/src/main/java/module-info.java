module coursach.libriary {
    requires javafx.controls;
    requires javafx.fxml;


    opens coursach.libriary to javafx.fxml;
    exports coursach.libriary;
}