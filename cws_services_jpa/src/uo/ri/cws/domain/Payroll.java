package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TPAYROLLS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CONTRACT_ID", "DATE" }))
public class Payroll extends BaseEntity {

	@Basic(optional = false)
	private LocalDate date;
	@Basic(optional = false)
	private double extraSalary;
	@Basic(optional = false)
	private double taxDeduction;
	@Basic(optional = false)
	private double nicDeduction;
	@Basic(optional = false)
	private double productivityEarning;
	@Basic(optional = false)
	private double trienniumEarning;
	@Basic(optional = false)
	private double baseSalary;

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

		calculateFields();
	}

	public double getGrossSalary() {
		return baseSalary + extraSalary + productivityEarning + trienniumEarning;
	}

	public LocalDate getDate() {
		return date;
	}

	public Contract getContract() {
		return contract;
	}

	public double getMonthlyBaseSalary() {
		return baseSalary;
	}

	public double getExtraSalary() {
		return extraSalary;
	}

	public double getProductivityEarning() {
		return productivityEarning;
	}

	public double getTrienniumEarning() {
		return trienniumEarning;
	}

	public double getTaxDeduction() {
		return taxDeduction;
	}

	public double getNicDeduction() {
		return nicDeduction;
	}

	public double getBaseSalary() {
		return baseSalary;
	}

	public double getNetSalary() {
		return getMonthlyBaseSalary() + extraSalary + productivityEarning
				+ trienniumEarning - taxDeduction - nicDeduction;
	}

	public double getTotalDeductions() {
		return getTaxDeduction() + getNicDeduction();
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

		double mechanicProductivity = 0.0;

		for (Intervention i : contract.getMechanic().getInterventions()) {
			WorkOrder w = i.getWorkOrder();
			if (w.getDate().getMonth().equals(this.date.getMonth())
					&& w.getDate().getYear() == this.date.getYear()) {
				mechanicProductivity += w.getAmount();
			}
		}

		return productivityRate * mechanicProductivity;

	}

	private double calculateTaxDeduction() {
		return getGrossSalary() * contract.getTaxRate();
	}

	private double calculateNicDeduction() {
		return 0.05 * contract.getAnnualBaseSalary() / 12;
	}

	private double calculateTrienniumEarning() {
		int trienniums = (int) ChronoUnit.YEARS.between(contract.getStartDate(),
				date) / 3;
		return trienniums * contract.getProfessionalGroup().getTrienniumPayment();
	}

	private double calculateExtraSalary() {
		double extraSalary;
		if (date.getMonthValue() == 6 || date.getMonthValue() == 12) {
			extraSalary = contract.getAnnualBaseSalary() / 14;
			return extraSalary;
		}
		return 0.0;
	}

	private void calculateFields() {
		this.baseSalary = contract.getAnnualBaseSalary() / 14;
		this.productivityEarning = calculateProductivityEarning();
		this.nicDeduction = calculateNicDeduction();
		this.trienniumEarning = calculateTrienniumEarning();
		this.extraSalary = calculateExtraSalary();
		this.taxDeduction = calculateTaxDeduction();
	}

}
