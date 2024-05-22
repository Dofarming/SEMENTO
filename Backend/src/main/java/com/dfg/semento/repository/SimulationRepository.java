package com.dfg.semento.repository;

import com.dfg.semento.document.LogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SimulationRepository extends ElasticsearchRepository<LogDocument, String> {
}
