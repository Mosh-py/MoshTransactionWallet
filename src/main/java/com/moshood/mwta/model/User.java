package com.moshood.mwta.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false, name = "user_id")
	private String userId;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String username;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Wallet wallet;
	public String getId() {
		return this.userId;
	}
	
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
		
		if (wallet != null && wallet.getUser() !=this)
			wallet.setUser(this);
	}
}
