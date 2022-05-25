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
import jp.co.seattle.library.service.RentBooksService;

@Controller
public class HistoryController {	
	final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);
	
	@Autowired
    private RentBooksService rentBooksService;
	@Autowired
    private BooksService booksService;
	
	@Transactional
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String history(Model model) {
		
		
		model.addAttribute("historyList",rentBooksService.historyList());
		return "history";
	}
	
	
	@Transactional
    @RequestMapping(value = "/historyDetails", method = RequestMethod.GET)
    public String historyDetails(Locale locale,
            @RequestParam("bookId") int bookId,
            Model model) {
		
		
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}

}
