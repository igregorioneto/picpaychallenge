package com.greg.picpaychallenge.service;

import com.greg.picpaychallenge.client.AuthorizationClient;
import com.greg.picpaychallenge.controller.dto.TransferDto;
import com.greg.picpaychallenge.exception.PicPayException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isAuthorized(TransferDto transfer) {

        var resp = authorizationClient.isAuthorized();

        if (resp.getStatusCode().isError()) {
            throw new PicPayException();
        }

        return resp.getBody().authorized();
    }

}
