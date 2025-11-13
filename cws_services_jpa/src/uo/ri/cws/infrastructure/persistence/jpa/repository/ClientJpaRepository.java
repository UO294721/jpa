package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.domain.Client;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ClientJpaRepository extends BaseJpaRepository<Client>
		implements ClientRepository {

	@Override
	public Optional<Client> findByNif(String nif) {
		return Jpa.getManager().createNamedQuery("Client.findByNif", Client.class)
				.setParameter(1, nif).getResultList().stream().findFirst();
	}

}
