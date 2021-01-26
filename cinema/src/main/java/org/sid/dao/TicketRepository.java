package org.sid.dao;

import org.sid.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
