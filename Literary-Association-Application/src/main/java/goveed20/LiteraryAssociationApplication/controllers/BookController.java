package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/download/{bookTitle}")
    public ResponseEntity<String> downloadBook(@PathVariable String bookTitle) {
        try {
            return new ResponseEntity<>(bookService.downloadBook(bookTitle), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
