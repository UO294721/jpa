package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCONTRACTS")
public class Contract extends BaseEntity {

	@OneToOne
	private Mechanic mechanic;
	@ManyToOne
	private ContractType contractType;
	@ManyToOne
	private ProfessionalGroup professionalGroup;

	private LocalDate startDate;
	private LocalDate endDate;
	private double annualBaseSalary;
	private double taxRate;
	private double settlement;

	public enum ContractState {
		IN_FORCE, TERMINATED
	}

	@Enumerated(EnumType.STRING)
	private ContractState state = ContractState.IN_FORCE;

	@OneToMany(mappedBy = "contract")
	private Set<Payroll> payrolls = new HashSet<>();

	Contract() {
	}

	public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group,
			LocalDate signingDate, double annualGrossSalary) {
		this(mechanic, type, group, signingDate, LocalDate.MAX, annualGrossSalary);
		ArgumentChecks.isTrue(!type.getName().equals("FIXED_TERM"),
				"The end date cannot be null for non FIXED_TERM contracts");
	}

	public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group,
			LocalDate signingDate, LocalDate endDate, double annualSalary) {
		ArgumentChecks.isNotNull(mechanic, "The mechanic cannot be null");
		ArgumentChecks.isNotNull(type, "The contract type cannot be null");
		ArgumentChecks.isNotNull(group, "The professional group cannot be null");
		ArgumentChecks.isNotNull(signingDate, "The signing date cannot be null");
		ArgumentChecks.isTrue(annualSalary >= 0,
				"The annual salary cannot be negative");
		ArgumentChecks.isTrue(endDate == null || !endDate.isBefore(signingDate),
				"The end date must be after the signing date");

		this.mechanic = mechanic;
		this.contractType = type;
		this.professionalGroup = group;
		this.startDate = signingDate.withDayOfMonth(1);
		this.annualBaseSalary = annualSalary;
		this.taxRate = calculateTaxRate();

		if (!type.getName().equals("FIXED_TERM")) {
			endDate = null;
		} else {
			ArgumentChecks.isNotNull(endDate,
					"The end date cannot be null for FIXED_TERM");
			this.endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());
			settlement = calculateSettlement();

		}

		Associations.Binds.link(mechanic, this);
		Associations.Defines.link(type, this);
		Associations.Categorizes.link(group, this);
	}

	public double getAnnualBaseSalary() {
		return annualBaseSalary;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public double getSettlement() {
		return settlement;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public ProfessionalGroup getProfessionalGroup() {
		return professionalGroup;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	public boolean isInForce() {
		return state == ContractState.IN_FORCE;
	}

	public Set<Payroll> getPayrolls() {
		return new HashSet<>(payrolls);
	}

	public void terminate(LocalDate endDate2) {
		ArgumentChecks.isNotNull(endDate2, "The end date cannot be null");
		ArgumentChecks.isTrue(!endDate2.isBefore(startDate),
				"The end date cannot be before the signing date");
		if (state == ContractState.TERMINATED) {
			throw new IllegalStateException("The contract is already terminated");
		}
		endDate = endDate2.withDayOfMonth(endDate2.lengthOfMonth());

		state = ContractState.TERMINATED;
		Associations.Binds.unlink(this);
		this.settlement = calculateSettlement();
	}

	public boolean isTerminated() {
		return state == ContractState.TERMINATED;
	}

	Set<Payroll> _getPayrolls() {
		return payrolls;
	}

	void _setMechanic(Mechanic mechanic2) {
		this.mechanic = mechanic2;
	}

	void _setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	void _setProfessionalGroup(ProfessionalGroup professionalGroup) {
		this.professionalGroup = professionalGroup;
	}

	public ContractState getState() {
		return state;
	}

	public double getTaxRate() {
		return taxRate;
	}

	private double calculateTaxRate() {
		double taxRate = 0;
		if (annualBaseSalary < 12400) {
			taxRate = 0.19;
		} else if (annualBaseSalary < 20200) {
			taxRate = 0.24;
		} else if (annualBaseSalary < 35200) {
			taxRate = 0.30;
		} else if (annualBaseSalary < 60000) {
			taxRate = 0.37;
		} else if (annualBaseSalary < 300000) {
			taxRate = 0.45;
		} else {
			taxRate = 0.47;
		}
		return taxRate;
	}

	private double calculateSettlement() {

		if (endDate == null) {
			return 0;
		}

		if (ChronoUnit.DAYS.between(startDate, endDate) < 364) {
			return 0;
		}

		if (payrolls.isEmpty()) {
			return 0;
		}

		int yearsWorked = (int) ChronoUnit.YEARS.between(startDate,
				endDate.plusDays(1));

		return contractType.getCompensationDaysPerYear() * (annualBaseSalary / 365)
				* yearsWorked;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setAnnualBaseSalary(double annualBaseSalary) {
		this.annualBaseSalary = annualBaseSalary;
		this.taxRate = calculateTaxRate();
	}

}
