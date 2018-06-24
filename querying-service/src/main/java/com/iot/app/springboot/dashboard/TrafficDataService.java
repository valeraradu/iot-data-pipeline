package com.iot.app.springboot.dashboard;

import com.iot.app.springboot.dao.POITrafficDataRepository;
import com.iot.app.springboot.dao.TotalTrafficDataRepository;
import com.iot.app.springboot.dao.WindowTrafficDataRepository;
import com.iot.app.springboot.dao.entity.POITrafficData;
import com.iot.app.springboot.dao.entity.TotalTrafficData;
import com.iot.app.springboot.dao.entity.WindowTrafficData;
import com.iot.app.springboot.vo.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TrafficDataService {
    private static final Logger logger = Logger.getLogger(TrafficDataService.class);
    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private TotalTrafficDataRepository totalRepository;
    @Autowired
    private WindowTrafficDataRepository windowRepository;
    @Autowired
    private POITrafficDataRepository poiRepository;


    @Scheduled(fixedRate = 5000)
    public void trigger() {
        List<TotalTrafficData> totalTrafficList = new ArrayList<>();
        List<WindowTrafficData> windowTrafficList = new ArrayList<>();
        List<POITrafficData> poiTrafficList = new ArrayList<>();

        totalRepository.findTrafficDataByDate(sdf.format(new Date())).forEach(e -> totalTrafficList.add(e));
        windowRepository.findTrafficDataByDate(sdf.format(new Date())).forEach(e -> windowTrafficList.add(e));
        poiRepository.findAll().forEach(e -> poiTrafficList.add(e));

        Response response = new Response();
        response.setTotalTraffic(totalTrafficList);
        response.setWindowTraffic(windowTrafficList);
        response.setPoiTraffic(poiTrafficList);
        logger.info("Sending to UI " + response);

        this.template.convertAndSend("/topic/trafficData", response);
    }

}
