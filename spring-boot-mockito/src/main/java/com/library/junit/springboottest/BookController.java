package com.library.junit.springboottest;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	BookRepository bookRepository;

	@GetMapping
	public List<Book> getAllBookRecords() {
		return bookRepository.findAll();
	}

	@GetMapping(value = "{bookId}")
	public Book getBookById(@PathVariable(value = "bookId") Long bookId) {
		return bookRepository.findById(bookId).get();
	}

	@PostMapping
	public Book createBookRecord(@RequestBody @Valid Book bookRecord) {
		return bookRepository.save(bookRecord);
	}

	@PutMapping
	public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException {
		if (bookRecord == null || bookRecord.getBookId() == null) {
			throw new NotFoundException("BookRecord or ID cannot be null");
		}
		Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());

		if (!optionalBook.isPresent()) {
			throw new NotFoundException("Book with Id: " + bookRecord.getBookId() + " not found.");
		}

		Book exisitingBookRec = optionalBook.get();
		exisitingBookRec.setName(bookRecord.getName());
		exisitingBookRec.setSummary(bookRecord.getSummary());
		exisitingBookRec.setRating(bookRecord.getRating());

		return bookRepository.save(exisitingBookRec);
	}

	@DeleteMapping(value = "{bookId}")
	public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException {
		if (!bookRepository.findById(bookId).isPresent()) {
			throw new NotFoundException("Book Id " + bookId + " not present");
		}

		bookRepository.deleteById(bookId);
	}
}
