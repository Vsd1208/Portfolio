package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member {
    private String id;
    private String name;
    private final List<String> borrowedBookIsbns;

    public Member(String id, String name) {
        this(id, name, new ArrayList<>());
    }

    public Member(String id, String name, List<String> borrowedBookIsbns) {
        this.id = id;
        this.name = name;
        this.borrowedBookIsbns = new ArrayList<>(borrowedBookIsbns);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBorrowedBookIsbns() {
        return Collections.unmodifiableList(borrowedBookIsbns);
    }

    public void borrowBook(String isbn) {
        if (!borrowedBookIsbns.contains(isbn)) {
            borrowedBookIsbns.add(isbn);
        }
    }

    public void returnBook(String isbn) {
        borrowedBookIsbns.remove(isbn);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Borrowed Books: %d",
                id, name, borrowedBookIsbns.size());
    }
}
