package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSpareParts")
public class SparePart extends BaseEntity {
	// natural attributes
	@Column(unique = true)
	private String code;
	private String description;
	private double price;
	private int stock;
	private int minStock;
	private int maxStock;

	// accidental attributes
	@OneToMany(mappedBy = "sparePart")
	private final Set<Substitution> substitutions = new HashSet<>();

	SparePart() {

	}

	public SparePart(String code, String description, double price, int stock,
			int minStock, int maxStock) {
		ArgumentChecks.isNotBlank(code,
				"The spare part's code cannot be " + "blank");
		ArgumentChecks.isNotBlank(description,
				"The spare part's description " + "cannot be blank");
		ArgumentChecks.isTrue(price > 0,
				"The spare part's price must be " + "positive");
		ArgumentChecks.isTrue(stock >= 0,
				"The spare part's stock must be " + "non-negative");
		ArgumentChecks.isTrue(minStock >= 0,
				"The spare part's minStock must " + "be non-negative");
		ArgumentChecks.isTrue(maxStock >= 0,
				"The spare part's maxStock must " + "be non-negative");
		ArgumentChecks.isTrue(maxStock >= minStock,
				"The spare part's " + "maxStock must be greater than minStock");

		this.code = code;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.minStock = minStock;
		this.maxStock = maxStock;
	}

	public SparePart(String string, String string2, double d) {
		this(string, string2, d, 0, 0, 0);
	}

	public SparePart(String string) {
		this(string, "no description", 1.0, 0, 0, 0);
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>(substitutions);
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public int getMinStock() {
		return minStock;
	}

	public int getMaxStock() {
		return maxStock;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SparePart sparePart = (SparePart) o;
		return Objects.equals(code, sparePart.code);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(code);
	}

	@Override
	public String toString() {
		return "SparePart{" + "code='" + code + '\'' + ", description='"
				+ description + '\'' + ", price=" + price + ", stock=" + stock
				+ ", minStock=" + minStock + ", maxStock=" + maxStock + '}';
	}

}
