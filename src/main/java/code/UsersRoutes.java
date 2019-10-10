package code;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import testDB.UsersEntity;

import javax.persistence.NoResultException;

@Path
public class UsersRoutes {

    private Utils utils = new Utils();
    private static Logger log = Utils.getLog();

    @GetRoute("/login")
    public String login(@Param String message) {
        return "login";
    }

    @GetRoute("/logout")
    public void logout(Request request, Response response) {
        UsersEntity user = request.session().attribute("user");

        log.info("[LOGIN] Logout: user " + user.getUslogin());
        request.session().remove("user");
        response.redirect("/login");
    }

    @PostRoute("/login")
    public void login(@Param String login, @Param String password, Request request, Response response) {
        Session session = utils.getDBSession();
        Query userQuery = utils.getDBSession().createQuery("from testDB.UsersEntity where uslogin = :login AND uspassword = :password");

        userQuery.setParameter("login", login);
        userQuery.setParameter("password", password);

        try {
            UsersEntity user = (UsersEntity) userQuery.getSingleResult();
            request.session().attribute("user", user);

            log.info("[LOGIN] Login success: user " + user.getUslogin());

            response.redirect("/");
        } catch (NoResultException e) {
            log.error("[LOGIN] Login failed: " + login + " Password: " + password);
            request.session().attribute("message", "Авторизация не удалась");
            response.redirect("/login");
        }
        session.close();
    }

}
