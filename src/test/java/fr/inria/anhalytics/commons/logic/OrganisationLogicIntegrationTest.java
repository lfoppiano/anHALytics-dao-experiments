package fr.inria.anhalytics.commons.logic;

import fr.inria.anhalytics.commons.dao.batis.OrganisationIbatisDAO;
import fr.inria.anhalytics.commons.entities.Organisation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * Created by lfoppiano on 02/02/17.
 */
public class OrganisationLogicIntegrationTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // create an SqlSessionFactory
        Reader reader = Resources.getResourceAsReader("mybatis-config-test.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        reader.close();
    }

    @AfterClass
    public static void tearDown() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
        // populate in-memory database
        SqlSession session = sqlSessionFactory.openSession(true);
        Connection conn = session.getConnection();
        Reader reader = Resources.getResourceAsReader("CreateDB_organisation.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setStopOnError(true);
        runner.setAutoCommit(true);
        runner.runScript(reader);
        session.commit();
        session.close();
        reader.close();
    }

    @Test
    public void testInsertSelect_Organisation_shouldWork() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        OrganisationIbatisDAO mapper = sqlSession.getMapper(OrganisationIbatisDAO.class);

        Organisation org = mapper.getOrganisationByID(1l);
        assertThat(org, nullValue());

        Organisation org1 = new Organisation();
        org1.setStatus("bao");
//        org1.setType("institution");
//        org1.setUrl("http://www.google.com");
        mapper.insertOrganisation(org1);
        sqlSession.commit();

        Organisation org1_2 = mapper.getOrganisationByID(org1.getOrganisationId());
        assertThat(org1_2, notNullValue());

        mapper.insertOrganisation(org1);
        sqlSession.commit();
        assertThat(org1.getOrganisationId(), greaterThan(0l));

        /*long organisationID1 = mapper.insertOrganisation("career", "", "VALID");
        sqlSession.commit();
        Organisation org2 = mapper.getOrganisationByID(organisationID1);
        assertThat(org2, notNullValue());

        long organisationID2 = mapper.insertOrganisation("career2", "", "VALID2");
        sqlSession.commit();
        assertThat(organisationID2, greaterThan(1l));*/
    }
}
