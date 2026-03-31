package com.moshood.mwta.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moshood.mwta.enums.TransactionType;
import com.moshood.mwta.model.Reversal;
import com.moshood.mwta.model.Transaction;
import com.moshood.mwta.model.Wallet;
import com.moshood.mwta.repository.NormalWalletRepository;
import com.moshood.mwta.repository.ReversalRepository;
import com.moshood.mwta.repository.TransactionRepository;
import com.moshood.mwta.repository.WalletRepository;
import com.moshood.mwta.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReversalService {

	private final ReversalRepository reversalRepository;
	private final TransactionRepository transactionRepository;
	private final NormalWalletRepository walletRepository;
	
	public ApiResponse reverseTransaction(String transactionReference) {
			Optional<Transaction> optionalTransaction = transactionRepository.findByTransactionReference(transactionReference);
			// checking if transactionReference Exist\
			// if it doesnt exist , inform them
		
			if (!optionalTransaction.isPresent()) 
				return new ApiResponse(transactionReference, "Transaction with the reference " + transactionReference + " doesn't exists");
			BigDecimal amount = optionalTransaction.get().getAmount();
			Transaction transaction = optionalTransaction.get();
			
			if (transaction.getTransactionType().equals(TransactionType.TRANSFER))
				return reverseTransferTransactions(transaction);
			else if (transaction.getTransactionType().equals(TransactionType.FUNDING))
				return reverseFundingTransactions(transaction);
			else if (transaction.getTransactionType().equals(TransactionType.WITHDRAWAL))
				return reverseWithdrawalTransactions(transaction);
			
			return new ApiResponse(transactionReference, "This transaction doesnt exists");
	}
	
	public ApiResponse reverseTransferTransactions(Transaction transaction ) {
	
		Wallet senderWallet = walletRepository.findWalletByUserId(transaction.getSenderId()).get();
		Wallet receiverWallet = walletRepository.findWalletByUserId(transaction.getReceiverId()).get();
		
		BigDecimal amount = transaction.getAmount();
		
		BigDecimal senderBalance = senderWallet.getBalance();
		BigDecimal receiverBalance = receiverWallet.getBalance();
		
		BigDecimal newSenderBalance = senderBalance.add(amount);
		log.info("adding " + amount + " to " + transaction.getSenderId() + "for Reversal");
		BigDecimal newReceiverBalance = receiverBalance.subtract(amount);
		log.info("subtracting " + amount + " from " + transaction.getReceiverId() + "for Reversal");
		walletRepository.updateBalance(transaction.getReceiverId(), newReceiverBalance);
		walletRepository.updateBalance(transaction.getSenderId(), newSenderBalance);
		createReversal(transaction);
		log.info( " Reversal of Transaction reference " + transaction.getTransactionReference() + "is successfull");
		return new ApiResponse(null, amount + "Has been succesfully reversed");
	}
	
	public ApiResponse reverseFundingTransactions(Transaction transaction) {
		Wallet receiverWallet = walletRepository.findWalletByUserId(transaction.getReceiverId()).get();
		BigDecimal amount = transaction.getAmount();
		BigDecimal receiverBalance = receiverWallet.getBalance();
		BigDecimal newReceiverBalance = receiverBalance.subtract(amount);
		
		log.info("subtracting " + amount + " from " + transaction.getReceiverId() + "for Reversal");
		walletRepository.updateBalance(transaction.getReceiverId(), newReceiverBalance);
		createReversal(transaction);
		log.info( " Reversal of Transaction reference " + transaction.getTransactionReference() + "is successfull");
		return new ApiResponse(null, amount + "Has been succesfully reversed");
	}
	
	public ApiResponse reverseWithdrawalTransactions(Transaction transaction) {
		Wallet senderWallet = walletRepository.findWalletByUserId(transaction.getReceiverId()).get();
		BigDecimal amount = transaction.getAmount();
		BigDecimal senderBalance = senderWallet.getBalance();
		BigDecimal newSenderBalance = senderBalance.add(senderBalance);
		
		log.info ("addign " + amount + " to " + transaction.getSenderId() + " for reversal");
		walletRepository.updateBalance(transaction.getReceiverId(), newSenderBalance);
		createReversal(transaction);
		log.info( " Reversal of Transaction reference " + transaction.getTransactionReference() + "is successfull");
		return new ApiResponse(null, amount + "Has been succesfully reversed");
	}
	
	
	
	private void createReversal(Transaction transaction) {
		Reversal reversal = new Reversal();
		reversal.setOriginalTransaction(transaction);
		reversal.setTransactionReference(transaction.getTransactionReference());
		reversalRepository.save(reversal);
	}
	
}
