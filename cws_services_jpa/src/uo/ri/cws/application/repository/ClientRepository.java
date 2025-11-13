package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.Client;

public interface ClientRepository extends Repository<Client> {

	Optional<Client> findByNif(String nif);
}
