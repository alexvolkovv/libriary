package coursach.libriary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileSystemLibrary implements LibraryInterface {

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private File booksFile = new File("./books.dat");


    @Override
    public void add(Book book) {
        bookList.add(book);
    }

    @Override
    public void delete(Book book) {
        bookList.remove(book);
    }

    @Override
    public void update(Book book) {

    }

    public void clear() {
        this.bookList = FXCollections.observableArrayList();
        this.writeFile();
    }

    private void checkFile() {
        try {
            if (!booksFile.exists()) {
                booksFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean writeFile() {
        try {
            this.checkFile();

            FileOutputStream fos = new FileOutputStream(this.booksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(new ArrayList<Book>(this.bookList));
            oos.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean readFile() {
        try {
            this.checkFile();

            FileInputStream fis = new FileInputStream(this.booksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            this.bookList = FXCollections.observableArrayList((List<Book>) ois.readObject());
            ois.close();

            return true;
        } catch (Exception e) {
            System.out.println("Ошибка в чтении файлов");
            e.printStackTrace();
        }

        return false;
    }

    public void fillTestData() {
        this.readFile();

        if (this.bookList.size() != 0) {
            return;
        }

        this.bookList.add(new Book("Капитанская дочка", "Пушкин А.С.", "1836"));
        this.bookList.add(new Book("Преступление и наказание", "Достоевский Ф.М.", "1866"));
        this.bookList.add(new Book("Идиот", "Достоевский Ф.М.", "1868"));
        this.bookList.add(new Book("Три товарища", "Эрих М.Р.", "1936"));
        this.bookList.add(new Book("Финансист", "Теодор Драйзер", "1912"));

        this.writeFile();
    }

    public File getBooksFile() {
        return this.booksFile;
    }

    public ObservableList<Book> getBookList() {
        return this.bookList;
    }
}
