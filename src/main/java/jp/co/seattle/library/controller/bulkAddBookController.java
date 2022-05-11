package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;




@Controller //APIの入り口
public class bulkAddBookController {
	
	final static Logger logger = LoggerFactory.getLogger(bulkAddBookController.class);
    
    @Autowired
    private BooksService booksService;

	private Object list;
    
	/**
	 * 書籍情報一括登録画面に推移する
	 * @param model モデル
	 * @return　遷移先画面
	 */
	
	@RequestMapping(value = "/bulkAddBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "bulkAddBook";
	}
	

	
	/**
	 * 書籍情報を一括登録する
	 * @param locale ロケール情報
	 * @param uploadFile　CSVファイル
	 * @param model　モデル
	 * @return　遷移先画面
	 */
    @Transactional
    @RequestMapping(value = "/bulkRegistBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
     
   
    public String bulkRegistBook(Locale locale,
            @RequestParam("upload_file")MultipartFile uploadFile,
            Model model) {
        logger.info("Welcome bulkRegistBook.java! The client locale is {}.", locale);

        
        List<BookDetailsInfo> bookLists = new ArrayList<BookDetailsInfo>(); 
        List<String> errorLists = new ArrayList<String>(); 
        
        String line;
        //読み込み行数の管理
        int count = 1;
        
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {
        	line = br.readLine();       
            //1行ずつ読み込みを行う
            while (!StringUtils.isEmpty(line)) {
            	String[] split = line.split(",", -1);
            	               	
                boolean requiredCheck = split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty() || split[3].isEmpty();
                boolean publishDateCheck = ! (split[3].length() == 8 && split[3].matches("^[0-9]+$"));
                boolean isbnCheck = !(split[4].length() == 10 || split[4].length() == 13 || split[4].length() == 0);
   
                    
                if (requiredCheck || publishDateCheck || isbnCheck) {
                	errorLists.add(count + "行目の書籍登録でエラーが起きました。");
                	
                }else {
                	BookDetailsInfo bookInfo = new BookDetailsInfo();
                	bookInfo.setTitle(split[0]);
                	bookInfo.setAuthor(split[1]);
                	bookInfo.setPublisher(split[2]);
                	bookInfo.setPublishDate(split[3]);
                	bookInfo.setIsbn(split[4]);
                	bookInfo.setExplanation(split[5]);
                	
                	bookLists.add(bookInfo);
                }
                    
                //行数
                count ++; 
                line = br.readLine();
                       	
            }
            
            if(bookLists.size() == 0) {
            	model.addAttribute("errorMessage", "CSVに書籍情報がありません。");
            	return "bulkAddBook";  
            }
        	
        } catch (IOException e) {
        	logger.error("CSVファイル読み込みでエラー発生", e);
        	model.addAttribute("errorMessage", "CSVファイルを選択してください。");
        	return "bulkAddBook";  
        }
          
       
        
        if (errorLists.size() != 0) {
        	//バリデーションチェックのエラーを表示する
        	model.addAttribute("errorListMessages",errorLists);
        	return "bulkAddBook";
        	
        }else {
            	
            for(BookDetailsInfo bookInfo: bookLists) {
            	// 書籍情報を新規登録する
                booksService.registBook(bookInfo);	
         
            }
            
            
        	// 書籍一覧画面を表示する  
        	model.addAttribute("bookList", booksService.getBookList());
        	//  詳細画面に遷移する
        	return "home";  	
        	
        }
        	
        	
        }
        
    
    
    
    
	
}
