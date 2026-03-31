package com.moshood.mwta.interfaces;

import com.moshood.mwta.dto.TransactionDto;
import com.moshood.mwta.dto.WalletDto;
import com.moshood.mwta.model.Transaction;
import com.moshood.mwta.model.Wallet;
import com.moshood.mwta.response.ApiResponse;
import com.moshood.mwta.response.CreationResponse;
import com.moshood.mwta.response.TransferResponse;

public interface WalletService {

	public CreationResponse createWallet( WalletDto walletDto);
	
	public ApiResponse fundWallet(TransactionDto dto, String idempotemcyKey);
	public int deleteWallet();
	public TransferResponse transferFromWallet(TransactionDto transactionDto, String idempotencyKey);
	
	public ApiResponse withdrawFromWallet(TransactionDto transactionDto, String idempotencyKey);
}
