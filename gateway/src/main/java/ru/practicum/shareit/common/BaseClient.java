package ru.practicum.shareit.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    protected ResponseEntity<Object> get(String url) {
        return get(url, null, null);
    }

    protected ResponseEntity<Object> get(String url, long userId) {
        return get(url, userId, null);
    }

    protected ResponseEntity<Object> get(
            String url,
            Long userId,
            Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, url, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String url, T body) {
        return post(url, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(
            String url,
            long userId,
            T body) {
        return post(url, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(
            String url,
            Long userId,
            Map<String, Object> parameters,
            T body) {
        return makeAndSendRequest(HttpMethod.POST, url, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String url, T body) {
        return patch(url, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(
            String url,
            long userId,
            T body) {
        return patch(url, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(
            String url,
            Long userId,
            Map<String, Object> parameters,
            T body) {
        return makeAndSendRequest(HttpMethod.PATCH, url, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String url) {
        return delete(url, null, null);
    }

    protected ResponseEntity<Object> delete(String url, long userId) {
        return delete(url, userId, null);
    }

    protected ResponseEntity<Object> delete(
            String url,
            Long userId,
            Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, url, userId, parameters, null);
    }

    private static HttpHeaders configureHeaders(Long userId) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", userId.toString());
        }
        return headers;
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(
            HttpMethod method,
            String url,
            Long userId,
            Map<String, Object> parameters,
            T body) {
        final HttpEntity<T> httpEntity = new HttpEntity<>(body, configureHeaders(userId));
        final ResponseEntity<Object> response = parameters != null ?
                rest.exchange(url, method, httpEntity, Object.class, parameters) :
                rest.exchange(url, method, httpEntity, Object.class);
        return prepareGatewayResponse(response);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        final ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        return response.hasBody() ?
                responseBuilder.body(response.getBody()) :
                responseBuilder.build();
    }
}