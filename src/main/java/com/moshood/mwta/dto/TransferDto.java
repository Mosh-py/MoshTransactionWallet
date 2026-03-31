package com.moshood.mwta.dto;

import java.math.BigDecimal;

public record TransferDto(String senderId, String receiverId, BigDecimal amount, String message) {
	
}
