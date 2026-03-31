package com.moshood.mwta.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.moshood.mwta.dto.TransactionDto;
import com.moshood.mwta.enums.TransactionStatus;
import com.moshood.mwta.enums.TransactionType;
import com.moshood.mwta.model.Transaction;
import com.moshood.mwta.model.Wallet;
import com.moshood.mwta.repository.NormalWalletRepository;
import com.moshood.mwta.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final NormalWalletRepository walletRepository;
	
	/**
	 * 
	 * @param transactionDto
	 * @param idempotencyKey
	 * @param wallet
	 * @return
	 * Works for booth Funding and receiving Transactions
	 */
	public int createTransaction(TransactionDto transactionDto,String idempotencyKey, Wallet wallet, TransactionType transactionType) {
		
		try {
			Transaction transaction = new Transaction();
			transaction.setAmount(transactionDto.getAmount());
			transaction.setIdempotencyKey(idempotencyKey);
			String reference = UUID.randomUUID().toString();
			transaction.setTransactionReference(reference);
			transaction.setWallet(wallet);
			transaction.setTransactionType(transactionType);
			transaction.setSenderId(transactionDto.getSenderId());
			transaction.setReceiverId(transactionDto.getReceiverId());
			transaction.setTransactionStatus(transactionDto.getStatus());
			transaction.setTransactionStatus(TransactionStatus.SUCCESS);
			transactionRepository.save(transaction);
		} catch (DataIntegrityViolationException e) {
			return 0;
		}
		return 1;
	}
	
	
	public Page<TransactionDto> getTransactions(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
		
		return transactionPage.map((transaction)->{
			return transaction.toDto();
		});
	}
	
	public Page<TransactionDto> getTransactionByStatus(TransactionStatus status, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		
		return transactionRepository.findByTransactionStatus(status, pageable).map(transaction -> {
			return transaction.toDto();
		});
	}
	
	public Page<TransactionDto> getTransactionByType(TransactionType type, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return transactionRepository.findByTransactionType(type, pageable).map(transaction -> {
			return transaction.toDto();
		});
	}
	
	public Page<TransactionDto> getTransactionByTypeAndStatus(TransactionType type, TransactionStatus status, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return transactionRepository.findByTransactionTypeAndTransactionStatus(type, status, pageable).map(transaction -> {
			return transaction.toDto();
		});
	}
}
