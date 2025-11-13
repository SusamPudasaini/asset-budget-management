package com.project.budget.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import com.project.budget.config.CustomUserDetails;

@Controller
@RequestMapping("/software")
public class SourceCodeController {


	@GetMapping("/list")
	public String showSoftwareListPage(Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = auth.getPrincipal();

	    String username = auth.getName();
	    String department = "defaultDepartment";

	    if (principal instanceof CustomUserDetails) {
	        department = ((CustomUserDetails) principal).getDepartment();
	    }

	    model.addAttribute("username", username);
	    model.addAttribute("department", department); 
	    return "software-list";
	}
	
	@GetMapping("/deleted-list")
	public String showSoftwareDeletedListPage(Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = auth.getPrincipal();

	    String username = auth.getName();
	    String department = "defaultDepartment";

	    if (principal instanceof CustomUserDetails) {
	        department = ((CustomUserDetails) principal).getDepartment();
	    }

	    model.addAttribute("username", username);
	    model.addAttribute("department", department); 
	    return "software-deleted-list";
	}
    
    @GetMapping("/form")
    public String showSoftwareFormPage(Model model) {
    	
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = auth.getPrincipal();

	    String username = auth.getName();
	    String department = "defaultDepartment";

	    if (principal instanceof CustomUserDetails) {
	        department = ((CustomUserDetails) principal).getDepartment();
	    }

	    model.addAttribute("username", username);
	    model.addAttribute("department", department); 
        return "software-form";
    }
    
    @GetMapping("/details")
    public String showSoftwaredetailsPage() {
        return "software-details";
    }
    
    @GetMapping("/edit")
    public String showSoftwareEditPage() {
        return "software-form";
    }
    
    
}

