package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContractType implements Command<Void> {

	private ContractTypeDto dto;
	private ContractTypeRepository repo = Factories.repository.forContractType();

	public UpdateContractType(ContractTypeDto dto) {
		ArgumentChecks.isNotNull(dto, "The contract type dto cannot be null");
		ArgumentChecks.isNotBlank(dto.name,
				"The contract type name cannot be empty");
		ArgumentChecks.isTrue(dto.compensationDays > 0,
				"The contract type monthly fee cannot be null");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {

		checkNotRepeatedName(dto.name);

		Optional<ContractType> oct = repo.findById(dto.id);

		BusinessChecks.exists(oct, "The contract type does not exist");

		ContractType ct = oct.get();
		BusinessChecks.hasVersion(ct.getVersion(), dto.version,
				"The contract type has been modified by another transaction");
		ct.setName(dto.name);
		ct.setCompensationDays(dto.compensationDays);

		return null;
	}

	private void checkNotRepeatedName(String name) throws BusinessException {
		Optional<ContractType> existing = repo.findByName(name);
		if (existing.isPresent() && !existing.get().getId().equals(dto.id)) {
			throw new BusinessException(
					"There is already a contract type with the same name");
		}
	}

}
