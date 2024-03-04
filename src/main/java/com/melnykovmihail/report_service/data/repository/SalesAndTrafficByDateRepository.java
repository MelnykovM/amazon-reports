package com.melnykovmihail.report_service.data.repository;

import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SalesAndTrafficByDateRepository extends MongoRepository<SalesAndTrafficByDate, String> {

    Optional<SalesAndTrafficByDate> findByDate(Date date);

    @Query("{'date' : { $gte: ?0, $lte: ?1 } }")
    List<SalesAndTrafficByDate> findByDateBetween(Date start, Date end);
}
