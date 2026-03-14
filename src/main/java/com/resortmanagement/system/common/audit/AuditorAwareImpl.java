/*
AuditorAwareImpl.java
Purpose:
 - Provide the current auditor (user) to JPA auditing.
Implementation:
 - Use Spring Security to return username:
   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   return Optional.ofNullable(auth != null ? auth.getName() : "SYSTEM");
Notes:
 - Do not call blocking code here.
 - Use "SYSTEM" as fallback.

File: common/audit/AuditorAwareImpl.java
*/

package com.resortmanagement.system.common.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // try {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated()) {
        // // If using a custom principal object, adapt here (e.g.,
        // ((UserDetails)authentication.getPrincipal()).getUsername())
        // return Optional.ofNullable(authentication.getName());
        // }
        // } catch (Exception ex) {
        // // never break auditing due to security context issues
        // }
        // // fallback
        return Optional.ofNullable("system_user");
    }

}