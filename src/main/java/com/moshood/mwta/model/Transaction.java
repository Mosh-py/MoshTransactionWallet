package com.moshood.mwta.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.transaction.support.TransactionTemplate;

import com.moshood.mwta.dto.TransactionDto;
import com.moshood.mwta.enums.TransactionStatus;
import com.moshood.mwta.enums.TransactionType;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumeratedValue;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "transactions")

public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	protected String id;

	@CreationTimestamp
	protected LocalDateTime dateCreated;
	protected BigDecimal amount;
	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	@ManyToOne
	@JoinColumn(name = "wallet_Id")
	private Wallet wallet;

	@Column(nullable = false, unique = true)
	private String idempotencyKey;

	private String senderId;
	private String receiverId;
	private String transactionReference;
	@OneToOne(mappedBy = "originalTransaction", cascade = CascadeType.ALL)
	private Reversal reversal;

	public TransactionDto toDto() {
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setAmount(this.getAmount());
		transactionDto.setReceiverId(this.getWallet().getId());
//		transactionDto.setSenderId(transfer)
		transactionDto.setStatus(this.getTransactionStatus());
		transactionDto.setTransactionType(this.getTransactionType());
		return transactionDto;
	}

}
