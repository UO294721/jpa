package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;

public class FindAllMechanics implements Command<List<MechanicDto>> {
	private MechanicRepository repo = Factories.repository.forMechanic();

	@Override
	public List<MechanicDto> execute() {

		List<Mechanic> list = repo.findAll();

		return list.stream().map(DtoAssembler::toDto).toList();
	}

}
