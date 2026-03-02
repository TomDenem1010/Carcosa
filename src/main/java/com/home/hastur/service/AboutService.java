package com.home.hastur.service;

import com.home.hastur.dto.AboutResponse;
import org.springframework.stereotype.Service;

@Service
public class AboutService{

    public AboutResponse getAboutResponse(){
        return new AboutResponse(
                "Hastur is a fictional character in the Cthulhu Mythos, created by writer Ambrose Bierce and later expanded upon by H.P. Lovecraft and other authors. Hastur is often depicted as a powerful and enigmatic entity associated with chaos, madness, and the unknown. The character has been featured in various works of fiction, including novels, short stories, and role-playing games, contributing to the rich tapestry of the Cthulhu Mythos.");
    }
}
