package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractTypeByName
		implements Command<Optional<ContractTypeDto>> {

	private String name;
	private ContractTypeRepository repo = Factories.repository.forContractType();

	public FindContractTypeByName(String name) {
		ArgumentChecks.isNotEmpty(name, "The contract type name cannot be empty");
		ArgumentChecks.isNotBlank(name, "The contract type name cannot be null");
		this.name = name;
	}

	@Override
	public Optional<ContractTypeDto> execute() throws BusinessException {

		return repo.findByName(name).map(ct -> DtoAssembler.toDto(ct));
	}

}
