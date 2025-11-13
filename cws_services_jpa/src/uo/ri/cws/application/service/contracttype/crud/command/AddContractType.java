package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContractType implements Command<ContractTypeDto> {

	private ContractTypeDto dto;
	private ContractTypeRepository repo = Factories.repository.forContractType();

	public AddContractType(ContractTypeDto type) {
		ArgumentChecks.isNotNull(type, "The contract type dto cannot be null");
		ArgumentChecks.isNotBlank(type.name,
				"The contract type name cannot be empty");
		ArgumentChecks.isTrue(type.compensationDays > 0,
				"The contract type monthly salary must be positive");
		this.dto = type;
	}

	@Override
	public ContractTypeDto execute() throws BusinessException {

		checkNotRepeatedName(dto.name);

		ContractType ct = new ContractType(dto.name, dto.compensationDays);

		repo.add(ct);

		return DtoAssembler.toDto(ct);
	}

	private void checkNotRepeatedName(String name) throws BusinessException {
		Optional<ContractType> existing = repo.findByName(name);
		BusinessChecks.doesNotExist(existing,
				"There is already a contract type with the same name");
	}

}
