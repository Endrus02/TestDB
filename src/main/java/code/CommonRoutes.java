package code;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;

@Path
public class CommonRoutes {
        @GetRoute("/")
        public String index(){
        return "index";
    }
}
