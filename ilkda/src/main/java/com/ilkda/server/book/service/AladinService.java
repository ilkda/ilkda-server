package com.ilkda.server.book.service;

import com.ilkda.server.book.model.Book;
import com.ilkda.server.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AladinService {

    @Value("${aladin.key}")
    private String key;

    private final BookRepository bookRepository;

    @Transactional
    public void storeBooks() {
        try {
            JSONArray jsonArray = getSearchBookList();
            List<Book> bookList = getBooksDetail(jsonArray);
            bookRepository.saveAll(bookList);
        } catch (ParseException p) {
            throw new RuntimeException("알라딘 데이터 파싱 실패");
        }
    }

    private JSONArray getSearchBookList() throws ParseException {
        String itemSearchUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
        Map<String, String> itemSearchParameters = new HashMap<>();
        itemSearchParameters.put("ttbkey", key);
        itemSearchParameters.put("Query", "java");
        itemSearchParameters.put("output", "js");

        String itemSearchResponse = sendHttpRequest(
                itemSearchUrl,
                new HttpHeaders(),
                itemSearchParameters,
                HttpMethod.GET
        ).replaceAll(";", "");

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(itemSearchResponse);
        return (JSONArray) jsonObject.get("item");
    }

    private List<Book> getBooksDetail(JSONArray jsonArray) throws ParseException {
        String itemLookUpUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
        Map<String, String> itemLookUpParameters = new HashMap<>();
        itemLookUpParameters.put("ttbkey", key);
        itemLookUpParameters.put("cover", "Big");
        itemLookUpParameters.put("output", "js");
        itemLookUpParameters.put("itemIdType", "ISBN13");

        JSONParser jsonParser = new JSONParser();
        List<Book> bookList = new ArrayList<>();
        for (Object o : jsonArray) {
            itemLookUpParameters.put("itemId", (String) ((JSONObject) o).get("isbn13"));
            String res = sendHttpRequest(
                    itemLookUpUrl,
                    new HttpHeaders(),
                    itemLookUpParameters,
                    HttpMethod.GET
            ).replaceAll(";", "");

            Book book = createBookFromJson((JSONObject) ((JSONArray) ((JSONObject) jsonParser.parse(res)).get("item")).get(0));
            bookList.add(book);
        }
        return bookList;
    }

    private String sendHttpRequest(String uri,
                                   HttpHeaders httpHeaders,
                                   Map<String, String> parameters,
                                   HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();

        if(method.equals(HttpMethod.GET)) uri = generateUri(uri, parameters);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                method,
                new HttpEntity<>(httpHeaders),
                String.class
        );

        return response.getBody();
    }

    private String generateUri(String baseUrl, Map<String, String> parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(baseUrl);
        for(String name : parameters.keySet())
            builder.queryParam(name, parameters.get(name));

        return builder.toUriString();
    }

    private Book createBookFromJson(JSONObject jsonObject) {
        return Book.builder()
                .title((String) jsonObject.get("title"))
                .isbn13((String) jsonObject.get("isbn13"))
                .author((String) jsonObject.get("author"))
                .cover((String) jsonObject.get("cover"))
                .page((Long) ((JSONObject) jsonObject.get("bookinfo")).get("itemPage"))
                .build();
    }
}
