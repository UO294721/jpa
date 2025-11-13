package uo.ri.cws.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import uo.ri.util.assertion.ArgumentChecks;

/**
 * This class is a Value Type, thus - no setters - hashcode and equals over all
 * attributes
 */
@Embeddable
public class Address implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4084258823563586458L;
	private String street;
	private String city;
	private String zipCode;

	public Address() {
	}

	public Address(String street, String city, String zipCode) {
		ArgumentChecks.isNotBlank(street, "The street cannot be blank");
		ArgumentChecks.isNotBlank(city, "The city cannot be blank");
		ArgumentChecks.isNotBlank(zipCode, "The zip code cannot be blank");

		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Address address = (Address) o;
		return Objects.equals(street, address.street)
				&& Objects.equals(city, address.city)
				&& Objects.equals(zipCode, address.zipCode);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(street);
		result = 31 * result + Objects.hashCode(city);
		result = 31 * result + Objects.hashCode(zipCode);
		return result;
	}
}
