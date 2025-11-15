package uo.ri.cws.domain;

public class Associations {

	public static class Owns {

		public static void link(Client client, Vehicle vehicle) {
			vehicle._setClient(client);
			client._getVehicles().add(vehicle);
		}

		public static void unlink(Client cliente, Vehicle vehicle) {
			cliente._getVehicles().remove(vehicle);
			vehicle._setClient(null);
		}

	}

	public static class Classifies {

		public static void link(VehicleType vehicleType, Vehicle vehicle) {
			vehicle._setVehicleType(vehicleType);
			vehicleType._getVehicles().add(vehicle);
		}

		public static void unlink(VehicleType tipoVehicle, Vehicle vehicle) {
			tipoVehicle._getVehicles().remove(vehicle);
			vehicle._setVehicleType(null);
		}
	}

	public static class Holds {

		public static void link(PaymentMean mean, Client client) {
			mean._setClient(client);
			client._getPaymentMeans().add(mean);
		}

		public static void unlink(Client client, PaymentMean mean) {
			client._getPaymentMeans().remove(mean);
			mean._setClient(null);
		}
	}

	public static class Fixes {

		public static void link(Vehicle vehicle, WorkOrder workOrder) {
			workOrder._setVehicle(vehicle);
			vehicle._getWorkOrders().add(workOrder);
		}

		public static void unlink(Vehicle vehicle, WorkOrder workOrder) {
			vehicle._getWorkOrders().remove(workOrder);
			workOrder._setVehicle(null);
		}
	}

	public static class Bills {

		public static void link(Invoice invoice, WorkOrder workOrder) {
			workOrder._setInvoice(invoice);
			invoice._getWorkOrders().add(workOrder);
		}

		public static void unlink(Invoice invoice, WorkOrder workOrder) {
			invoice._getWorkOrders().remove(workOrder);
			workOrder._setInvoice(null);
		}
	}

	public static class Settles {

		public static void link(Invoice invoice, Charge cargo, PaymentMean mp) {
			cargo._setInvoice(invoice);
			cargo._setPaymentMean(mp);

			invoice._getCharges().add(cargo);
			mp._getCharges().add(cargo);
		}

		public static void unlink(Charge cargo) {
			cargo._getInvoice()._getCharges().remove(cargo);
			cargo._getPaymentMean()._getCharges().remove(cargo);

			cargo._setInvoice(null);
			cargo._setPaymentMean(null);
		}
	}

	public static class Assigns {

		public static void link(Mechanic mechanic, WorkOrder workOrder) {
			workOrder._setMechanic(mechanic);
			mechanic._getWorkOrders().add(workOrder);
		}

		public static void unlink(Mechanic mechanic, WorkOrder workOrder) {
			mechanic._getWorkOrders().remove(workOrder);
			workOrder._setMechanic(null);
		}
	}

	public static class Intervenes {

		public static void link(WorkOrder workOrder, Intervention intervention,
				Mechanic mechanic) {
			intervention._setWorkOrder(workOrder);
			intervention._setMechanic(mechanic);

			workOrder._getInterventions().add(intervention);
			mechanic._getInterventions().add(intervention);
		}

		public static void unlink(Intervention intervention) {
			intervention._getMechanic()._getInterventions().remove(intervention);
			intervention._getWorkOrder()._getInterventions().remove(intervention);

			intervention._setWorkOrder(null);
			intervention._setMechanic(null);
		}
	}

	public static class Substitutes {

		public static void link(SparePart sparePart, Substitution substitution,
				Intervention intervention) {
			substitution._setIntervention(intervention);
			substitution._setSparePart(sparePart);

			sparePart._getSubstitutions().add(substitution);
			intervention._getSubstitutions().add(substitution);
		}

		public static void unlink(Substitution substitution) {
			substitution._getSparePart()._getSubstitutions().remove(substitution);
			substitution._getIntervention()._getSubstitutions().remove(substitution);

			substitution._setIntervention(null);
			substitution._setSparePart(null);
		}
	}

	public static class Binds {

		public static void link(Mechanic mechanic, Contract contract) {
			contract._setMechanic(mechanic);
			mechanic._getContracts().add(contract);
			mechanic._setInforceContract(contract);
		}

		public static void unlink(Contract contract) {
			contract.getMechanic()._getContracts().remove(contract);
			contract.getMechanic()._setInforceContract(null);
			contract._setMechanic(null);
		}
	}

	public static class Defines {

		public static void link(ContractType contractType, Contract contract) {
			contract._setContractType(contractType);
			contractType._getContracts().add(contract);
		}

		public static void unlink(Contract contract) {
			contract.getContractType()._getContracts().remove(contract);
			contract._setContractType(null);
		}
	}

	public static class Categorizes {

		public static void link(ProfessionalGroup professionalGroup,
				Contract contract) {
			contract._setProfessionalGroup(professionalGroup);
			professionalGroup._getContracts().add(contract);
		}

		public static void unlink(Contract contract) {
			contract.getProfessionalGroup()._getContracts().remove(contract);
			contract._setProfessionalGroup(null);
		}
	}

	public static class Generates {

		public static void link(Contract contract, Payroll payroll) {
			payroll._setContract(contract);
			contract._getPayrolls().add(payroll);
		}

		public static void unlink(Payroll payroll) {
			payroll._getContract()._getPayrolls().remove(payroll);
			payroll._setContract(null);
		}
	}

}
