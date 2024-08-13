package bootcamp.geulhyang.repository;

import bootcamp.geulhyang.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
