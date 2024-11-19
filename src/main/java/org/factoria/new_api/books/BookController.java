package org.factoria.new_api.books;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @GetMapping
    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);

        if (optionalBook.isPresent()) {
            return new ResponseEntity<>(optionalBook.get(), HttpStatus.OK); // 200 OK
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
    }

    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            return new ResponseEntity<>("El ISBN ya existe", HttpStatus.BAD_REQUEST);
        }

        bookRepository.save(book);
        return new ResponseEntity<>("Libro creado con éxito", HttpStatus.CREATED);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBookByIsbn(@PathVariable String isbn) {
        if (bookRepository.findByIsbn(isbn).isEmpty()) {
            return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
        }

        bookRepository.deleteByIsbn(isbn);
        return new ResponseEntity<>("Libro eliminado con éxito", HttpStatus.OK);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<String> updateBook(@PathVariable String isbn, @RequestBody Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);

        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();

            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setIsbn(updatedBook.getIsbn());

            bookRepository.save(existingBook);

            return new ResponseEntity<>("Libro actualizado", HttpStatus.OK);
        }

        return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
    }

}
