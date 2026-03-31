package com.moshood.mwta.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moshood.mwta.model.NormalWallet;
import com.moshood.mwta.model.Wallet;

import jakarta.transaction.Transactional;

@Repository
public interface NormalWalletRepository extends WalletRepository<NormalWallet, String> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(" UPDATE NormalWallet nw set nw.balance = :newBalance where nw.user.userId = :id")
	public void updateBalance(@Param("id")String id,@Param("newBalance") BigDecimal newBalance);
	
	public Optional<Wallet> findWalletByUserId(String userId);
}
