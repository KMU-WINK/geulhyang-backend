package bootcamp.geulhyang.controller;

import bootcamp.geulhyang.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/recommend")
@Slf4j
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) { this.recommendService = recommendService; }

    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularBooks() {
        List<Map<String, Object>> books = recommendService.popularRecommend();
        Map<String, Object> response = Map.of("data", books);

        return ResponseEntity.ok(response);
    }
}
