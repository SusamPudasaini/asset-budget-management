package com.project.budget.controller;

import com.project.budget.entity.FiscalEntity;
import com.project.budget.repository.FiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/fiscal")
public class FiscalController {

    @Autowired
    private FiscalRepository fiscalRepository;

    // ✅ Show all fiscals (fiscal-list.html)
    @GetMapping("/all")
    public String viewAllFiscals(Model model) {
        model.addAttribute("fiscals", fiscalRepository.findAllByOrderByStartDateDesc());
        return "fiscal-list";
    }

    // ✅ Show add fiscal form (last 5 fiscals)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("fiscal", new FiscalEntity());
        model.addAttribute("recentFiscals", fiscalRepository.findTop5ByOrderByStartDateDesc());
        return "fiscal-form";
    }

    // ✅ Handle add
    @PostMapping("/add")
    public String addFiscal(@ModelAttribute("fiscal") FiscalEntity fiscal) {
        fiscalRepository.save(fiscal);
        return "redirect:/fiscal/all";
    }

    // ✅ Show edit fiscal form
    @GetMapping("/edit/{fiscalId}")
    public String showEditForm(@PathVariable("fiscalId") Long fiscalId, Model model) {
        Optional<FiscalEntity> fiscal = fiscalRepository.findById(fiscalId);
        if (fiscal.isPresent()) {
            model.addAttribute("fiscal", fiscal.get());
            model.addAttribute("recentFiscals", fiscalRepository.findTop5ByOrderByStartDateDesc());
            return "fiscal-form";
        } else {
            return "redirect:/fiscal/all";
        }
    }

    // ✅ Handle update
    @PostMapping("/edit/{fiscalId}")
    public String updateFiscal(@PathVariable("fiscalId") Long fiscalId,
                               @ModelAttribute("fiscal") FiscalEntity updatedFiscal) {
        fiscalRepository.findById(fiscalId).ifPresent(fiscal -> {
            fiscal.setFiscalYear(updatedFiscal.getFiscalYear());
            fiscal.setStartDate(updatedFiscal.getStartDate());
            fiscal.setEndDate(updatedFiscal.getEndDate());
            fiscalRepository.save(fiscal);
        });
        return "redirect:/fiscal/all";
    }

    // ✅ Show Delete Confirmation
    @GetMapping("/confirm-delete/{fiscalId}")
    public String confirmDeleteFiscal(@PathVariable("fiscalId") Long fiscalId, Model model,
                                      RedirectAttributes redirectAttributes) {
        Optional<FiscalEntity> fiscalOpt = fiscalRepository.findById(fiscalId);
        if (fiscalOpt.isPresent()) {
            model.addAttribute("fiscal", fiscalOpt.get());
            return "fiscal-delete-confirmation";
        } else {
            redirectAttributes.addFlashAttribute("error", "Fiscal not found.");
            return "redirect:/fiscal/all";
        }
    }

    // ✅ Handle Deletion
    @GetMapping("/delete/{fiscalId}")
    public String deleteFiscal(@PathVariable("fiscalId") Long fiscalId,
                               RedirectAttributes redirectAttributes) {
        try {
            fiscalRepository.deleteById(fiscalId);
            redirectAttributes.addFlashAttribute("success", "Fiscal year deleted successfully.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete Fiscal: it is referenced by other records.");
        }
        return "redirect:/fiscal/all";
    }
}
