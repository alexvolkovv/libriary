package coursach.libriary;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private String author;
    private String edition;

    public Book() {}

    public Book(String name, String author, String edition) {
        this.name = name;
        this.author = author;
        this.edition = edition;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getAuthor() {return author;}

    public void setAuthor(String author) {this.author = author;}

    public String getEdition() {return edition;}

    public void setEdition(String edition) {this.edition = edition;}
}