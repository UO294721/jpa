package uo.ri.cws.application.service.contracttype.crud.command;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContractType implements Command<Void> {

	private String name;
	private ContractTypeRepository repo = Factories.repository.forContractType();

	public DeleteContractType(String name) {
		ArgumentChecks.isNotEmpty(name);
		ArgumentChecks.isNotBlank(name);
		this.name = name;
	}

	@Override
	public Void execute() throws BusinessException {

		ContractType type = repo.findByName(name)
				.orElseThrow(() -> new BusinessException("Contract type not found"));

		BusinessChecks.isTrue(type.getContracts().isEmpty(),
				"Cannot delete contract type with associated contracts");

		repo.remove(type);

		return null;
	}

}
