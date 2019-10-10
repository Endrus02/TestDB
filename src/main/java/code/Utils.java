package code;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Utils {

    private SessionFactory sessionFactory;

    private static Logger log = LoggerFactory.getLogger(Main.class);

    Utils() {
        Configuration configuration = new Configuration().configure();
        this.sessionFactory = configuration.buildSessionFactory();
    }

    Session getDBSession() {
        return sessionFactory.openSession();
    }

    static Logger getLog() {
        return log;
    }
 }
