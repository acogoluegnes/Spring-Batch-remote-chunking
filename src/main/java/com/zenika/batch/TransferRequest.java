/**
 * 
 */
package com.zenika.batch;

import java.io.Serializable;

/**
 * @author acogoluegnes
 *
 */
public class TransferRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private boolean processed = false;
	
	private Long accountId;
	
	private Integer amount;
	
	public TransferRequest() { }
	
	public TransferRequest(Long id, Long accountId, Integer amount) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public boolean isProcessed() {
		return processed;
	}

	public Long getAccountId() {
		return accountId;
	}

	public Integer getAmount() {
		return amount;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "TransferRequest [id=" + id + ", accountId=" + accountId
				+ ", amount=" + amount + "]";
	}
		
}
