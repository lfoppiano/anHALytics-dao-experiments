package fr.inria.anhalytics.commons.dao.batis;

import fr.inria.anhalytics.commons.entities.Organisation;
import fr.inria.anhalytics.commons.entities.Organisation_Identifier;
import fr.inria.anhalytics.commons.entities.Organisation_Name;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by lfoppiano on 18/01/17.
 */
public class PetitMain {

    public static void main(String... args) throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            OrganisationIbatisDAO mapper = session.getMapper(OrganisationIbatisDAO.class);
            Organisation organisation = mapper.getOrganisationByID(5);

            System.out.println(organisation.getOrganisationId());
            System.out.println(organisation.getType());

            List<Organisation_Name> names = mapper.getOrganisationNamesByOrganisationId(5);

            System.out.println(names.size());
            for (Organisation_Name n : names) {
                System.out.println(n.getName());
            }

            List<Organisation_Identifier> ids = mapper.getOrganisationIdentifiersByOrganisationID(5);

            System.out.println(ids.size());
            for (Organisation_Identifier n : ids) {
                System.out.println(n.getId());
            }


        } finally {
            session.close();
        }

    }
}
