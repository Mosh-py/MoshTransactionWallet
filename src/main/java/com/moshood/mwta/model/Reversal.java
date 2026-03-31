package com.moshood.mwta.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Reversal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   
    @Column(name = "date_reversed")
    @CreatedDate
    private LocalDateTime dateReversed;
    
    @LastModifiedDate
    @Column(name = "date_last_modified")
    private LocalDateTime dateLastModified;
    
    @Column(unique = true)
    private String transactionReference;
    
    @OneToOne(optional = false)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id", unique = true)
    private Transaction originalTransaction;
}
