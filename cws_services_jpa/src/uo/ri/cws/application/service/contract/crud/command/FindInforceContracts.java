package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class FindInforceContracts implements Command<List<ContractDto>> {

	private ContractRepository repo = Factories.repository.forContract();

	@Override
	public List<ContractDto> execute() throws BusinessException {

		return repo.findInforceContracts().stream().map(DtoAssembler::toDto)
				.toList();
	}

}
