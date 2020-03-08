package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.DashboardDTO;
import com.codeit.survey.services.DashboardService;
import com.codeit.survey.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {
    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/dashboard")
    public DashboardDTO dashboard(@RequestHeader(name="Authorization") String token){
        return dashboardService.dashboard(token.substring(7));
    }


}
