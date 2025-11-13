package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TPAYROLLS")
public class Payroll extends BaseEntity {

	private LocalDate date;
	private double extraSalary;
	private double taxDeduction;
	private double nicDeduction;
	private double productivityEarning;
	private double trienniumEarning;

	@ManyToOne
	private Contract contract;

	Payroll() {
	}

	public Payroll(Contract c, LocalDate payrollDate) {
		ArgumentChecks.isNotNull(c, "The contract cannot be null");
		ArgumentChecks.isNotNull(payrollDate, "The payroll date cannot be null");
		ArgumentChecks.isTrue(!payrollDate.isBefore(c.getStartDate()),
				"The payroll date cannot be before the contract signing date");
		this.contract = c;
		this.date = payrollDate;
		Associations.Generates.link(c, this);
		productivityEarning = calculateProductivityEarning();
	}

	public double getGrossSalary() {
		return getMonthlyBaseSalary() + extraSalary + productivityEarning
				+ trienniumEarning;
	}

	public LocalDate getDate() {
		return date;
	}

	public Contract getContract() {
		return contract;
	}

	public double getMonthlyBaseSalary() {
		return contract.getAnnualBaseSalary() / 14;
	}

	public double getExtraSalary() {
		if (date.getMonthValue() == 6 || date.getMonthValue() == 12) {
			extraSalary = contract.getAnnualBaseSalary() / 14;
			return extraSalary;
		}
		extraSalary = 0;
		return extraSalary;
	}

	public double getProductivityEarning() {
		return productivityEarning;
	}

	public double getTrienniumEarning() {
		int trienniums = (int) ChronoUnit.YEARS.between(contract.getStartDate(),
				date) / 3;
		trienniumEarning = trienniums
				* contract.getProfessionalGroup().getTrienniumPayment();
		return trienniumEarning;
	}

	public double getTaxDeduction() {
		return getGrossSalary() * contract.getTaxRate();
	}

	public double getNicDeduction() {
		nicDeduction = 0.05 * contract.getAnnualBaseSalary() / 12;
		return nicDeduction;
	}

	public double getNetSalary() {
		return contract.getAnnualBaseSalary() / 14 + extraSalary
				+ productivityEarning + trienniumEarning - taxDeduction - nicDeduction;
	}

	public double getTotalDeductions() {
		return 0;
	}

	void _setContract(Contract contract2) {
		this.contract = contract2;
	}

	Contract _getContract() {
		return contract;
	}

	private double calculateProductivityEarning() {

		double productivityRate = contract.getProfessionalGroup()
				.getProductivityRate();

		if (contract.getMechanic() == null) {
			return 0.0;
		}

		double mechanicProductivity = contract.getMechanic().getAssigned().stream()
				.filter(a -> a.getDate().getMonthValue() == this.date.getMonthValue()
						&& a.getDate().getYear() == this.date.getYear() && a.isInvoiced())
				.mapToDouble(w -> w.getAmount()).sum();

		return productivityRate * mechanicProductivity;

	}

}
