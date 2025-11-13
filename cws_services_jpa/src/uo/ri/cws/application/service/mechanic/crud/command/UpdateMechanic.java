package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

	private MechanicRepository repo = Factories.repository.forMechanic();

	private MechanicDto dto;

	public UpdateMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "The mechanic dto cannot be null");
		ArgumentChecks.isNotBlank(dto.name, "The mechanic name cannot be empty");
		ArgumentChecks.isNotBlank(dto.surname,
				"The mechanic surname cannot be empty");
		ArgumentChecks.isNotBlank(dto.nif, "The mechanic nif cannot be empty");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {

		checkNotRepeatedNif(dto.nif);

		Optional<Mechanic> om = repo.findById(dto.id);

		BusinessChecks.exists(om, "The mechanic does not exist");

		Mechanic m = om.get();
		BusinessChecks.hasVersion(m.getVersion(), dto.version,
				"The mechanic has been modified by another transaction");

		m.setName(dto.name);
		m.setSurname(dto.surname);

		return null;
	}

	private void checkNotRepeatedNif(String nif) throws BusinessException {
		Optional<Mechanic> existing = repo.findByNif(nif);
		if (existing.isPresent() && !existing.get().getId().equals(dto.id)) {
			throw new BusinessException(
					"There is already a mechanic with the same NIF");
		}
	}

}
