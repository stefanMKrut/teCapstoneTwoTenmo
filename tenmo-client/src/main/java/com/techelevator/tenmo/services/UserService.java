package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class UserService {

    public static String AUTH_TOKEN = "";
    private final String BASE_URL;
    public RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        this.BASE_URL = url + "/users";
    }

    public User[] listAllUsers() {
        User[] users = null;
        users = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        return users;
    }

    public String getUsernameByAccountId(int accountId) {
        String username = null;
        username = restTemplate.exchange(BASE_URL + "/" + accountId, HttpMethod.GET, makeAuthEntity(), String.class).getBody();
        return username;
    }


    /**
     * Returns an {HttpEntity} with the `Authorization: Bearer:` header
     *
     * @return {HttpEntity}
     */
    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
