package com.greg.picpaychallenge.service;

import com.greg.picpaychallenge.controller.dto.TransferDto;
import com.greg.picpaychallenge.entity.Transfer;
import com.greg.picpaychallenge.entity.Wallet;
import com.greg.picpaychallenge.exception.InsufficientBalanceException;
import com.greg.picpaychallenge.exception.TransferNotAllowedForWalletType;
import com.greg.picpaychallenge.exception.TransferNotAuthorizedException;
import com.greg.picpaychallenge.exception.WalletNotFoundException;
import com.greg.picpaychallenge.repository.TransferRepository;
import com.greg.picpaychallenge.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    public TransferService(
            TransferRepository transferRepository,
            WalletRepository walletRepository,
            AuthorizationService authorizationService,
            NotificationService notificationService
    ) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transfer transfer(TransferDto transferDto) {

        var sender = walletRepository.findById(transferDto.payer())
                .orElseThrow(() -> new WalletNotFoundException(transferDto.payer()));

        var receiver = walletRepository.findById(transferDto.payee())
                .orElseThrow(() -> new WalletNotFoundException(transferDto.payee()));

        validateTransfer(transferDto, sender);

        sender.debit(transferDto.value());
        receiver.credit(transferDto.value());

        var transfer = new Transfer(sender, receiver, transferDto.value());

        walletRepository.save(sender);
        walletRepository.save(receiver);
        var transferResult = transferRepository.save(transfer);

        CompletableFuture.runAsync(() -> notificationService.sendNotification(transferResult));

        return transferResult;
    }

    private void validateTransfer(TransferDto transferDto, Wallet sender) {

        if (!sender.isTransferAllowedForWalletType()) {
            throw new TransferNotAllowedForWalletType();
        }

        if (!sender.isBalanceEqualOrGreatherThan(transferDto.value())) {
            throw new InsufficientBalanceException();
        }

        if (!authorizationService.isAuthorized(transferDto)) {
            throw new TransferNotAuthorizedException();
        }

    }
}
