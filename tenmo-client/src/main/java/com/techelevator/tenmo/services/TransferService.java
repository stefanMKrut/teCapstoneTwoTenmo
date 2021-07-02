package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


public class TransferService {

    public static String AUTH_TOKEN = "";
    private final String BASE_URL;
    public RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.BASE_URL = url + "/account/transfer";
    }

    public Transfer send(Transfer transfer) {
        Transfer transferSent = null;
        transferSent = restTemplate.postForObject(BASE_URL, makeTransferEntity(transfer), Transfer.class);
        return transferSent;
    }

    public Transfer[] listTransfers() {
        Transfer[] transfers = null;
        transfers = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        return transfers;
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

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

}
