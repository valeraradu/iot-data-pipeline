package com.iot.app.springboot.dao;

import com.iot.app.springboot.dao.entity.POITrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface POITrafficDataRepository extends CassandraRepository<POITrafficData, Date> {

}
