package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TCONTRACTTYPES")
public class ContractType extends BaseEntity {

	private String name;
	private double compensationDaysPerYear;

	@OneToMany(mappedBy = "contractType")
	private Set<Contract> contracts = new HashSet<>();

	ContractType() {
	}

	public ContractType(String name, double d) {
		this.name = name;
		this.compensationDaysPerYear = d;
	}

	public Set<Contract> getContracts() {
		return new HashSet<>(contracts);
	}

	public double getCompensationDaysPerYear() {
		return compensationDaysPerYear;
	}

	public Set<Contract> _getContracts() {
		return contracts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name2) {
		this.name = name2;
	}

	public void setCompensationDays(double compensationDays) {
		this.compensationDaysPerYear = compensationDays;
	}

}
