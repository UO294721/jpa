package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVEHICLES")
public class Vehicle extends BaseEntity {

	@Column(unique = true)
	private String plateNumber;
	@Basic(optional = false)
	private String make;
	@Basic(optional = false)
	private String model;

	// accidental attributes
	@ManyToOne
	private Client client;
	@ManyToOne
	private VehicleType vehicletype;

	@OneToMany(mappedBy = "vehicle")
	private Set<WorkOrder> workOrders = new HashSet<>();

	Vehicle() {
	}

	public Vehicle(String plateNumber, String make, String model) {
		ArgumentChecks.isNotBlank(plateNumber,
				"The vehicle's plate number cannot be blank");
		ArgumentChecks.isNotBlank(make, "The vehicle's make cannot be blank");
		ArgumentChecks.isNotBlank(model, "The vehicle's model cannot be blank");

		this.plateNumber = plateNumber;
		this.make = make;
		this.model = model;
	}

	public Vehicle(String plateNumber2) {
		this(plateNumber2, "no-make", "no-model");
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public Client getClient() {
		return client;
	}

	void _setClient(Client client) {
		this.client = client;
	}

	public VehicleType getVehicleType() {
		return vehicletype;
	}

	void _setVehicleType(VehicleType type) {
		this.vehicletype = type;
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Vehicle vehicle = (Vehicle) o;
		return Objects.equals(plateNumber, vehicle.plateNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(plateNumber);
	}

	@Override
	public String toString() {
		return "Vehicle{" + "plateNumber='" + plateNumber + '\'' + ", make='" + make
				+ '\'' + ", model='" + model + '\'' + '}';
	}
}
