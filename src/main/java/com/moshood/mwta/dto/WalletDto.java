package com.moshood.mwta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDto {

	@Schema(description = "The name the user wants to be refered as", example = "lovelyMan" )
	private String username;
	@Schema(description = "The email the user wants to use", example = "bestman@gmail.com")
	private String email;
	
}
