package com.moshood.mwta.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="id")
	protected String id;
	
	@Column(nullable = false)
	protected BigDecimal balance = new BigDecimal(0);
	
	@OneToOne(cascade = CascadeType.ALL )
    @JoinColumn(name = "user_id")
    private User user;
	public BigDecimal getBalance() {
		return this.balance;
	}
	
	public void setUser(User user) {
		this.user = user;
		
		if (user!=null && user.getWallet()!=this) {
			user.setWallet(this);
		}
	}
}
