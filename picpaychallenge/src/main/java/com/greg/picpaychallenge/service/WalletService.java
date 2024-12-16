package com.greg.picpaychallenge.service;

import com.greg.picpaychallenge.controller.dto.CreateWalletDto;
import com.greg.picpaychallenge.entity.Wallet;
import com.greg.picpaychallenge.exception.WalletDataAlreadyExistsException;
import com.greg.picpaychallenge.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfCnpjOrEmail(dto.cpfCnpj(), dto.email());
        if (walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCnpj or Email already exists");
        }

        return walletRepository.save(dto.toWallet());
    }

}
