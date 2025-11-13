package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSUBSTITUTIONS", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "SPAREPART_ID", "INTERVENTION_ID" }) })
public class Substitution extends BaseEntity {
	// natural attributes
	private int quantity;

	// accidental attributes
	@ManyToOne
	private SparePart sparePart;
	@ManyToOne
	private Intervention intervention;

	Substitution() {

	}

	public Substitution(int quantity, SparePart sparePart,
			Intervention intervention) {
		ArgumentChecks.isTrue(quantity > 0,
				"The substitution's quantity must be positive");
		ArgumentChecks.isNotNull(sparePart,
				"The substitution's spare part cannot be null");
		ArgumentChecks.isNotNull(intervention,
				"The substitution's intervention cannot be null");

		this.quantity = quantity;
		Associations.Substitutes.link(sparePart, this, intervention);
	}

	public Substitution(SparePart sparePart, Intervention intervention,
			int quantity) {
		this(quantity, sparePart, intervention);
	}

	public int getQuantity() {
		return quantity;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public SparePart getSparePart() {
		return sparePart;
	}

	SparePart _getSparePart() {
		return sparePart;
	}

	Intervention _getIntervention() {
		return intervention;
	}

	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}

	public double getAmount() {
		return quantity * sparePart.getPrice();
	}

}
