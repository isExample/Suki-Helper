package com.example.suki.api.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final BuildProperties buildProperties;

    @ModelAttribute("appVersion")
    public String getAppVersion() {
        return buildProperties.getVersion();
    }
}
