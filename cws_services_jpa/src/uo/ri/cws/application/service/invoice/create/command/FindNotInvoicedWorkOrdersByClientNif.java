package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrdersByClientNif
		implements Command<List<InvoicingWorkOrderDto>> {

	private String nif;
	private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
	private ClientRepository clientRepo = Factories.repository.forClient();

	public FindNotInvoicedWorkOrdersByClientNif(String nif) {
		ArgumentChecks.isNotEmpty(nif, "The client nif cant be null or empty");
		this.nif = nif;
	}

	@Override
	public List<InvoicingWorkOrderDto> execute() throws BusinessException {

		Optional<Client> client = clientRepo.findByNif(nif);
		if (!client.isPresent()) {
			return List.of();
		}

		Client m = client.get();

		List<WorkOrder> workOrder = invoiceRepo
				.findNotInvoicedWorkOrdersByClientId(m.getId());

		return DtoAssembler.toInvoicingWorkOrderDtoList(workOrder);
	}

}
