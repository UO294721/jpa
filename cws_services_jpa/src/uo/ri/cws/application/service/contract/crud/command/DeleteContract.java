package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.InterventionRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContract implements Command<Void> {

	private String id;
	private ContractRepository repo = Factories.repository.forContract();
	private MechanicRepository mechanicRepo = Factories.repository.forMechanic();
	private PayrollRepository payrollRepo = Factories.repository.forPayroll();
	private InterventionRepository interventionRepo = Factories.repository
			.forIntervention();

	public DeleteContract(String id) {
		ArgumentChecks.isNotEmpty(id, "The contract id can't be empty");
		ArgumentChecks.isNotBlank(id, "The contract id can't be blank");
		this.id = id;
	}

	@Override
	public Void execute() throws BusinessException {

		Optional<Contract> contractOpt = repo.findById(id);
		BusinessChecks.exists(contractOpt, "The contract does not exist");
		Contract contract = contractOpt.get();

		Optional<Mechanic> mechanicOpt = mechanicRepo
				.findById(contract.getMechanic().getId());
		BusinessChecks.exists(mechanicOpt,
				"The mechanic associated to the contract does not exist");
		Mechanic mechanic = mechanicOpt.get();

		BusinessChecks.isTrue(
				interventionRepo.findByMechanicId(mechanic.getId()).isEmpty(),
				"The contract's mechanic has associated interventions");

		BusinessChecks.isTrue(
				payrollRepo.findByContract(contract.getId()).isEmpty(),
				"The contract has associated payrolls");

		repo.remove(contract);
		return null;
	}

}
