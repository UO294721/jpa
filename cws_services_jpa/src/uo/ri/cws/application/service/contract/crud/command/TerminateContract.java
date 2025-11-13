package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class TerminateContract implements Command<Void> {

	private String contractId;
	private ContractRepository repo = Factories.repository.forContract();

	public TerminateContract(String contractId) {
		ArgumentChecks.isNotEmpty(contractId, "The contract id cannot be null");
		ArgumentChecks.isNotBlank(contractId, "The contract id cannot be blank");
		this.contractId = contractId;
	}

	@Override
	public Void execute() throws BusinessException {

		Optional<Contract> ocontract = repo.findById(contractId);
		BusinessChecks.exists(ocontract, "The contract does not exist");
		Contract contract = ocontract.get();

		BusinessChecks.isTrue(!contract.isTerminated(),
				"The contract is already terminated");

		contract.terminate(LocalDate.now());

		return null;
	}

}
