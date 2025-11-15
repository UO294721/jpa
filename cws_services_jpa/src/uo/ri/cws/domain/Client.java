package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCLIENTS")
public class Client extends BaseEntity {

	@Column(unique = true)
	private String nif;
	private String name;
	private String surname;

	private String email;
	private String phone;
	private Address address;

	@OneToMany(mappedBy = "client")
	private Set<Vehicle> vehicles = new HashSet<>();
	@OneToMany(mappedBy = "client")
	private Set<PaymentMean> paymentMeans = new HashSet<>();

	public Client(String nif, String name, String surname, String email,
			String phone, Address address) {

		ArgumentChecks.isNotBlank(nif, "The client's NIF cannot be blank");
		ArgumentChecks.isNotBlank(name, "The client's name cannot be blank");
		ArgumentChecks.isNotBlank(surname, "The client's surname cannot be blank");
		ArgumentChecks.isNotBlank(email, "The client's email cannot be blank");
		ArgumentChecks.isNotBlank(phone, "The client's phone cannot be blank");
		ArgumentChecks.isNotNull(address, "The client's address cannot be null");

		this.nif = nif;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	Client() {
	}

	public Client(String nif, String name, String surname) {
		this(nif, name, surname, "no-email", "no-phone",
				new Address("no-street", "no-city", "no-zipcode"));
	}

	public Client(String nif) {
		this(nif, "no-name", "no-surname", "no-email", "no-phone",
				new Address("no-street", "no-city", "no-zipcode"));
	}

	void _addVehicle(Vehicle vehicle) {
		vehicles.add(vehicle);
	}

	public String getNif() {
		return nif;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public Address getAddress() {
		return address;
	}

	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}

	Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	public Set<PaymentMean> getPaymentMeans() {
		return new HashSet<>(paymentMeans);
	}

	Set<PaymentMean> _getPaymentMeans() {
		return paymentMeans;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Client client = (Client) o;
		return nif.equals(client.nif);
	}

	@Override
	public int hashCode() {
		return nif.hashCode();
	}

	@Override
	public String toString() {
		return "Client{" + "nif='" + nif + '\'' + ", name='" + name + '\''
				+ ", surname='" + surname + '\'' + ", email='" + email + '\''
				+ ", phone='" + phone + '\'' + ", address=" + address + '}';
	}

	public void setAddress(Address address2) {
		this.address = address2;
	}
}
