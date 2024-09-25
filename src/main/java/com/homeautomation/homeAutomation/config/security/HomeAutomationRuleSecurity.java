//package com.homeautomation.homeAutomation.config.security;
//
//import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
////Security component to only output the linked user related rules
//@Component("homeAutomationRuleSecurity")
//public class HomeAutomationRuleSecurity {
//
//    @Autowired
//    private HomeAutomationRuleService homeAutomationRuleService;
//
//    public boolean isOwner(Long ruleId, Authentication authentication) {
//        String currentUsername = authentication.getName();
//        return homeAutomationRuleService.isOwner(ruleId, currentUsername);
//    }
//
//    public boolean isOwner(Long ruleId, Long deviceId, Authentication authentication) {
//        String currentUsername = authentication.getName();
//        return homeAutomationRuleService.isOwner(ruleId, deviceId, currentUsername);
//    }
//}
