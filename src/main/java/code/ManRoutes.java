package code;

import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import testDB.ManufacturersEntity;
import testDB.MaterialsEntity;

import java.util.List;

@Path
public class ManRoutes {

    private Utils utils = new Utils();

    private List<ManufacturersEntity> search (String name) {
        Session session = utils.getDBSession();

        Query manQuery = session.createQuery("from testDB.ManufacturersEntity where manname LIKE :name ORDER BY manname");
        manQuery.setParameter("name", "%" + name + "%");

        return (List<ManufacturersEntity>) manQuery.list();
    }

    @GetRoute("/editMan/:id")
    public String editMan (@PathParam int id, Request request) {

        Session session = utils.getDBSession();

        ManufacturersEntity brand = session.get(ManufacturersEntity.class, id);
        request.attribute("brand", brand);
        request.attribute("materials", session.createCriteria(MaterialsEntity.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list());

        session.close();
        return "editMan";
    }

    @GetRoute("/addMatToMan/:matId/:manId")
    public void addMatToMan (@PathParam int matId, @PathParam int manId, Response response) {

        Session session = utils.getDBSession();
        session.beginTransaction();

        ManufacturersEntity brand = session.get(ManufacturersEntity.class, manId);
        MaterialsEntity material = session.get(MaterialsEntity.class, matId);

        brand.addMaterial(material);

        try {
            session.update(brand);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        response.redirect("/editMan/" + manId);
    }

    @GetRoute("/delMatFromMan/:matId/:manId")
    public void delMatFromMan (@PathParam int matId, @PathParam int manId, Response response) {

        Session session = utils.getDBSession();
        session.beginTransaction();

        ManufacturersEntity brand = session.get(ManufacturersEntity.class, manId);
        MaterialsEntity material = session.get(MaterialsEntity.class, matId);

        brand.removeMaterial(material);

        try {
            session.update(brand);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        response.redirect("/editMan/" + manId);
    }

    @PostRoute("/editMan")
    public void doEdit(@Param ManufacturersEntity brand, Response response, Request request) {

        if (brand.getInn().matches("^\\d{12}$")) {
            Session session = utils.getDBSession();
            session.beginTransaction();
            try {
                session.update(brand);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.out.println(e);
            }
            session.close();
        }
        else {
            request.session().attribute("message", "ИНН должен быть из 12 цифр");
        }
        response.redirect("/searchMan");
    }

    @GetRoute("/addMan")
    public String addMan(){
        return "addMan";
    }

    @PostRoute("/addMan")
    public void doAdd(@Param ManufacturersEntity brand, Response response, Request request) {

        if (brand.getInn().matches("^\\d{12}$")) {
            Session session = utils.getDBSession();
            session.beginTransaction();
            try {
                session.save(brand);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.out.println(e);
            }
            session.close();
         }
        else {
                request.session().attribute("message", "ИНН должен быть из 12 цифр");
            }
        response.redirect("/searchMan");
    }

    @GetRoute("/searchMan")
    public String searchMan(Request request){

        request.attribute("brands", this.search(""));
        return "searchMan";
    }

    @PostRoute("/searchMan")
    public String doSearch(@Param String name, Request request) {

        request.attribute("name", name);
        request.attribute("brands", this.search(name));

        return "searchMan";
    }

    @GetRoute("/delMan/:id")
    public String delMan (@PathParam int id, Request request)
    {
        Session session = utils.getDBSession();
        session.beginTransaction();

        ManufacturersEntity brand = session.get(ManufacturersEntity.class, id);

        request.attribute("message", "Производитель \"" + brand.getManname() + "\" удалён");

        try {
            session.delete(brand);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        return "searchMan";
    }

}
