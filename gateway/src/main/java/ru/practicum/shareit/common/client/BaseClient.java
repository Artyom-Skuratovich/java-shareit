package ru.practicum.shareit.common.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

public abstract class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + getApiPrefix()))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    protected abstract String getApiPrefix();

    protected ResponseEntity<Object> get(String url, Long userId, Map<String, Object> params) {
        return request(HttpMethod.GET, url, userId, params, null);
    }

    protected <T> ResponseEntity<Object> post(String url, Long userId, T body) {
        return request(HttpMethod.POST, url, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String url, Long userId, Map<String, Object> params, T body) {
        return request(HttpMethod.PATCH, url, userId, params, body);
    }

    protected ResponseEntity<Object> delete(String url) {
        return request(HttpMethod.DELETE, url, null, null, null);
    }

    private ResponseEntity<Object> request(HttpMethod method, String url, Long userId, Map<String, Object> params, Object body) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", userId.toString());
        }

        final HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        try {
            final ResponseEntity<Object> response = rest.exchange(url, method, entity, Object.class, params != null ?
                    params :
                    Map.of());
            return response.getStatusCode().is2xxSuccessful() ?
                    response :
                    ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }
}