package com.moshood.mwta.dto;

import java.math.BigDecimal;

import com.moshood.mwta.enums.TransactionStatus;
import com.moshood.mwta.enums.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

	private BigDecimal amount;
	private String senderId;
	private String receiverId;
	private String message;
	private TransactionType transactionType;
	private TransactionStatus status;
}
