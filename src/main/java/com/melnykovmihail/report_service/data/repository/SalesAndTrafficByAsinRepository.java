package com.melnykovmihail.report_service.data.repository;

import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SalesAndTrafficByAsinRepository extends MongoRepository<SalesAndTrafficByAsin, String> {

    Optional<SalesAndTrafficByAsin> findByParentAsin(String parentAsin);

    List<SalesAndTrafficByAsin> findByParentAsinIn(List<String> parentAsins);
}
