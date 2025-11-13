package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVEHICLETYPES")
public class VehicleType extends BaseEntity {
	// natural attributes
	@Column(unique = true)
	private String name;
	@Basic(optional = false)
	private double pricePerHour;

	// accidental attributes
	@OneToMany(mappedBy = "vehicletype")
	private final Set<Vehicle> vehicles = new HashSet<>();

	VehicleType() {
	}

	public VehicleType(String name, double pricePerHour) {
		ArgumentChecks.isNotBlank(name, "The vehicle type's name cannot be blank");
		ArgumentChecks.isTrue(pricePerHour >= 0,
				"The vehicle type's price per hour must be positive");
		this.name = name;
		this.pricePerHour = pricePerHour;
	}

	public VehicleType(String name2) {
		this(name2, 0.0);
	}

	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}

	Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	public String getName() {
		return name;
	}

	public double getPricePerHour() {
		return pricePerHour;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		VehicleType vehicleType = (VehicleType) o;
		return name.equals(vehicleType.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "VehicleType{" + "name='" + name + '\'' + ", pricePerHour="
				+ pricePerHour + '}';
	}

}
