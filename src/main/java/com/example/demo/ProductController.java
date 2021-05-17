package com.example.demo;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class ProductController {

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("products", null);
        return "index";
    }

    @RequestMapping("/scrape")
    public String home(@RequestParam String category, @RequestParam String country, Model model) {
        var url = "https://www.ebay." + country + "/deals/trending" + category;
        var products = new ArrayList<Product>();

        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(true);
            final HtmlPage page = webClient.getPage(url);

            var cols = page.querySelectorAll(".col");
            for (var col : cols) {
                var product = new Product();
                product.setName(col.querySelector(".ebayui-ellipsis-2").getTextContent());
                product.setPrice(col.querySelector(".first").getTextContent());
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("products", products);
        return "index";
    }

}
