package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

	private List<String> workOrderIds;
	private WorkOrderRepository wrkrsRepo = Factories.repository.forWorkOrder();
	private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull(workOrderIds, "The work order ids cannot be null");
		ArgumentChecks.isFalse(workOrderIds.isEmpty(),
				"The work order ids cannot be empty");
		ArgumentChecks.isFalse(
				workOrderIds.stream().anyMatch(id -> id == null || id.isBlank()),
				"The work order ids cannot contain null or empty values");
		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		Long number = invoiceRepo.getNextInvoiceNumber();
		List<WorkOrder> workorders = wrkrsRepo.findByIds(workOrderIds);

		BusinessChecks.isTrue(workOrderIds.size() == workorders.size(),
				"Some work orders do not exist");
		BusinessChecks.isTrue(allFinished(workorders),
				"All work orders must be finished before invoicing");

		Invoice i = new Invoice(number, workorders);
		invoiceRepo.add(i);

		return DtoAssembler.toDto(i);
	}

	private boolean allFinished(List<WorkOrder> workorders) {
		return workorders.stream().allMatch(wo -> wo.isFinished());
	}

}
