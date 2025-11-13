package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "TCREDITCARDS")
public class CreditCard extends PaymentMean {
	private String number;

	private String type;
	private LocalDate validThru;

	CreditCard() {

	}

	public CreditCard(String number, String type, LocalDate validThru) {
		this.number = number;
		this.type = type;
		this.validThru = validThru;
	}

	public String getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public LocalDate getValidThru() {
		return validThru;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CreditCard that = (CreditCard) o;
		return Objects.equals(number, that.number);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(number);
	}

	@Override
	public String toString() {
		return "CreditCard{" + "number='" + number + '\'' + ", type='" + type + '\''
				+ ", validThru=" + validThru + '}';
	}

	@Override
	public void pay(double amount) {
		if (!canPay(amount)) {
			throw new IllegalStateException("The credit card is outdated");
		}
		super.pay(amount);
	}

	/**
	 * A credit card can pay if is not outdated
	 */
	@Override
	public boolean canPay(Double amount) {
		if (validThru.isAfter(LocalDate.now())) {
			return true;
		}
		return false;
	}

}
