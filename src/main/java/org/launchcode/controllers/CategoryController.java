package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.data.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping("")
    public String index(Model model) {
            model.addAttribute("title", "Categories");
            model.addAttribute("categories", categoryDao.findAll());

        return "category/index";
    }

    @GetMapping("add")
    public String displayAddForm(Model model) {
        model.addAttribute("title", "Add Category");
        model.addAttribute(new Category());
        return "category/add";
    }

    @PostMapping("add")
    public String processAddForm(Model model,
                                 @ModelAttribute @Valid Category newCategory,
                                 Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "category/add";
        } else {
            categoryDao.save(newCategory);
            return "redirect:";
        }
    }


}
