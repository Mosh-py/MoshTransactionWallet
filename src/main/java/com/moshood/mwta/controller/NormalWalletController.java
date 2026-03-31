package com.moshood.mwta.controller;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moshood.mwta.dto.TransactionDto;
import com.moshood.mwta.dto.WalletDto;
import com.moshood.mwta.enums.Status;
import com.moshood.mwta.interfaces.WalletService;
import com.moshood.mwta.model.NormalWallet;
import com.moshood.mwta.model.Transaction;
import com.moshood.mwta.model.Wallet;
import com.moshood.mwta.response.ApiResponse;
import com.moshood.mwta.response.CreationResponse;
import com.moshood.mwta.response.TransferResponse;
import com.moshood.mwta.service.NormalWalletService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/normalWallet")
@RequiredArgsConstructor
@Slf4j
public class NormalWalletController {

	private final WalletService normalWalletService;

	@PostMapping("create")
	@Operation(summary = " Create a Wallet", description = "Credit a wallet with a specific amount")
	public ResponseEntity<CreationResponse> createNormalWallet(@RequestBody WalletDto walletDto) {
		try {
			CreationResponse response = normalWalletService.createWallet(walletDto);
			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException de) {
			log.error(" AN ATTEMPT was made to make a duplicate name entry " + walletDto.getUsername()
					+ LocalDateTime.now());
			return ResponseEntity.ok(new CreationResponse(walletDto.getUsername(), walletDto.getEmail(), "sas",
					"Kindly provide a unique email", Status.FAILED));
		}

	}

	@PostMapping("/fund")
	@Operation(summary = "Fund your wallet" , description = " Fund your wallet from external")
	public ResponseEntity<ApiResponse> fundWallet(@RequestBody TransactionDto transaction,
			@RequestHeader("idempotency-key") String idempotencyKey) {
		
		try {
			ApiResponse response = normalWalletService.fundWallet(transaction, idempotencyKey);
			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException e) {
			log.error(" AN ATTEMPT was made to make a Idempotent Operation " + LocalDateTime.now());
			return ResponseEntity.ok(new ApiResponse(transaction.getSenderId(), "This operation has been completed already"));
		}
		
		
	}

	@PostMapping("/transfer")
	@Operation(summary = "Transfer to another wallet", description = " Transfer from your wallet to another wallet")
	public ResponseEntity<TransferResponse> transferFromWallet(@RequestBody TransactionDto transactionDto,
			@RequestHeader("idempotency-key") String idempotencyKey) {
		
	
			TransferResponse response = normalWalletService.transferFromWallet(transactionDto, idempotencyKey);
			
			return ResponseEntity.ok(response);
		
		
	}

	@PostMapping("/withdraw")
	@Operation(summary = "withdraw money from your wallet", description = " Withdraw Money from your wallet")
	public ResponseEntity<ApiResponse> withdrawFromWallet(@RequestBody TransactionDto transactionDto,
			@RequestHeader("idempotency-key") String idempotencyKey) {
		try {
			ApiResponse response = normalWalletService.withdrawFromWallet(transactionDto, idempotencyKey);
			return ResponseEntity.ok(response);
		} catch(DataIntegrityViolationException e) {
			return ResponseEntity.ok(new ApiResponse(transactionDto.getReceiverId(), " This operation has been completed"));
		}
		
	}
}
