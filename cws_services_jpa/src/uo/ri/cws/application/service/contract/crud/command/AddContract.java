package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContract implements Command<ContractDto> {

	private ContractDto dto;
	private ContractRepository repo = Factories.repository.forContract();
	private MechanicRepository mechanicRepo = Factories.repository.forMechanic();
	private ContractTypeRepository contractTypeRepo = Factories.repository
			.forContractType();
	private ProfessionalGroupRepository professionalGroupRepo = Factories.repository
			.forProfessionalGroup();

	public AddContract(ContractDto c) {
		ArgumentChecks.isNotNull(c, "The contract dto cannot be null");
		ArgumentChecks.isNotNull(c.mechanic, "The mechanic cannot be null");
		ArgumentChecks.isNotEmpty(c.mechanic.nif,
				"The mechanic nif cannot be null");
		ArgumentChecks.isNotBlank(c.mechanic.nif,
				"The mechanic nif cannot be blank");
		ArgumentChecks.isNotNull(c.contractType,
				"The contract type cannot be null");
		ArgumentChecks.isNotNull(c.startDate,
				"The contract start date cannot be null");
		ArgumentChecks.isNotNull(c.professionalGroup,
				"The contract price per hour cannot be null");
		ArgumentChecks.isTrue(c.annualBaseSalary > 0,
				"The annual base salary cannot be negative");
		ArgumentChecks.isNotEmpty(c.contractType.name,
				"The contract type name cannot be null");
		ArgumentChecks.isNotBlank(c.contractType.name,
				"The contract type name cannot be blank");
		ArgumentChecks.isNotEmpty(c.professionalGroup.name,
				"The professional group name cannot be null");
		ArgumentChecks.isNotBlank(c.professionalGroup.name,
				"The professional group name cannot be blank");
		this.dto = c;
	}

	@Override
	public ContractDto execute() throws BusinessException {

		Optional<Mechanic> omech = mechanicRepo.findByNif(dto.mechanic.nif);
		BusinessChecks.exists(omech,
				"The mechanic for the contract does not exist");

		Mechanic mech = omech.get();

		Optional<ContractType> oct = contractTypeRepo
				.findByName(dto.contractType.name);
		BusinessChecks.exists(oct, "The contract type does not exist");

		ContractType ct = oct.get();

		Optional<ProfessionalGroup> opg = professionalGroupRepo
				.findByName(dto.professionalGroup.name);
		BusinessChecks.exists(opg, "The professional group does not exist");

		ProfessionalGroup pg = opg.get();

		validateEndDate();
		LocalDate startDate = dto.startDate.plus(1, ChronoUnit.MONTHS)
				.withDayOfMonth(1);

		Contract contract = new Contract(mech, ct, pg, startDate, dto.endDate,
				dto.annualBaseSalary);

		repo.add(contract);

		return DtoAssembler.toDto(contract);
	}

	private void validateEndDate() throws BusinessException {
		if (dto.endDate != null) {
			BusinessChecks.isTrue(
					dto.endDate.isAfter(dto.startDate)
							&& !dto.contractType.name.equals("PERMANENT"),
					"The contract end date must be after or equal to the start date");
		}
	}

}
