package code;

import com.blade.ioc.annotation.Bean;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.http.Session;
import org.slf4j.Logger;
import testDB.UsersEntity;

@Bean
public class GlobalHooks implements WebHook {

    private static Logger log = Utils.getLog();
    private RouteContext ctx;
    private UsersEntity user;

    @Override
    public boolean before(RouteContext ctx) {

        this.ctx = ctx;

        this.setMessage();
        this.checkLogin();
        this.logAction();

        return true;
    }

    private void setMessage()
    {
        ctx.request().attribute("message", ctx.request().session().attribute("message"));
        ctx.request().session().remove("message");
    }

    private void checkLogin()
    {
        String uri = ctx.request().uri();

        if (uri.equals("/login"))
        {
            return;
        }

        Session session = ctx.session();
        user = session.attribute("user");

        if ( user == null)
        {
            ctx.response().redirect("/login");
        }
        else {
            ctx.request().attribute("user", user );
        }
    }

    private void logAction()
    {
        String uri = ctx.request().uri();

        String[] actions = uri.split("/");
        String action = "";

        if (actions.length > 1)
        {
            action = actions[1];
        }

        switch (action) {
            case "addManToMat":
                log.info("[DATA] ["+user.getUslogin()+"] Добавление материала для производителя");
                break;
            case "addMatToMan":
                log.info("[DATA] ["+user.getUslogin()+"] Добавление производителя для материала");
                break;
            case "editMan":
                log.info("[DATA] ["+user.getUslogin()+"] Изменение производителя");
                break;
            case "editMat":
                log.info("[DATA] ["+user.getUslogin()+"] Изменение материала");
                break;
            case "addMat":
                log.info("[DATA] ["+user.getUslogin()+"] Добавление материала");
                break;
            case "addMan":
                log.info("[DATA] ["+user.getUslogin()+"] Добавление производителя");
                break;
            case "delMan":
                log.info("[DATA] ["+user.getUslogin()+"] Удаление производителя");
                break;
            case "delMat":
                log.info("[DATA] ["+user.getUslogin()+"] Удаление материала");
                break;
            case "massDelMat":
                log.info("[DATA] ["+user.getUslogin()+"] Массовое удаление материала");
                break;

        }
    }

}
