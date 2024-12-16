package com.greg.picpaychallenge.config;

import com.greg.picpaychallenge.entity.WalletType;
import com.greg.picpaychallenge.repository.WalletTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final WalletTypeRepository walletTypeRepository;

    public DataLoader(WalletTypeRepository walletTypeRepository) {
        this.walletTypeRepository = walletTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Arrays.stream(WalletType.Enum.values())
               // .forEach(walletType -> walletTypeRepository.save(walletType.get()));
    }
}
