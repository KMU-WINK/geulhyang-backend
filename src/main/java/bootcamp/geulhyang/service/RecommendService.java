package bootcamp.geulhyang.service;

import bootcamp.geulhyang.dto.response.Book;
import bootcamp.geulhyang.util.OpenApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecommendService {

    private final String url;
    private final String authKey;

    RecommendService(@Value("${library.auth-key}") String authKey,
                     @Value("${library.url}") String url)
    {
        this.url = url;
        this.authKey = authKey;
    }

    public List<Book> popularRecommend() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("hi~~~~~~~~~~~~~~~~~~~`");

        // URI 빌더를 사용해 필요한 쿼리 파라미터 추가
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("authKey", authKey)
                .queryParam("pageNo", "1")
                .queryParam("pageSize", "5")
                .queryParam("format", "json")
                .toUriString();


        OpenApi openApi = new OpenApi();
        String json = openApi.getOpenApi(requestUrl);
        log.info(json);
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(json);
        JSONObject resopnse = (JSONObject) obj.get("response");
        JSONArray docs = (JSONArray) resopnse.get("docs");

        List<Book> popularBooks = new ArrayList<Book>();
        Book book;

        for(int i=0; i<docs.size(); i++){
            book = new Book();

            JSONObject doc = (JSONObject) docs.get(i);
            JSONObject docData = (JSONObject) doc.get("doc");
            book.setIsbn(""+docData.get("isbn13"));
            book.setRank(Integer.parseInt(""+docData.get("ranking")));
            book.setName(""+docData.get("bookname"));
            book.setAuthor(""+docData.get("authors"));
            book.setPublisher(""+docData.get("publisher"));
            book.setPublishedAt(""+docData.get("publication_year "));
            book.setCoverImage(""+docData.get("bookImageURL"));

            popularBooks.add(book);
        }

        return popularBooks;
    }
}
