package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 貸出履歴情報格納DTO
 *
 */
@Configuration
@Data
public class HistoryInfo {

    private int bookId;

    private String title;

    private String rentDate;

    private String returnDate;

    public HistoryInfo() {

    }

    public HistoryInfo(int bookId, String title, String rentDate, String returnDate) {
        this.bookId = bookId;
        this.title = title;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
    }

}
