package com.lazy.springbooks.controller;

import com.lazy.springbooks.forms.BookForm;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import com.lazy.springbooks.model.Book;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    private static List<Book> books=new ArrayList<Book>();
    static {
        books.add(new Book("Full Stack Development with JHipster","Deepu K Sasidharan,Sendil Kumar N"));
        books.add(new Book("Pro Spring Security","Carlo Scarioni, Massimo Nardone"));
    }

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value={"/","/index"}, method = RequestMethod.GET)
    public ModelAndView index(Model model){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);

        return modelAndView;
    }
    @RequestMapping(value = {"/allbooks"}, method = RequestMethod.GET)
    public  ModelAndView personList(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        model.addAttribute("books", books);
        return modelAndView;
    }

    @RequestMapping(value = "/addbook", method = RequestMethod.GET)
    public ModelAndView ShowAddListPage(Model model){
        ModelAndView modelAndView = new ModelAndView("addbook");
        BookForm booksForm = new BookForm();
        model.addAttribute("bookform", booksForm);
        return modelAndView;
    }
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public ModelAndView ActionAddListPage(Model model, @ModelAttribute("bookform") BookForm booksForm){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        String title = booksForm.getTitle();
        String author = booksForm.getAuther();

        if(title != null && title.length()>0 && author != null && author.length()>0){
            Book book = new Book(title, author);
            books.add(book);
            model.addAttribute("books", books);
            return modelAndView;
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addbook");
        return modelAndView;
    }
    private int ind = 0;
    @GetMapping("{update}")
    public ModelAndView UpdateGetProduct(Model model, @ModelAttribute("productUpdate") Book book){
        ModelAndView modelAndView = new ModelAndView("update");
        BookForm bookForm = new BookForm();
        bookForm.setTitle(book.getTitle());
        bookForm.setAuther(book.getAuther());
        ind = books.indexOf(book);//почему -1?
        model.addAttribute("bookform", bookForm);
        return  modelAndView;
    }

    @PostMapping("{update}")
    public ModelAndView UpdatePostProduct(Model model, @ModelAttribute("productUpdate") Book book){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        String title = book.getTitle();
        String author = book.getAuther();
        //ind  = books.indexOf(book);
        if(title != null && title.length()>0 && author != null && author.length()>0){
            books.get(ind).setTitle(title);
            books.get(ind).setAuther(author);
            model.addAttribute("books", books);
            return  modelAndView;
        }
        model.addAttribute("booksList", books);
        return modelAndView;
    }
    @GetMapping("/delete/{name}")
    public String DeleteProduct(@PathVariable(value = "name") String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        Book cardelete = books.
                stream()
                .filter(x -> x.getTitle().equals(name)).findFirst().orElse(null);

        books.remove(cardelete);
        return "redirect:/allbooks";
    }
}
