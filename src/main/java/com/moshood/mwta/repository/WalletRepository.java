package com.moshood.mwta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.moshood.mwta.model.Wallet;

@NoRepositoryBean
public interface WalletRepository<T extends Wallet, ID> extends JpaRepository<T, ID> {

}
