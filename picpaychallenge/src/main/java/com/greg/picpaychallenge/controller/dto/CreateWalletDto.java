package com.greg.picpaychallenge.controller.dto;

import com.greg.picpaychallenge.entity.Wallet;
import com.greg.picpaychallenge.entity.WalletType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateWalletDto(
        @NotBlank String fullName,
        @NotBlank String cpfCnpj,
        @NotBlank String email,
        @NotBlank String password,
        @NotNull WalletType.Enum walletType
        ) {

    public Wallet toWallet() {
        return new Wallet(
          fullName,
                cpfCnpj,
                email,
                password,
                walletType.get()
        );
    }

}
