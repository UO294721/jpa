package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContract implements Command<Void> {

	private ContractDto dto;
	private ContractRepository repo = Factories.repository.forContract();

	public UpdateContract(ContractDto dto) {
		ArgumentChecks.isNotNull(dto, "The contract dto cannot be null");
		ArgumentChecks.isNotEmpty(dto.id, "The contract id cannot be null");
		ArgumentChecks.isNotBlank(dto.id, "The contract id cannot be blank");
		ArgumentChecks.isNotNull(dto.mechanic, "The mechanic cannot be null");
		ArgumentChecks.isNotNull(dto.contractType,
				"The contract type cannot be null");
		ArgumentChecks.isNotNull(dto.startDate,
				"The contract start date cannot be null");
		ArgumentChecks.isNotNull(dto.professionalGroup,
				"The contract price per hour cannot be null");
		ArgumentChecks.isTrue(dto.annualBaseSalary > 0,
				"The annual base salary cannot be negative");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {

		Optional<Contract> contractOpt = repo.findById(dto.id);
		BusinessChecks.exists(contractOpt, "The contract does not exist");

		Contract contract = contractOpt.get();

		BusinessChecks.isTrue(!contract.isTerminated(),
				"The contract is terminated and cannot be updated");

		BusinessChecks.isTrue(
				dto.endDate == null || !dto.endDate.isBefore(contract.getStartDate()),
				"The contract start date cannot be after the end date");

		BusinessChecks.hasVersion(contract.getVersion(), dto.version,
				"The contract has been modified by another transaction");

		contract.setEndDate(dto.endDate);
		contract.setAnnualBaseSalary(dto.annualBaseSalary);

		return null;
	}

}
