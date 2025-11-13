package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class PayrollJpaRepository extends BaseJpaRepository<Payroll>
		implements PayrollRepository {

	@Override
	public List<Payroll> findByContract(String contractId) {
		return Jpa.getManager()
				.createNamedQuery("Payroll.findByContract", Payroll.class)
				.setParameter(1, contractId).getResultList();
	}

	@Override
	public List<Payroll> findLastMonthPayrolls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Payroll> findLastPayrollByMechanicId(String mechanicId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Payroll> findByProfessionalGroupName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Payroll> findByMechanicId(String mId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Payroll> findByContractIdAndDate(String id, LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

}
