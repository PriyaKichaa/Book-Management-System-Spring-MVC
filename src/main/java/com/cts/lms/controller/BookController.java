package com.cts.lms.controller;

import com.cts.lms.entity.Book;
import com.cts.lms.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/view-books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/view-book/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PostMapping("/add-book")
    public ResponseEntity<?> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
    }

    @PatchMapping("/modify-book/{bookId}")
    public ResponseEntity<?> modifyBook(@PathVariable Long bookId, @RequestBody Book bookDetails) {
        return ResponseEntity.ok(bookService.modifyBook(bookId, bookDetails));
    }

    @DeleteMapping("/delete-book/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book ID " + bookId + " deleted successfully.");
    }

    @GetMapping("/search-author/{author}")
    public ResponseEntity<?> searchBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
    }

    @GetMapping("/search-title/{title}")
    public ResponseEntity<?> searchBooksByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    @GetMapping("/search-genre/{genre}")
    public ResponseEntity<?> searchBooksByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(bookService.searchBooksByGenre(genre));
    }
}
