package com.moshood.mwta.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moshood.mwta.dto.TransactionDto;
import com.moshood.mwta.enums.TransactionStatus;
import com.moshood.mwta.enums.TransactionType;
import com.moshood.mwta.model.Transaction;
import com.moshood.mwta.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	
	@GetMapping("/AllTransactions")
	@Operation(summary = "get all your transactions")
	public Page<TransactionDto> getTransactions(@RequestParam int page, @RequestParam int size){
		return transactionService.getTransactions(page, size);
	}
	
	@GetMapping("/transactions")
	@Operation(summary = "filter your transactions either by status, type or both")
	public Page<TransactionDto> getTransactions(@RequestParam(required = false) TransactionStatus status, @RequestParam(required = false) TransactionType type,
			int page, int size){
		if (status != null && type != null)
			return transactionService.getTransactionByTypeAndStatus(type, status, page, size);
		else if(status !=null)
			return transactionService.getTransactionByStatus(status, page, size);
		else if(type!=null)
			return transactionService.getTransactionByType(type, page, size);
		return transactionService.getTransactions(page, size);
	}
}
