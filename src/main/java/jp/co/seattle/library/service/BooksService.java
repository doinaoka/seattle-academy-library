package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id,title,author,publisher,thumbnail_url,publish_date,explanation,isbn from books order by title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }
    
    /**
     * 最新の情報を取得
     * @return 最新書籍情報
     */
    public BookDetailsInfo getLatestBookInfo() {
    	
    	String sql = "select * from books where id=(select max(id) from books);";
    	
    	BookDetailsInfo latestBookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
		return latestBookDetailsInfo;
    	
    }
    

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */public void registBook(BookDetailsInfo bookInfo) {

         String sql = "INSERT INTO books (title, author,publisher,publish_date,explanation,isbn,thumbnail_name,thumbnail_url,reg_date,upd_date) VALUES ('"
                 + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','" 
                 + bookInfo.getPublishDate() + "','"
                 + bookInfo.getExplanation() + "','"
                 + bookInfo.getIsbn() + "','"
                 + bookInfo.getThumbnailName() + "','"
                 + bookInfo.getThumbnailUrl() + "',"
                 + "now(),"
                 + "now())";

         jdbcTemplate.update(sql);
     }
     
    public void deleteBook(Integer bookId) {

        String sql = "delete from books where id= " + bookId +";";

        jdbcTemplate.update(sql);
    }
    
    
    
    
}
