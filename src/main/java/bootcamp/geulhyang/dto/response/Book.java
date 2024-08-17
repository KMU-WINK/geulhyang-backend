package bootcamp.geulhyang.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Book {
    private String isbn;
    private int rank;
    private String name;
    private String author;
    private String publisher;
    private String publishedAt;
    private String coverImage;
    private double rating;
}
