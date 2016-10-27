/**
 * Class to map the Tokens View Object
 * @author Arpit Pittie
 */
package com.project.vo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TokensVO {

	private long tokenId;
	private String requestTime;
	private UsersVO user;

	public long getTokenId() {
		return tokenId;
	}

	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public UsersVO getUser() {
		return user;
	}

	public void setUser(UsersVO user) {
		this.user = user;
	}
}
