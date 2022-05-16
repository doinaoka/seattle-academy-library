package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

@Controller
public class SearchBooksController {
	final static Logger logger = LoggerFactory.getLogger(EditController.class);
	
	@Autowired
    private BooksService booksService;
	
	

	@Transactional
    @RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBook(Locale locale,
			@RequestParam("search") String search,
			Model model) {
	logger.info("Welcome updateBook.java! The client locale is {}.", locale);
	
	
	model.addAttribute("bookList", booksService.searchBookList(search));
    return "home";
	
	}
}
