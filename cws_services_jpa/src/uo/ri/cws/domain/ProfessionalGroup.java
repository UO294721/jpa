package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TPROFESSIONALGROUPS")
public class ProfessionalGroup extends BaseEntity {

	private String name;
	private double trienniumPayment;
	private double productivityRate;

	@OneToMany(mappedBy = "professionalGroup")
	private Set<Contract> contracts = new HashSet<>();

	ProfessionalGroup() {
	}

	public ProfessionalGroup(String name, double trienniumSalary,
			double productivityPlus) {
		this.name = name;
		this.trienniumPayment = trienniumSalary;
		this.productivityRate = productivityPlus;
	}

	public Set<Contract> getContracts() {
		return new HashSet<>(contracts);
	}

	public double getProductivityRate() {
		return productivityRate;
	}

	public Set<Contract> _getContracts() {
		return contracts;
	}

	public String getName() {
		return name;
	}

	public double getTrienniumPayment() {
		return trienniumPayment;
	}

}
