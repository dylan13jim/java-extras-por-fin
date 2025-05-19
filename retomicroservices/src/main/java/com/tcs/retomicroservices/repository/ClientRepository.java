package com.tcs.retomicroservices.repository;

import com.tcs.retomicroservices.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Puedes agregar métodos personalizados aquí si es necesario
}
