package jp.co.seattle.library.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;



@Controller
public class EditController {
	final static Logger logger = LoggerFactory.getLogger(EditController.class);
	
	@Autowired
    private BooksService booksService;
	
	@Autowired
    private ThumbnailService thumbnailService;

	
	/**
     * 書籍情報を編集する
     * @param locale
     * @param bookId
     * @param model
     * @return edit画面に遷移
     */
	
	
	@Transactional
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(
            Locale locale,
            @RequestParam("bookId") int bookId,
            Model model) {
        logger.info("Welcome edit! The client locale is {}.", locale);
        
        model.addAttribute("bookEditInfo", booksService.getBookInfo(bookId));
        return "edit";
	}

	
	/**
     * 書籍情報を更新する
     * @param locale
     * @param bookId
     * @param title
     * @param author
     * @param publisher
     * @param publish_date
     * @param explanation
     * @param isbn
     * @param file
     * @param model
     * @return 遷移先画面
     */
	
	@Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateBook(Locale locale,
    		@RequestParam("bookId") int bookId,
    		@RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("publish_date") String publishDate,
            @RequestParam("isbn") String isbn,
            @RequestParam("explanation") String explanation,
            Model model) {
        logger.info("Welcome updateBook.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setExplanation(explanation);
        bookInfo.setIsbn(isbn);


        

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "edit";
            }
        }   

        List<String> errorLists = new ArrayList<String>(); 
        
        boolean requiredCheck = title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty();
        boolean publishDateCheck = ! (publishDate.length() == 8 && publishDate.matches("^[0-9]+$"));
        boolean isbnCheck = !(isbn.length() == 10 || isbn.length() == 13 || isbn.length() == 0);
       
        //必須項目
        if(requiredCheck) {
        	
        	errorLists.add("必須項目を入力してください");    	
        }
        
        //出版日
        if(publishDateCheck) {
        	
        	errorLists.add("出版日は半角数字のYYYYMMDD形式で入力してください");       		
        	    	
        }
        
        //ISBN
        if (isbnCheck) {
        	
    		errorLists.add("ISBNの桁数または半角数字が正しくありません");       		
    	  		
    	}       
        
        if (requiredCheck || publishDateCheck || isbnCheck) {
        	model.addAttribute("errorListMessages",errorLists);
        	model.addAttribute("bookEditInfo",bookInfo);
        	return "edit";
        }
        
        
        // 書籍情報を新規登録する
        booksService.editBook(bookInfo);
        
		// TODO 登録した書籍の詳細情報を表示するように実装  
        
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookInfo.getBookId()));
		
		//  詳細画面に遷移する
		return "details";

	}
}