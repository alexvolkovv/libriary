package coursach.libriary;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class MainController {
    @FXML
    private TableView tableLibrary;

    @FXML
    private TableColumn<Book, String> tableColumnName;

    @FXML
    private TableColumn<Book, String> tableColumnAuthor;

    @FXML
    private TableColumn<Book, String> tableColumnDateEdition;

    @FXML
    private Label countBooks;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField txtSearch;

    @FXML
    private MenuItem menuSave;


    private Parent fxmlModal;

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private ModalController modalController;

    private Stage modalStage;

    private FileSystemLibrary library = new FileSystemLibrary();

    private ObservableList<Book> backupList;

    public static boolean hasChanges = false;


    @FXML
    private void initialize() {
        this.initializeModalWindow();
        this.initializeTable();

        this.backupList = FXCollections.observableArrayList();
        this.backupList.addAll(this.library.getBookList());

        this.initListeners();
    }

    public void search() {
        library.getBookList().clear();

        for (Book book : this.backupList) {
            if (book.getName().toLowerCase().contains(txtSearch.getText().toLowerCase()) ||
                    book.getEdition().toLowerCase().contains(txtSearch.getText().toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(txtSearch.getText().toLowerCase())) {
                this.library.add(book);
            }
        }
    }

        public void initListeners() {
            this.backupList.addListener(new ListChangeListener<Book>() {
                @Override
                public void onChanged(Change<? extends Book> change) {
                    updateCount();
                }
            });

            tableLibrary.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        Book selectedBook = (Book) tableLibrary.getSelectionModel().getSelectedItem();
                        modalController.setBook(selectedBook);
                        Window parentWindow = ((Node)mouseEvent.getSource()).getScene().getWindow();
                        showDialog(parentWindow);
                        hasChanges = true;
                        updateTable();
                    }
                }
            });
        }

    public void initializeTable() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        tableColumnAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        tableColumnDateEdition.setCellValueFactory(new PropertyValueFactory<Book, String>("edition"));

        library.fillTestData();

        tableLibrary.setItems(library.getBookList());
        updateCount();
    }

    public void initializeModalWindow() {
        try {
            fxmlLoader.setLocation(getClass().getResource("modal-add.fxml"));
            fxmlModal = fxmlLoader.load();
            modalController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCount() {
        this.countBooks.setText("Количество книг: " + this.library.getBookList().size());
    }

    public void updateTable() {
        ObservableList<Book> copy = FXCollections.observableArrayList(library.getBookList());
        library.getBookList().removeAll(library.getBookList());

        for (int i = 0; i < copy.size(); i++) {
            library.add(copy.get(i));
        }
    }

    public void editButtonClick(ActionEvent event) {
        Object source = event.getSource();

        if (!(source instanceof Button)) {
            return;
        }

        Button clickedButton = (Button) source;

        Book selectedBook = (Book) tableLibrary.getSelectionModel().getSelectedItem();

        Window parentWindow = ((Node)event.getSource()).getScene().getWindow();

        switch (clickedButton.getId()) {
            case "addButton":
                this.modalController.setBook(new Book());
                this.showDialog(parentWindow);

                if (modalController.getBook().getAuthor() != null) {
                    this.library.add(modalController.getBook());
                    this.backupList.add(modalController.getBook());
                    this.hasChanges = true;
                }

                break;
            case "editButton":
                if (!isSelectBook(selectedBook)) {
                    return;
                }

                this.modalController.setBook(selectedBook);
                this.showDialog(parentWindow);
                this.updateTable();
                this.hasChanges = true;
                break;
            case "removeButton":
                if (!isSelectBook(selectedBook)) {
                    return;
                }

                this.library.delete(selectedBook);
                this.backupList.remove(selectedBook);
                this.hasChanges = true;
                break;
        }
    }

    public boolean isSelectBook(Book book) {
        if (book == null) {
            DialogManager.showInfoDialog("Ошибка", "Выберите книгу");

            return false;
        }

        return true;
    }

    public void clear() {
        txtSearch.setText("");
        search();
    }

    public void deleteAllBooks() {
        this.library.getBookList().removeAll(this.library.getBookList());
        this.backupList.removeAll(this.backupList);
        hasChanges = true;
    }

    public void saveChanges() {
        if (this.library.getBookList().size() < this.backupList.size()) {
            this.library.getBookList().clear();
            this.library.getBookList().addAll(this.backupList);
        }

        library.writeFile();
        hasChanges = false;
    }

    public void showDialog(Window parentWindow) {
        if (modalStage == null) {
            modalStage = new Stage();
            modalStage.setTitle("Редактирование записи");
            modalStage.setMinWidth(200);
            modalStage.setMinHeight(100);
            modalStage.setResizable(false);
            modalStage.setScene(new Scene(fxmlModal));
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(parentWindow);
        }

        modalStage.showAndWait();
    }
}
