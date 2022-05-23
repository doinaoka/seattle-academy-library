package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.HistoryInfo;
import jp.co.seattle.library.rowMapper.HistoryInfoRowMapper;

@Service
public class RentBooksService {
	final static Logger logger = LoggerFactory.getLogger(RentBooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍を借りる
	 * @param bookId 書籍ID
	 * @param title　タイトル
	 */
	 
	public void rentBook(Integer bookId, String title) {

		//String sql = "INSERT INTO rentBooks(book_id) SELECT " + bookId +" WHERE NOT EXISTS ( SELECT * FROM rentBooks WHERE book_id = " + bookId + ")";
		String sql = "INSERT INTO rentBooks(book_id,rent_date,title) SELECT "
				+ bookId + ",current_date, '"
				+ title + "' WHERE NOT EXISTS ( SELECT * FROM rentBooks WHERE book_id = "
				+ bookId +")";
		
		jdbcTemplate.update(sql);
		
	}
	
	/**
	 * 書籍を再度借りる
	 * @param bookId
	 */
	public void rentAgainBook(Integer bookId) {
		
		String sql = "update rentbooks set rent_date = current_date, return_date = null "
				+ " where book_id ="+bookId;
		
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を返却する
	 * @param bookId 書籍ID
	 */
	public void returnBook(Integer bookId) {

		//String sql = "delete FROM rentBooks where book_id = " + bookId;
		String sql ="update rentbooks set rent_date = null, return_date  = current_date where book_id =" +bookId;

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
	
	
	/**
	 * 貸出書籍リスト
	 * @return　遷移先
	 */
	public List<HistoryInfo> historyList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<HistoryInfo> historyList = jdbcTemplate.query(
				"select book_id ,books.title,rent_date ,return_date from rentbooks left outer join books on books.id = rentbooks.book_id ;",
				new HistoryInfoRowMapper());

		return historyList;
		
	}
	
	/**
	 * 貸出書籍情報を取得する
	 * @param bookId
	 * @return　貸出書籍情報
	 */
	public HistoryInfo selectHistoryInfo(Integer bookId) {
		
		String sql = "select book_id ,books.title,rent_date ,return_date from rentbooks left outer join books on books.id = rentbooks.book_id "
				+ " where books.id =262;" +bookId;
		
		try {
			HistoryInfo selectedHistoryInfo = jdbcTemplate.queryForObject(sql, new HistoryInfoRowMapper());
			return selectedHistoryInfo;
        }catch (Exception e ) {
        	return null ;
        }
		
	}


}
