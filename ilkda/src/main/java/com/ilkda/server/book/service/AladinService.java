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

import java.util.HashMap;
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
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(itemSearchResponse);
            JSONArray jsonArray = (JSONArray) jsonObject.get("item");
            String itemLookUpUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
            Map<String, String> itemLookUpParameters = new HashMap<>();
            itemLookUpParameters.put("ttbkey", key);
            itemLookUpParameters.put("cover", "Big");
            itemLookUpParameters.put("output", "js");
            itemLookUpParameters.put("itemIdType", "ISBN13");
            for (Object o : jsonArray) {
                itemLookUpParameters.put("itemId", (String) ((JSONObject) o).get("isbn13"));
                String res = sendHttpRequest(
                        itemLookUpUrl,
                        new HttpHeaders(),
                        itemLookUpParameters,
                        HttpMethod.GET
                ).replaceAll(";", "");
                JSONObject bookJsonObject = (JSONObject) ((JSONArray) ((JSONObject) jsonParser.parse(res)).get("item")).get(0);
                Book book = Book.builder()
                        .title((String) bookJsonObject.get("title"))
                        .isbn13((String) bookJsonObject.get("isbn13"))
                        .author((String) bookJsonObject.get("author"))
                        .cover((String) bookJsonObject.get("cover"))
                        .page((Long) ((JSONObject) bookJsonObject.get("bookinfo")).get("itemPage"))
                        .build();
                bookRepository.save(book);
            }
        } catch (ParseException p) {
            p.printStackTrace();
            throw new RuntimeException("알라딘 데이터 파싱 실패");
        }
    }

    private String sendHttpRequest(String url,
                                  HttpHeaders httpHeaders,
                                  Map<String, String> parameters,
                                  HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();

        if(method.equals(HttpMethod.GET) && parameters.size() >= 1) {
            StringBuilder urlBuilder = new StringBuilder(url);
            String[] parameterKeys = parameters.keySet().toArray(String[]::new);
            urlBuilder.append("?").append(parameterKeys[0]).append("=").append(parameters.get(parameterKeys[0]));
            for(int i = 1; i < parameterKeys.length; i++)
                urlBuilder.append("&").append(parameterKeys[i]).append("=").append(parameters.get(parameterKeys[i]));
            url = urlBuilder.toString();
        }

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                method,
                new HttpEntity<>(httpHeaders),
                String.class
        );

        return response.getBody();
    }
}
