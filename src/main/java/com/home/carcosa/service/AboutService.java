package com.home.carcosa.service;

import com.home.carcosa.dto.AboutResponse;
import org.springframework.stereotype.Service;

@Service
public class AboutService{

    public AboutResponse getAboutResponse(){
        return new AboutResponse(
                "Carcosa is a fictional, decadent, and metaphysically unstable city associated with the mythos of the The King in Yellow.");
    }
}
