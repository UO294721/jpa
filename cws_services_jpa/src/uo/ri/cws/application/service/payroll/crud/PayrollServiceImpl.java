package uo.ri.cws.application.service.payroll.crud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.util.exception.BusinessException;

public class PayrollServiceImpl implements PayrollService {

	@Override
	public List<PayrollDto> generateForPreviousMonth() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayrollDto> generateForPreviousMonthOf(LocalDate present)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteLastGeneratedOfMechanicId(String mechanicId)
			throws BusinessException {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteLastGenerated() throws BusinessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<PayrollDto> findById(String id) throws BusinessException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<PayrollSummaryDto> findAllSummarized() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayrollSummaryDto> findSummarizedByMechanicId(String id)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayrollSummaryDto> findSummarizedByProfessionalGroupName(
			String name) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
