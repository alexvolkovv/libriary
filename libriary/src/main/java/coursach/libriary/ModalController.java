package coursach.libriary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalController {
    @FXML
    private Button cancelButton;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtEdition;

    private Book book;

    public Book getBook() {return this.book;}

    public void setBook(Book book) {
        this.book = book;

        txtName.setText(book.getName());
        txtAuthor.setText(book.getAuthor());
        txtEdition.setText(book.getEdition());

    }

    public void actionClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void saveInfo(ActionEvent event) {
        if (checkFields()) {
            return;
        }

        book.setName(txtName.getText());
        book.setAuthor(txtAuthor.getText());
        book.setEdition(txtEdition.getText());
        actionClose(event);
    }

    public boolean checkFields() {
        if (txtName.getText() == null || txtAuthor.getText() == null || txtEdition.getText() == null) {
            DialogManager.showErrorDialog("Ошибка", "Заполните все поля");
            return true;
        }

        return false;
    }
}
