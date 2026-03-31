package com.moshood.mwta.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.moshood.mwta.dto.TransactionDto;
import com.moshood.mwta.dto.TransferDto;
import com.moshood.mwta.dto.WalletDto;
import com.moshood.mwta.enums.Status;
import com.moshood.mwta.enums.TransactionStatus;
import com.moshood.mwta.enums.TransactionType;
import com.moshood.mwta.interfaces.WalletService;
import com.moshood.mwta.model.NormalWallet;
import com.moshood.mwta.model.Transaction;
import com.moshood.mwta.model.User;
import com.moshood.mwta.model.Wallet;
import com.moshood.mwta.repository.NormalWalletRepository;
import com.moshood.mwta.repository.TransactionRepository;
import com.moshood.mwta.repository.UserRepository;
import com.moshood.mwta.response.ApiResponse;
import com.moshood.mwta.response.CreationResponse;
import com.moshood.mwta.response.TransferResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class NormalWalletService implements WalletService {

	private final NormalWalletRepository normalWalletRepository;
	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;
	private final TransactionService transactionService;

	@Override
	@Transactional
	
	public CreationResponse createWallet(WalletDto walletDto) {
		
		NormalWallet normalWallet = new NormalWallet();
		User user = new User();
		try {
			user.setEmail(walletDto.getEmail());
			log.info("Sving the user mail "  + walletDto.getEmail());
			user.setUsername(walletDto.getUsername());
			log.info("Sving the user username"  + walletDto.getUsername());
			userRepository.save(user);
			log.info("Sving the user " + user);
			normalWallet.setUser(user);
			log.info("Saving the wallet " );
			normalWalletRepository.save(normalWallet);
		} catch(DataIntegrityViolationException de) {
			log.error( " AN ATTEMPT was made to make a duplicate name entry " + walletDto.getUsername() + LocalDateTime.now());
			return new CreationResponse(walletDto.getUsername(), walletDto.getEmail(), user.getId(), "Kindly provide a unique email", Status.FAILED);
		}
		
		catch (Exception e) {
			
			return new CreationResponse(walletDto.getUsername(), walletDto.getEmail(), user.getId(), "Somwthing went wromg, please try again", Status.FAILED);
		}
		
		return new CreationResponse(walletDto.getUsername(), walletDto.getEmail(), user.getId(), "", Status.SUCCESS);
	}

	@Override
	public int deleteWallet() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	@Transactional
	public ApiResponse fundWallet(TransactionDto transactionDto, String idempotencyKey) {
		
		try {
			Wallet wallet = normalWalletRepository.findWalletByUserId(transactionDto.getReceiverId()).get();
			return this.isFundOperationAndUpdate(transactionDto, wallet, idempotencyKey, true);
			
		} catch (NoSuchElementException ne) {
			return new ApiResponse(transactionDto.getReceiverId(), "The wallet address aint valid");
		}
	}
	
	@Transactional
	@Override
	public TransferResponse transferFromWallet(TransactionDto transactionDto, String idempotencyKey) {
		String senderId = transactionDto.getSenderId();
		String receiverId = transactionDto.getReceiverId();
		BigDecimal amount = transactionDto.getAmount();
		if (transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
			log.info("Idempotemt Operation encountered at " + LocalDateTime.now());
			return new TransferResponse(senderId, receiverId, amount, "Successful", TransactionStatus.SUCCESS);
		}
		try {

			Wallet receiverWallet = normalWalletRepository.findWalletByUserId(receiverId).get();
			Wallet senderWallet = normalWalletRepository.findWalletByUserId(senderId).get();
			
			// check if sender has enough amount
			if (senderWallet.getBalance().compareTo(amount)>=1) {
				doOperationAndUpdateBalance(transactionDto);
			} else{
				return new TransferResponse(senderId, receiverId, amount, "Insufficient Funds", TransactionStatus.SUCCESS);
			}
			
			// Persisting transaction to the service
			transactionService.createTransaction(transactionDto, idempotencyKey, receiverWallet, TransactionType.TRANSFER);
			// check if transcation persisted successfully
				return new TransferResponse(senderId, receiverId, amount, "Success", TransactionStatus.SUCCESS);
				
		} // catch if they are sending to the wrong address
		catch (NoSuchElementException e) {
			//
			return new TransferResponse(senderId, receiverId, amount, receiverId + " doesn't exist", TransactionStatus.FAILED);
		} catch (DataIntegrityViolationException de) {
			log.info("Idempotemt Operation encountered at " + LocalDateTime.now());
			return new TransferResponse(senderId, receiverId, amount, "Successful", TransactionStatus.SUCCESS);
		}
	}
	
	public void doOperationAndUpdateBalance(TransactionDto transactionDto) {
		String senderId = transactionDto.getSenderId();
		String receiverId = transactionDto.getReceiverId();
		BigDecimal amount = transactionDto.getAmount();
		Wallet receiverWallet = normalWalletRepository.findWalletByUserId(receiverId).get();
		Wallet senderWallet = normalWalletRepository.findWalletByUserId(senderId).get();
		BigDecimal senderNewBalance = senderWallet.getBalance().subtract(amount);
		log.info("Proceeding to deduct " + amount + " from  account " + senderWallet.getUser().getEmail() + "at " + LocalDateTime.now());
		BigDecimal receiverNewBalance = receiverWallet.getBalance().add(amount);
		log.info("Proceeding to add " + amount + " to account " + receiverWallet.getUser().getEmail() + "at " + LocalDateTime.now());
		
		// Proceeding to update the wallet Balances
		normalWalletRepository.updateBalance(receiverId, receiverNewBalance);
		log.info(" Updated the receiver balance to " + receiverNewBalance );
		normalWalletRepository.updateBalance(senderId, senderNewBalance);
	}

	@Override
	public ApiResponse withdrawFromWallet(TransactionDto transactionDto, String idempotencyKey) {
		BigDecimal amount = transactionDto.getAmount();
		String senderId = transactionDto.getSenderId();
		try {
			Wallet senderWallet = normalWalletRepository.findWalletByUserId(senderId).get();
			if (senderWallet.getBalance().compareTo(amount) >=0)
				return isFundOperationAndUpdate(transactionDto, senderWallet, idempotencyKey, false );
			else 
				return new ApiResponse(transactionDto.getSenderId(), "You don't have sufficient funds in your account");
		} catch (NoSuchElementException e) {
			return new ApiResponse(transactionDto.getSenderId(), "The wallet address aint valid");
		}
		
	}

	// Smart function that withdraws or funds account based on what you tell it
	public ApiResponse isFundOperationAndUpdate(TransactionDto transactionDto, Wallet wallet, String idempotencyKey, boolean toFund) {
		BigDecimal amount = transactionDto.getAmount();
		String receiverId = transactionDto.getReceiverId();
	
		if (transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
			return new ApiResponse(receiverId, amount + " has been succesfully sent to " + receiverId);
		}
		Wallet receiverWallet = normalWalletRepository.findWalletByUserId(receiverId).get();
		BigDecimal newBalance;
		if (toFund) {
			newBalance = receiverWallet.getBalance().add(amount);
			log.info(" Funding the waller " + wallet.getUser().getEmail() + " with " + amount + "at " + LocalDateTime.now().toString());
		} else {
			newBalance = receiverWallet.getBalance().subtract(amount);
			log.info(" Withdrawing from the wallet " + wallet.getUser().getEmail() + " with " + amount + "at " + LocalDateTime.now());
		}
		normalWalletRepository.updateBalance(receiverId, newBalance);
		transactionDto.setStatus(TransactionStatus.SUCCESS);
		transactionService.createTransaction(transactionDto, idempotencyKey, wallet, TransactionType.FUNDING);
		return new ApiResponse(receiverId, amount + " has been succesfully sent to " + receiverId);
	}


}
