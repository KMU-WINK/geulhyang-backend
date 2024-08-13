package bootcamp.geulhyang.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Book {
    @Id
    private String isbn;

    private String name;
    private String author;
    private String publisher;
    private String publishedAt;

    private String coverImage;

    private int rating;
}
