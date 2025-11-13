package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TINVOICES")
public class Invoice extends BaseEntity {
	public enum InvoiceState {
		NOT_YET_PAID, PAID
	}

	// natural attributes
	private Long number;
	private LocalDate date;
	private double amount;
	private double vat;
	@Enumerated(EnumType.STRING)
	private InvoiceState state = InvoiceState.NOT_YET_PAID;

	// accidental attributes
	@OneToMany(mappedBy = "invoice")
	private Set<WorkOrder> workOrders = new HashSet<>();
	@OneToMany(mappedBy = "invoice")
	private Set<Charge> charges = new HashSet<>();

	Invoice() {

	}

	public Invoice(Long number) {
		// call full constructor with sensible defaults
		this(number, LocalDate.now());
	}

	public Invoice(Long number, LocalDate date) {
		// call full constructor with sensible defaults
		this(number, date, List.of());
	}

	public Invoice(Long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	// full constructor
	public Invoice(Long number, LocalDate date, List<WorkOrder> workOrders) {
		// check arguments (always), through IllegalArgumentException
		// store the number
		// add every work order calling addWorkOrder( w )
		ArgumentChecks.isNotNull(number, "The invoice number cannot be null");
		ArgumentChecks.isNotNull(date, "The invoice date cannot be null");
		ArgumentChecks.isNotNull(workOrders,
				"The work orders list in the invoice cannot be null");
		for (WorkOrder w : workOrders) {
			ArgumentChecks.isNotNull(w,
					"The work orders in the invoice cannot be null");
		}
		this.number = number;
		this.date = date;
		for (WorkOrder w : workOrders) {
			addWorkOrder(w);
		}
		computeAmount();
	}

	/**
	 * Computes amount and vat (vat depends on the date)
	 */
	private void computeAmount() {
		this.amount = workOrders.stream().mapToDouble(WorkOrder::getAmount).sum();
		if (date.isBefore(LocalDate.of(2012, 7, 1))) {
			this.vat = amount * 0.18;
		} else {
			this.vat = amount * 0.21;
		}

	}

	/**
	 * Adds (double links) the workOrder to the invoice and updates the amount and
	 * vat
	 * 
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 * @throws IllegalStateException if the workorder status is not FINISHED
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		ArgumentChecks.isNotNull(workOrder, "The work order to add cannot be null");
		if (state != InvoiceState.NOT_YET_PAID) {
			throw new IllegalStateException(
					"Cannot add work order to a settled invoice");
		}
		if (!workOrder.isFinished()) {
			throw new IllegalStateException(
					"Cannot add a work order that is not finished");
		}
		Associations.Bills.link(this, workOrder);
		workOrder.markAsInvoiced();
		computeAmount();
	}

	/**
	 * Removes a work order from the invoice, updates the workorder state and
	 * recomputes amount and vat
	 * 
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException    if the invoice status is not NOT_YET_PAID
	 * @throws IllegalArgumentException if the invoice does not contain the
	 *                                  workorder
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		if (state != InvoiceState.NOT_YET_PAID) {
			throw new IllegalStateException(
					"Cannot remove work order from a settled invoice");
		}
		if (!workOrders.contains(workOrder)) {
			throw new IllegalArgumentException(
					"Cannot remove a work order that is not in the invoice");
		}
		Associations.Bills.unlink(this, workOrder);
		workOrder.markBackToFinished();
		computeAmount();
	}

	/**
	 * Marks the invoice as PAID, but
	 * 
	 * @throws IllegalStateException if - Is already settled - Or the amounts paid
	 *                               with charges to payment means do not cover
	 *                               the total of the invoice
	 */
	public void settle() {
		if (state == InvoiceState.PAID) {
			throw new IllegalStateException("The invoice is already settled");
		}
		double totalPaid = charges.stream().mapToDouble(Charge::getAmount).sum();
		if (totalPaid < getAmount()) {
			throw new IllegalStateException(
					"The total paid does not cover the invoice amount");
		}
		state = InvoiceState.PAID;

	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	public double getAmount() {
		return amount + vat;
	}

	public boolean isNotSettled() {
		return state == InvoiceState.NOT_YET_PAID;
	}

	public Long getNumber() {
		return number;
	}

	public double getBaseAmount() {
		return amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getVat() {
		return vat;
	}

	public InvoiceState getState() {
		return state;
	}

}
