package bootcamp.geulhyang.controller;

import bootcamp.geulhyang.dto.response.Book;
import bootcamp.geulhyang.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
@Slf4j
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularBooks() throws Exception {
        List<Book> popularBooks = recommendService.popularRecommend();
        Map<String, Object> response = Map.of("books", popularBooks);

        return ResponseEntity.ok(response);
    }
}
