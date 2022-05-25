package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.HistoryInfo;

@Configuration
public class HistoryInfoRowMapper implements RowMapper<HistoryInfo> {

    @Override
    public HistoryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        HistoryInfo historyInfo = new HistoryInfo();

        historyInfo.setBookId(rs.getInt("book_id"));
        historyInfo.setTitle(rs.getString("title"));
        historyInfo.setRentDate(rs.getString("rent_date"));
        historyInfo.setReturnDate(rs.getString("return_date"));
        return historyInfo;
    }

}