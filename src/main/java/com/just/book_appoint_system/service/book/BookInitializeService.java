package com.just.book_appoint_system.service.book;

import com.just.book_appoint_system.domain.BookView;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface BookInitializeService {
    List<BookView>getHotBookList();
    List<BookView>getLatestBookList();
    List<BookView>getSlideShowBookList();
    List<BookView>getRecentBookList();
}
