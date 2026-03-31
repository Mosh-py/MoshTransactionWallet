package com.moshood.mwta.response;

import com.moshood.mwta.enums.Status;

public record CreationResponse( String name, String email, String userId,String message, Status status) {

}
