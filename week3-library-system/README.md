# Console-Based Library Management System

## Project Description

A Java console application for managing library operations including book tracking, member registration, borrowing, returning, reservations, overdue fine calculation, and file-based data persistence.

## Project Overview and Objectives

The goal of this Week 3 project is to practice Java basics and object-oriented programming through a complete command-line application. The project demonstrates classes, objects, encapsulation, constructors, methods, data types, control structures, ArrayLists, file I/O, exception handling, input validation, and formatted console output.

## Features

- Add, remove, view, and search books
- Register and view library members
- Borrow and return books with two-week due dates
- Calculate overdue fines at $2 per overdue day
- Reserve unavailable books
- Save and load data from text files
- Export books and members to CSV files
- Validate console input
- Display library statistics

## How to Run

### Option 1: Compile with javac

```bash
javac -d bin src/main/java/library/*.java
java -cp bin library.Main
```

### Option 2: Run with Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass=library.Main
```

The application reads and writes data in the `data` folder. Run commands from inside `week3-library-system` so the file paths resolve correctly.

## Sample Menu

```text
=== LIBRARY MANAGEMENT SYSTEM ===
1. Add New Book
2. View All Books
3. Search Books
4. Remove Book
5. Register Member
6. View All Members
7. Borrow Book
8. Return Book
9. Reserve Book
10. View Library Statistics
11. Export CSV and Exit

Enter your choice:
```

## Project Structure

```text
week3-library-system/
|-- src/
|   |-- main/
|   |   `-- java/
|   |       `-- library/
|   |           |-- Main.java
|   |           |-- Book.java
|   |           |-- Member.java
|   |           |-- Library.java
|   |           `-- FileHandler.java
|   `-- resources/
|-- data/
|   |-- books.txt
|   `-- members.txt
|-- README.md
|-- .gitignore
`-- pom.xml
```

## Code Structure

- `Main.java` contains the console menu, input validation, and user interaction flow.
- `Book.java` models book data, availability, due dates, fines, and reservations.
- `Member.java` models library members and their borrowed book list.
- `Library.java` contains the core operations for books, members, borrowing, returning, searching, statistics, and CSV export.
- `FileHandler.java` handles text-file loading, saving, error handling, and CSV export.
- `data/books.txt` stores book records in pipe-delimited format.
- `data/members.txt` stores member records in pipe-delimited format.

## Technical Details

Books and members are stored in `ArrayList` collections. Search uses Java streams to filter books by ISBN, title, or author. Borrowing marks a book unavailable, stores the borrowing member ID, and sets a due date two weeks from the current date. Returning clears the borrowing fields and calculates a fine when the book is overdue.

The application persists data with text files instead of a database. Book records use this format:

```text
isbn|title|author|year|available|borrowedBy|dueDate|reservationMemberIds
```

Member records use this format:

```text
id|name|borrowedBookIsbns
```

File operations are wrapped in exception handling so missing or invalid files do not crash the program.

## Testing Evidence

- Started the app and confirmed sample books and members load from `data`.
- Viewed all books and checked formatted output.
- Added a new book and confirmed it was saved to `books.txt`.
- Tried adding a duplicate ISBN and confirmed validation prevents it.
- Registered a new member and confirmed it was saved to `members.txt`.
- Borrowed an available book and confirmed due date assignment.
- Tried borrowing an already borrowed book and confirmed the error message appears.
- Returned a borrowed book and confirmed availability is restored.
- Searched by title, author, and ISBN.
- Viewed statistics for total, available, borrowed, overdue, reserved, and fine values.
- Exported CSV files to the `data` folder.

## Visual Documentation

Recommended screenshots for submission:

- Main menu
- View all books output
- Search results
- Borrow book workflow with due date
- Return book workflow with fine output
- Library statistics

## Quality Standards Checklist

- [x] Project overview and objectives included
- [x] Setup instructions included
- [x] Code structure documented
- [x] Technical details explained
- [x] Testing evidence included
- [x] Java classes use encapsulation
- [x] Constructors, getters, and setters included
- [x] ArrayLists used for collections
- [x] File I/O persistence implemented
- [x] Console menu system implemented
- [x] Exception handling included
- [x] Input validation included
- [x] Search and filter functionality included
- [x] Overdue fine calculation included
- [x] CSV export included
- [x] Reservation system included
