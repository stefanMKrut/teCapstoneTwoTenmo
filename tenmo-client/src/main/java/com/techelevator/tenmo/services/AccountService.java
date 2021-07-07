package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    public static String AUTH_TOKEN = "";
    private final String BASE_URL;
    public RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.BASE_URL = url + "/account";
    }

    public Account getAccount(){
        Account account = null;
        account = restTemplate.exchange(BASE_URL + "", HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
        if (account != null) {
            return account;
        } else {
            return null;
        }
    }

    public long getAccountId(int userId){
        Account account = null;
        account = restTemplate.exchange(BASE_URL + "/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
        return account.getAccountId();
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
