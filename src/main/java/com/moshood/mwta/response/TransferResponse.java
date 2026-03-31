package com.moshood.mwta.response;

import java.math.BigDecimal;

import com.moshood.mwta.enums.TransactionStatus;

public record TransferResponse(String senderId, String receiverId, BigDecimal balance, String message, TransactionStatus status) {

}
