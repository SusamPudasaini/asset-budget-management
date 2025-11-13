package com.project.budget.controller;

import com.project.budget.config.CustomUserDetails;

import com.project.budget.service.FeaturesService;
import com.project.budget.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    @Autowired
    private FeaturesService featuresService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Get username
        String username = auth.getName(); 

        // Get the principal object
        Object principal = auth.getPrincipal();
        String department = "defaultDepartment"; // default value

        // If using custom UserDetails with department field
        if (principal instanceof CustomUserDetails) {  
            department = ((CustomUserDetails) principal).getDepartment();
        }

        // Add to model
        model.addAttribute("username", username);
        model.addAttribute("department", department);


        return "dashboard";
    }

    @PostMapping("/dashboard/toggleAddUser")
    public String toggleAddUser(@RequestParam(value = "active", required = false) String active) {
        boolean isActive = (active != null && active.equals("true"));
        featuresService.updateFeatureStatus("Add User", isActive);

        return "redirect:/dashboard";
    }
}
