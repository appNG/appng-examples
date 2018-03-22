package org.appng.application.example.camunda.repository;

import java.util.List;

import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.domain.Document.State;
import org.appng.persistence.repository.SearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends SearchRepository<Document, Integer> {

	List<Document> findByState(State state);

}
