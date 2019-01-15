package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());
        return "menu/index";
    }

    @GetMapping("view")
    public String viewMenu(Model model, @RequestParam int id) {
        Menu menu = menuDao.findOne(id);
        model.addAttribute("title", "Menu: " + menu.getName());
        model.addAttribute("menu", menu);
        return "menu/view";
    }


    @GetMapping("add")
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add a Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @PostMapping("add")
    public String processAddMenuForm(Model model, @ModelAttribute @Valid Menu newMenu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add a Menu");
            return "menu/add";
        } else {
            menuDao.save(newMenu);
            return "redirect:view?id=" + newMenu.getId();
        }

    }

    @GetMapping("add-item")
    public String displayAddMenuItemForm(Model model, @RequestParam int id) {
        Menu menu = menuDao.findOne(id);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        model.addAttribute("title", "Add Cheeses to Menu: " + menu.getName());
        model.addAttribute("form", new AddMenuItemForm(menu, cheeses));
        return "menu/add-item";

    }

    @PostMapping("add-item")
    public String processAddMenuItemForm(Model model, @RequestParam int id, @Valid AddMenuItemForm form,
                                         Errors errors)   {
       Menu menu = menuDao.findOne(id);
       Cheese newCheese = cheeseDao.findOne(form.getCheeseId());
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheeses to Menu: " + menu.getName());
            model.addAttribute("form", form);
            return "menu/add-item";
         } else {
                menu.addItem(newCheese);
                menuDao.save(menu);
                return "redirect:/menu/view?id=" + id;
            }
        }

    @GetMapping(value = "remove")
    public String displayRemoveCheeseFromMenuForm(Model model, @RequestParam int id) {
        Menu menu = menuDao.findOne(id);
        model.addAttribute("cheeses", menu.getCheeses());
        model.addAttribute("title", "Remove Cheeses from " + menu.getName());
        return "menu/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseFromMenuForm(@RequestParam int id, @RequestParam int[] cheeseIds) {

        Menu menu = menuDao.findOne(id);

        for (int cheeseId : cheeseIds) {
            Cheese theCheese = cheeseDao.findOne(cheeseId);
            menu.removeItem(theCheese);
        }

        menuDao.save(menu);
        return "redirect:view?id=" + menu.getId();
    }

    }


