package com.moshood.mwta.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moshood.mwta.repository.ReversalRepository;
import com.moshood.mwta.response.ApiResponse;
import com.moshood.mwta.service.ReversalService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/reversal")
@RequiredArgsConstructor
public class ReversalController {
	
	private final ReversalService reversalService;
	
	@PostMapping("reverse")
	@Operation(summary = " Reverse the wallet Transactions", description = " Help reverse transactions")
	public ResponseEntity<ApiResponse> reverseTransaction(@RequestParam String transactionReference){
		try {
			return ResponseEntity.ok(reversalService.reverseTransaction(transactionReference));
		} catch (DataIntegrityViolationException e){
			return ResponseEntity.ok(new ApiResponse("null", "This transaction has been reversed Already"));
		}
		
	}
	
	
}
