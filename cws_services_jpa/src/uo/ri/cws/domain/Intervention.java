package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TINTERVENTIONS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DATE", "MECHANIC_ID" }))
public class Intervention extends BaseEntity {
	// natural attributes
	@Basic(optional = false)
	private LocalDateTime date;
	@Basic(optional = false)
	private int minutes;

	// accidental attributes
	@ManyToOne
	private WorkOrder workOrder;
	@ManyToOne
	private Mechanic mechanic;
	@OneToMany(mappedBy = "intervention")
	private Set<Substitution> substitutions = new HashSet<>();

	Intervention() {
	}

	public Intervention(LocalDateTime date, int minutes, WorkOrder workOrder,
			Mechanic mechanic) {
		ArgumentChecks.isNotNull(date, "The intervention's date cannot be null");
		ArgumentChecks.isTrue(minutes >= 0,
				"The intervention's minutes must be positive");
		ArgumentChecks.isNotNull(workOrder,
				"The intervention's work order cannot be null");
		ArgumentChecks.isNotNull(mechanic,
				"The intervention's mechanic cannot be null");

		this.date = date.truncatedTo(ChronoUnit.MILLIS);
		this.minutes = minutes;
		Associations.Intervenes.link(workOrder, this, mechanic);
	}

	public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
		this(LocalDateTime.now(), minutes, workOrder, mechanic);
	}

	public Intervention(Mechanic mechanic2, WorkOrder workOrder2,
			LocalDateTime date2, int i) {
		this(date2, i, workOrder2, mechanic2);
	}

	public LocalDateTime getDate() {
		return date.truncatedTo(ChronoUnit.MILLIS);
	}

	public int getMinutes() {
		return minutes;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	void _setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	Mechanic _getMechanic() {
		return mechanic;
	}

	WorkOrder _getWorkOrder() {
		return workOrder;
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>(substitutions);
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	@Override
	public String toString() {
		return "Intervention{" + "date=" + date + ", minutes=" + minutes
				+ ", workOrder=" + workOrder + ", mechanic=" + mechanic + '}';
	}

	public double getAmount() {
		return workOrder.getVehicle().getVehicleType().getPricePerHour() * minutes
				/ 60.0
				+ substitutions.stream().mapToDouble(Substitution::getAmount).sum();
	}
}
