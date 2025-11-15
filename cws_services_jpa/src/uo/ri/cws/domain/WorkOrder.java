package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TWORKORDERS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DATE", "VEHICLE_ID" }))
public class WorkOrder extends BaseEntity {
	public enum WorkOrderState {
		OPEN, ASSIGNED, FINISHED, INVOICED
	}

	// natural attributes
	@Basic(optional = false)
	private LocalDateTime date;
	@Basic(optional = false)
	private String description;
	@Basic(optional = false)
	private double amount = 0.0;
	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	private WorkOrderState state = WorkOrderState.OPEN;

	// accidental attributes
	@ManyToOne
	private Vehicle vehicle;
	@ManyToOne
	private Mechanic mechanic;
	@ManyToOne
	private Invoice invoice;
	@OneToMany(mappedBy = "workOrder")
	private Set<Intervention> interventions = new HashSet<>();

	WorkOrder() {
	}

	public WorkOrder(Vehicle vehicle, LocalDateTime date, String description) {
		ArgumentChecks.isNotNull(vehicle,
				"The work order's vehicle cannot be null");
		ArgumentChecks.isNotNull(date, "The work order's date cannot be null");
		ArgumentChecks.isNotBlank(description,
				"The work order's description cannot be blank");

		this.date = date.truncatedTo(ChronoUnit.MILLIS);
		this.description = description;
		Associations.Fixes.link(vehicle, this);
	}

	public WorkOrder(Vehicle vehicle, String string) {
		this(vehicle, LocalDateTime.now(), string);
	}

	public WorkOrder(Vehicle vehicle) {
		this(vehicle, LocalDateTime.now(), "no description");
	}

	public WorkOrder(Vehicle vehicle2, LocalDateTime now) {
		this(vehicle2, now, "no description");
	}

	public LocalDateTime getDate() {
		return date.truncatedTo(ChronoUnit.MILLIS);
	}

	public String getDescription() {
		return description;
	}

	public double getAmount() {
		if (state == WorkOrderState.ASSIGNED || state == WorkOrderState.OPEN) {
			return 0.0;
		}
		return interventions.stream().mapToDouble(i -> i.getAmount()).sum();
	}

	public WorkOrderState getState() {
		return state;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public Mechanic getMechanic() {
		return mechanic;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	@Override
	public String toString() {
		return "WorkOrder{" + "date=" + date + ", description='" + description
				+ '\'' + ", amount=" + amount + ", state=" + state + ", vehicle="
				+ vehicle + ", mechanic=" + mechanic + ", invoice=" + invoice + '}';
	}

	/**
	 * Changes it to INVOICED state given the right conditions This method is
	 * called from Invoice.addWorkOrder(...)
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not FINISHED, or - The
	 *                               work order is not linked with the invoice
	 */
	public void markAsInvoiced() {
		if (state != WorkOrderState.FINISHED) {
			throw new IllegalStateException(
					"The work order is not in FINISHED state");
		}
		if (invoice == null) {
			throw new IllegalStateException(
					"The work order is not linked with the invoice");
		}
		this.state = WorkOrderState.INVOICED;
	}

	/**
	 * Given the right conditions unlinks the workorder and the mechanic, changes
	 * the state to FINISHED and computes the amount
	 *
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in ASSIGNED state,
	 *                               or
	 */
	public void markAsFinished() {
		if (state != WorkOrderState.ASSIGNED) {
			throw new IllegalStateException(
					"The work order is not in ASSIGNED state");
		}
		this.state = WorkOrderState.FINISHED;
		double total = 0.0;
		for (Intervention i : interventions) {
			total += i.getAmount();
		}
		this.amount = total;
		Associations.Assigns.unlink(this.mechanic, this);
	}

	/**
	 * Changes it back to FINISHED state given the right conditions This method is
	 * called from Invoice.removeWorkOrder(...)
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not INVOICED, or
	 */
	public void markBackToFinished() {
		if (state != WorkOrderState.INVOICED) {
			throw new IllegalStateException(
					"The work order is not in INVOICED state");
		}
		state = WorkOrderState.FINISHED;
	}

	/**
	 * Links (assigns) the work order to a mechanic and then changes its state to
	 * ASSIGNED
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in OPEN state, or
	 */
	public void assignTo(Mechanic mechanic) {
		ArgumentChecks.isNotNull(mechanic, "The mechanic to assign cannot be null");
		if (state != WorkOrderState.OPEN) {
			throw new IllegalStateException("The work order is not in OPEN state");
		}
		Associations.Assigns.link(mechanic, this);
		this.state = WorkOrderState.ASSIGNED;
	}

	/**
	 * Unlinks (deassigns) the work order and the mechanic and then changes its
	 * state back to OPEN
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in ASSIGNED state
	 */
	public void unassign() {
		if (state != WorkOrderState.ASSIGNED) {
			throw new IllegalStateException(
					"The work order is not in ASSIGNED state");
		}
		Associations.Assigns.unlink(this.mechanic, this);
		this.state = WorkOrderState.OPEN;
	}

	/**
	 * In order to assign a work order to another mechanic it first have to be
	 * moved back to OPEN state.
	 * 
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if - The work order is not in FINISHED state
	 */
	public void reopen() {
		if (state != WorkOrderState.FINISHED) {
			throw new IllegalStateException(
					"The work order is not in FINISHED state");
		}
		this.state = WorkOrderState.OPEN;
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>(interventions);
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}

	void _setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public boolean isFinished() {
		return state == WorkOrderState.FINISHED;
	}

	public boolean isAssigned() {
		return mechanic != null && state == WorkOrderState.ASSIGNED;
	}

	public boolean isOpen() {
		return state == WorkOrderState.OPEN;
	}

	public boolean isInvoiced() {
		return state == WorkOrderState.INVOICED;
	}

}
