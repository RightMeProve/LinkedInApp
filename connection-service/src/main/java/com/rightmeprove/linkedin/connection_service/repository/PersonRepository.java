package com.rightmeprove.linkedin.connection_service.repository;

import com.rightmeprove.linkedin.connection_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person,Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (personA:personB) -[:CONNECTED_TO]- (personB:personA) " +
    "WHERE personA.userId = %userId" +
    "RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

}
