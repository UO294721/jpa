package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "TVOUCHERS")
public class Voucher extends PaymentMean {
	private String code;

	private double available = 0.0;
	private String description;

	Voucher() {

	}

	public Voucher(String code, String description, double available) {
		this.code = code;
		this.description = description;
		this.available = available;
	}

	public String getCode() {
		return code;
	}

	public double getAvailable() {
		return available;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Augments the accumulated (super.pay(amount) ) and decrements the available
	 * 
	 * @throws IllegalStateException if not enough available to pay
	 */
	@Override
	public void pay(double amount) {
		if (!canPay(amount)) {
			throw new IllegalStateException(
					"Not enough available in voucher to pay the amount");
		}
		super.pay(amount);
		this.available -= amount;
	}

	/**
	 * A voucher can pay if it has enough available to pay the amount
	 */
	@Override
	public boolean canPay(Double amount) {
		return amount <= available;
	}

}
