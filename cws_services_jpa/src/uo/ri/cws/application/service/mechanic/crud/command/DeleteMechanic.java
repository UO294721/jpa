package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {
	private MechanicRepository repo = Factories.repository.forMechanic();

	private String mechanicId;

	public DeleteMechanic(String mechanicId) {
		this.mechanicId = mechanicId;
	}

	@Override
	public Void execute() throws BusinessException {

		Optional<Mechanic> om = repo.findById(mechanicId);
		BusinessChecks.exists(om, "The mechanic does not exist");
		Mechanic m = om.get();

		BusinessChecks.isTrue(m.getInterventions().isEmpty(),
				"Cannot delete mechanic with interventions");
		BusinessChecks.isTrue(m.getAssigned().isEmpty(),
				"Cannot delete mechanic with assignments");
		BusinessChecks.isTrue(m.getContracts().isEmpty(),
				"Cannot delete mechanic with contracts");
		BusinessChecks.isTrue(m.getAssigned().isEmpty(),
				"Cannot delete mechanic with workorders");
		BusinessChecks.doesNotExist(m.getContractInForce(),
				"Cannot delete mechanic with a contract in force");

		repo.remove(m);

		return null;
	}

}
