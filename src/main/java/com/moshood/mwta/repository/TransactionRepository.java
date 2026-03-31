package com.moshood.mwta.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moshood.mwta.enums.TransactionStatus;
import com.moshood.mwta.enums.TransactionType;
import com.moshood.mwta.model.Transaction;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

	Optional<Transaction>  findByIdempotencyKey(String idempotencyKey);
	
	Optional<Transaction> findByTransactionReference(String transactionReference);
	
	List<Transaction> findAllByTransactionReference(String transactionReference);
	
	Page<Transaction> findByTransactionStatus(TransactionStatus status, Pageable pageable);
	
	Page<Transaction> findByTransactionType(TransactionType transactionType, Pageable pageable);
	
	Page<Transaction> findByTransactionTypeAndTransactionStatus(TransactionType type, TransactionStatus status, Pageable pageable);
	
}
