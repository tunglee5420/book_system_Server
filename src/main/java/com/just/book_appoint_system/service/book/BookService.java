package com.just.book_appoint_system.service.book;

import com.just.book_appoint_system.domain.BookView;
import com.just.book_appoint_system.dto.BookViewReceiveDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface BookService {
    BookView getBookInfoByIsbn(String isbn);
    List<BookView> getBookList(BookView bookView);
    boolean updateBookInfo(BookView bookView);

    boolean updateBookAmount(BookView bookView,BookView bookView1);
}
