package com.just.book_appoint_system.service.borrow;

import com.just.book_appoint_system.domain.Book;
import com.just.book_appoint_system.domain.BookView;
import com.just.book_appoint_system.domain.BorrowView;
import com.just.book_appoint_system.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface BorrowService {
    BorrowView reserveBook(BookView bookView, UserDto userDto);
    List<BorrowView>getBorrowViewList(BorrowView borrowView);
    boolean updateBorrowHistory(BorrowView borrowView);
}
