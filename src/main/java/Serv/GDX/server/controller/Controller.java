package Serv.GDX.server.controller;

import Serv.GDX.server.desks.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    DeskService deskService;

    @GetMapping(value = "/setup",params = "size")
     public String setupSizeDesk(
            @RequestParam("size") int size) {
            deskService.SetupSizeDesk(size);
            return "DeskSize:"+size;
            }

    @GetMapping(value = "/setup",params = "count")
    public String setupFoodCount(
            @RequestParam("count") int count) {
                deskService.SetupCountFood(count);
                return "FoodCount:"+count;
            }

    @GetMapping(value = "/info")
    public String infoDesk() {
        String deskInfo=deskService.GetDeskInfo();
        return deskInfo;
    }

}