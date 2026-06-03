package library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {
    private static final Path DATA_DIR = Path.of("data");
    private static final Path BOOKS_FILE = DATA_DIR.resolve("books.txt");
    private static final Path MEMBERS_FILE = DATA_DIR.resolve("members.txt");
    private static final Path BOOKS_CSV = DATA_DIR.resolve("books.csv");
    private static final Path MEMBERS_CSV = DATA_DIR.resolve("members.csv");

    public List<Book> loadBooks() {
        ensureDataFiles();
        List<Book> books = new ArrayList<>();

        try {
            for (String line : Files.readAllLines(BOOKS_FILE, StandardCharsets.UTF_8)) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length < 8) {
                    System.out.println("Skipped invalid book record: " + line);
                    continue;
                }

                List<String> reservations = splitList(parts[7]);
                LocalDate dueDate = parts[6].isBlank() ? null : LocalDate.parse(parts[6]);
                books.add(new Book(
                        parts[0],
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        Boolean.parseBoolean(parts[4]),
                        parts[5].isBlank() ? null : parts[5],
                        dueDate,
                        reservations
                ));
            }
        } catch (IOException | RuntimeException exception) {
            System.out.println("Could not load books: " + exception.getMessage());
        }

        return books;
    }

    public List<Member> loadMembers() {
        ensureDataFiles();
        List<Member> members = new ArrayList<>();

        try {
            for (String line : Files.readAllLines(MEMBERS_FILE, StandardCharsets.UTF_8)) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length < 3) {
                    System.out.println("Skipped invalid member record: " + line);
                    continue;
                }

                members.add(new Member(parts[0], parts[1], splitList(parts[2])));
            }
        } catch (IOException | RuntimeException exception) {
            System.out.println("Could not load members: " + exception.getMessage());
        }

        return members;
    }

    public void saveBooks(List<Book> books) {
        ensureDataFiles();
        List<String> lines = new ArrayList<>();

        for (Book book : books) {
            lines.add(String.join("|",
                    safe(book.getIsbn()),
                    safe(book.getTitle()),
                    safe(book.getAuthor()),
                    String.valueOf(book.getYear()),
                    String.valueOf(book.isAvailable()),
                    safe(book.getBorrowedBy()),
                    book.getDueDate() == null ? "" : book.getDueDate().toString(),
                    String.join(",", book.getReservationMemberIds())
            ));
        }

        writeLines(BOOKS_FILE, lines, "books");
    }

    public void saveMembers(List<Member> members) {
        ensureDataFiles();
        List<String> lines = new ArrayList<>();

        for (Member member : members) {
            lines.add(String.join("|",
                    safe(member.getId()),
                    safe(member.getName()),
                    String.join(",", member.getBorrowedBookIsbns())
            ));
        }

        writeLines(MEMBERS_FILE, lines, "members");
    }

    public void exportCsv(List<Book> books, List<Member> members) {
        ensureDataFiles();
        List<String> bookRows = new ArrayList<>();
        bookRows.add("isbn,title,author,year,available,borrowed_by,due_date,reservations");
        for (Book book : books) {
            bookRows.add(String.join(",",
                    csv(book.getIsbn()),
                    csv(book.getTitle()),
                    csv(book.getAuthor()),
                    String.valueOf(book.getYear()),
                    String.valueOf(book.isAvailable()),
                    csv(book.getBorrowedBy()),
                    csv(book.getDueDate() == null ? "" : book.getDueDate().toString()),
                    csv(String.join(";", book.getReservationMemberIds()))
            ));
        }

        List<String> memberRows = new ArrayList<>();
        memberRows.add("id,name,borrowed_books");
        for (Member member : members) {
            memberRows.add(String.join(",",
                    csv(member.getId()),
                    csv(member.getName()),
                    csv(String.join(";", member.getBorrowedBookIsbns()))
            ));
        }

        writeLines(BOOKS_CSV, bookRows, "book CSV export");
        writeLines(MEMBERS_CSV, memberRows, "member CSV export");
    }

    private void ensureDataFiles() {
        try {
            Files.createDirectories(DATA_DIR);
            if (Files.notExists(BOOKS_FILE)) {
                Files.createFile(BOOKS_FILE);
            }
            if (Files.notExists(MEMBERS_FILE)) {
                Files.createFile(MEMBERS_FILE);
            }
        } catch (IOException exception) {
            System.out.println("Could not prepare data files: " + exception.getMessage());
        }
    }

    private void writeLines(Path path, List<String> lines, String label) {
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println("Could not save " + label + ": " + exception.getMessage());
        }
    }

    private List<String> splitList(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(value.split(",")));
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", " ").replace(System.lineSeparator(), " ").trim();
    }

    private String csv(String value) {
        String cleaned = value == null ? "" : value;
        return "\"" + cleaned.replace("\"", "\"\"") + "\"";
    }
}
