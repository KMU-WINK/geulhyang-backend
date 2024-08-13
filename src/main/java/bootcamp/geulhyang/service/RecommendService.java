package bootcamp.geulhyang.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    private static final Logger log = LoggerFactory.getLogger(RecommendService.class);
    private final String authKey;

    RecommendService(@Value("${library.auth-key}") String authKey) {
        this.authKey = authKey;
    }

    public List<Map<String, Object>> popularRecommend() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://data4library.kr/api/loanItemSrch";// 실제 발급받은 키로 교체하세요

        // URI 빌더를 사용해 필요한 쿼리 파라미터 추가
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("authKey", authKey)
                .queryParam("pageNo", "1")
                .queryParam("pageSize", "5")
                .queryParam("format", "json");

        Map<String, Object> response = restTemplate.getForObject(uriBuilder.toUriString(), Map.class);

        // 받은 데이터를 파싱하여 원하는 형식으로 변환
        if (Objects.nonNull(response) && response.containsKey("response")) {
            Map<String, Object> responseData = (Map<String, Object>) response.get("response");

            if (responseData.containsKey("docs")) {
                List<Map<String, Object>> docs = (List<Map<String, Object>>) responseData.get("docs");

                return docs.stream().map(doc -> {
                    Map<String, Object> bookInfo = (Map<String, Object>) doc.get("doc");

                    return Map.of(
                            "isbn", bookInfo.get("isbn13"),
                            "rank", doc.get("ranking"),
                            "name", bookInfo.get("bookname"),
                            "author", bookInfo.get("authors"),
                            "publisher", bookInfo.get("publisher"),
                            "publishedAt", bookInfo.get("publication_year"),
                            "coverImage", bookInfo.get("bookImageURL")
                    );
                }).collect(Collectors.toList());
            }
        }

        // 만약 결과가 없다면 빈 리스트 반환
        return List.of();
    }
}
