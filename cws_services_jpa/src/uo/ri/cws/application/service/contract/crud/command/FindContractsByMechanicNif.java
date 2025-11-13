package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractsByMechanicNif
		implements Command<List<ContractSummaryDto>> {

	private String nif;
	private ContractRepository repo = Factories.repository.forContract();
	private MechanicRepository mechanicRepo = Factories.repository.forMechanic();
	private PayrollRepository payrollRepo = Factories.repository.forPayroll();

	public FindContractsByMechanicNif(String nif) {
		ArgumentChecks.isNotEmpty(nif, "The mechanic NIF can't be empty");
		ArgumentChecks.isNotBlank(nif, "The mechanic NIF can't be blank");
		this.nif = nif;
	}

	@Override
	public List<ContractSummaryDto> execute() throws BusinessException {

		Optional<Mechanic> om = mechanicRepo.findByNif(nif);

		if (!om.isPresent()) {
			return List.of();
		}

		List<ContractSummaryDto> contracts = repo.findByMechanicId(om.get().getId())
				.stream().map(DtoAssembler::toContractSummaryDto).toList();

		contracts
				.forEach(c -> c.numPayrolls = payrollRepo.findByContract(c.id).size());

		return contracts;
	}

}
