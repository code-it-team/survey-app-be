package com.codeit.survey.services;

import com.codeit.survey.DTOs.DashboardDTO;
import com.codeit.survey.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private JwtUtil jwtUtil;

    public DashboardService(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    public DashboardDTO dashboard(String token){
        return new DashboardDTO(jwtUtil.extractUsername(token));
    }

}
