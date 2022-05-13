package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RentBooksService {
	final static Logger logger = LoggerFactory.getLogger(RentBooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍を借りる
	 *
	 * @param bookId 書籍ID
	 */
	 
	public void rentBook(Integer bookId) {

		String sql = "INSERT INTO rentBooks(book_id) SELECT " + bookId +" WHERE NOT EXISTS ( SELECT * FROM rentBooks WHERE book_id = " + bookId + ")";

		jdbcTemplate.update(sql);
		
	}
	
<<<<<<< HEAD
=======
	/**
	 * 書籍を返却する
	 * @param bookId　書籍ID
	 */
>>>>>>> feature/task_11

	
	public void returnBook(Integer bookId) {

		String sql = "delete FROM rentBooks where book_id = " + bookId;

		jdbcTemplate.update(sql);
		
	}

	/**
	 * 借りた書籍を数える
	 * @param bookId 書籍ID
	 * @return 遷移先
	 */

	
	public Integer countRentBook(Integer bookId) {
        
    	String sql = "select count (book_id) from rentBooks where book_id = "+ bookId ;
    	
		return jdbcTemplate.queryForObject(sql,Integer.class);

    }


}
