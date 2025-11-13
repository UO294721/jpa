package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

	private MechanicDto dto;

	private MechanicRepository repo = Factories.repository.forMechanic();

	public AddMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "The mechanic dto cannot be null");
		ArgumentChecks.isNotEmpty(dto.nif, "The mechanic nif cannot be empty");
		ArgumentChecks.isNotEmpty(dto.name, "The mechanic name cannot be empty");
		ArgumentChecks.isNotEmpty(dto.surname,
				"The mechanic surname cannot be empty");
		this.dto = dto;
	}

	@Override
	public MechanicDto execute() throws BusinessException {

		checkNotRepeatedNif(dto.nif);

		Mechanic m = new Mechanic(dto.nif, dto.name, dto.surname);

		repo.add(m);

		return DtoAssembler.toDto(m);
	}

	private void checkNotRepeatedNif(String nif) throws BusinessException {
		Optional<Mechanic> existing = repo.findByNif(nif);
		BusinessChecks.doesNotExist(existing,
				"There is already a mechanic with the same NIF");
	}

}
