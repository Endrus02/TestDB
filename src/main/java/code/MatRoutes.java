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
public class MatRoutes {

    private Utils utils = new Utils();

    private List<MaterialsEntity> search (String name) {
        Query matQuery = utils.getDBSession().createQuery("from testDB.MaterialsEntity where matname LIKE :name ORDER BY matname");
        matQuery.setParameter("name", "%" + name + "%");

        return (List<MaterialsEntity>) matQuery.list();
    }

    @GetRoute("/editMat/:id")
    public String editMat (@PathParam int id, Request request) {
        Session session = utils.getDBSession();
        MaterialsEntity material = session.get(MaterialsEntity.class, id);
        request.attribute("material", material);
        request.attribute("brands", session.createCriteria(ManufacturersEntity.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list());
        session.close();
        return "editMat";
    }

    @GetRoute("/addManToMat/:manId/:matId")
    public void addManToMat (@PathParam int manId, @PathParam int matId, Response response) {

        Session session = utils.getDBSession();
        session.beginTransaction();

        ManufacturersEntity brand = session.get(ManufacturersEntity.class, manId);
        MaterialsEntity material = session.get(MaterialsEntity.class, matId);

        material.addManufacturer(brand);

        try {
            session.update(material);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        response.redirect("/editMat/" + matId);
    }

    @GetRoute("/delManFromMat/:manId/:matId")
    public void delManFromMat (@PathParam int manId, @PathParam int matId, Response response) {

        Session session = utils.getDBSession();
        session.beginTransaction();

        ManufacturersEntity brand = session.get(ManufacturersEntity.class, manId);
        MaterialsEntity material = session.get(MaterialsEntity.class, matId);

        material.removeManufacturer(brand);

        try {
            session.update(material);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        response.redirect("/editMat/" + matId);
    }

    @PostRoute("/editMat")
    public void doEdit(@Param MaterialsEntity material, Response response) {

        Session session = utils.getDBSession();
        session.beginTransaction();

        try {
            session.update(material);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        response.redirect("/searchMat");
    }


    @GetRoute("/addMat")
    public String addMat(){
        return "addMat";
    }

    @PostRoute("/addMat")
    public void doAdd(@Param MaterialsEntity material, Request request, Response response) {

        Session session = utils.getDBSession();
        session.beginTransaction();

        try {
            session.save(material);
            session.getTransaction().commit();
            request.session().attribute("message", "Материал добавлен");
        } catch (Exception e) {
            session.getTransaction().rollback();
            request.session().attribute("message", "Ошибка при добавлении материала");
            System.out.println(e);
        }
        session.close();
        response.redirect("/searchMat");
    }

    @GetRoute("/searchMat")
    public String searchMat(Request request){

        request.attribute("materials", this.search(""));
        return "searchMat";
    }

    @PostRoute("/searchMat")
    public String doSearch(@Param String name, Request request) {

        request.attribute("name", name);
        request.attribute("materials", this.search(name));

        return "searchMat";
    }

    @GetRoute("/delMat/:id")
    public String delMat (@PathParam int id, Request request)
    {
        Session session = utils.getDBSession();
        session.beginTransaction();

        MaterialsEntity material = session.get(MaterialsEntity.class, id);

        request.attribute("message", "Производитель \"" + material.getMatname() + "\" удалён");

        try {
            session.delete(material);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        return "searchMat";
    }

    @PostRoute("/massDelMat")
    public void massDelMat (Request request, Response response)
    {
        Session session = utils.getDBSession();
        session.beginTransaction();

        List<String> ids =  request.parameterValues("matIds");

        try {

            for (int i = 0; i < ids.size(); i++ )
            {

                MaterialsEntity material = session.get(MaterialsEntity.class, Integer.parseInt(ids.get(i)));
                session.delete(material);
            }

            request.session().attribute("message", "Материалы удалены");

            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e);
        }
        session.close();
        response.redirect("/searchMat");
    }
}
