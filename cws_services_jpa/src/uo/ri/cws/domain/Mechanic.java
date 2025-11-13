package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TMECHANICS")
public class Mechanic extends BaseEntity {
	// natural attributes
	@Column(unique = true)
	private String nif;
	@Basic(optional = false)
	private String surname;
	@Basic(optional = false)
	private String name;

	public Mechanic(String nif, String name, String surname) {
		ArgumentChecks.isNotBlank(nif, "The mechanic's NIF cannot be blank");
		ArgumentChecks.isNotBlank(name, "The mechanic's name cannot be blank");
		ArgumentChecks.isNotBlank(surname,
				"The mechanic's surname cannot be blank");
		this.nif = nif;
		this.name = name;
		this.surname = surname;
	}

	// accidental attributes
	@OneToMany(mappedBy = "mechanic")
	private final Set<WorkOrder> assigned = new HashSet<>();
	@OneToMany(mappedBy = "mechanic")
	private final Set<Intervention> interventions = new HashSet<>();
	@OneToMany(mappedBy = "mechanic")
	private final Set<Contract> contracts = new HashSet<>();

	@OneToOne(mappedBy = "mechanic")
	private Contract contract;

	Mechanic() {
	}

	public Mechanic(String nif) {
		this(nif, "no-name", "no-surname");
	}

	public Set<WorkOrder> getAssigned() {
		return new HashSet<>(assigned);
	}

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>(interventions);
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}

	Set<WorkOrder> _getWorkOrders() {
		return assigned;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getNif() {
		return nif;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Mechanic mechanic = (Mechanic) o;
		return Objects.equals(nif, mechanic.nif);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nif);
	}

	@Override
	public String toString() {
		return "Mechanic{" + "nif='" + nif + '\'' + ", name='" + name + '\''
				+ ", surname='" + surname + '\'' + '}';
	}

	public void setName(String name2) {
		this.name = name2;
	}

	public void setSurname(String surname2) {
		this.surname = surname2;
	}

	public Set<Contract> getContracts() {
		return new HashSet<>(contracts);
	}

	public Optional<Contract> getContractInForce() {
		return Optional.ofNullable(contract);
	}

	Set<Contract> _getContracts() {
		return contracts;
	}

	public void _setInforceContract(Contract contract2) {
		if (this.contract != null && this.contract.isTerminated() == false) {
			this.contract.terminate(contract2.getStartDate().minusDays(1));
		}
		this.contract = contract2;
	}

}
