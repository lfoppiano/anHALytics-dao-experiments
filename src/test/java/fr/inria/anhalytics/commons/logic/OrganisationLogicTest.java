package fr.inria.anhalytics.commons.logic;

import fr.inria.anhalytics.commons.dao.batis.OrganisationIbatisDAO;
import fr.inria.anhalytics.commons.entities.Organisation;
import fr.inria.anhalytics.commons.entities.Organisation_Name;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * Created by lfoppiano on 02/02/17.
 */
public class OrganisationLogicTest {
    OrganisationLogic target;
    SqlSessionFactory sessionFactoryMock;
    SqlSession sqlSessionMock;
    OrganisationIbatisDAO organisationIbatisDAOMock;

    @Before
    public void setUp() throws Exception {
        target = new OrganisationLogic();
        sessionFactoryMock = EasyMock.createMock(SqlSessionFactory.class);
        sqlSessionMock = EasyMock.createMock(SqlSession.class);
        organisationIbatisDAOMock = EasyMock.createMock(OrganisationIbatisDAO.class);

        target.setSqlSessionFactory(sessionFactoryMock);
    }


    @Test
    public void test1() throws Exception {
        Organisation organisation = new Organisation();
        organisation.setType("department");
        organisation.setStatus("NEW");
        final Date dateName1 = new Date(1);
        organisation.getNames().add(new Organisation_Name(null, "name1", dateName1));
        final Date dateName2 = new Date(2);
        organisation.getNames().add(new Organisation_Name(null, "name2", dateName2));

        expect(sessionFactoryMock.openSession()).andReturn(sqlSessionMock);
        expect(sqlSessionMock.getMapper((Class<Object>) anyObject())).andReturn(organisationIbatisDAOMock);

        expect(organisationIbatisDAOMock.insertOrganisation(organisation)).andReturn(1l);
        //This done automatically, but doesn't work in this test
        organisation.setOrganisationId(12l);

        expect(organisationIbatisDAOMock.insertOrganisationName(1l, "name1", dateName1)).andReturn(1l);
        expect(organisationIbatisDAOMock.insertOrganisationName(1l, "name2", dateName2)).andReturn(2l);

        sqlSessionMock.close();
        replay(sessionFactoryMock, sqlSessionMock, organisationIbatisDAOMock);
        target.create(organisation);
        System.out.println(organisation.getOrganisationId());
        verify(sessionFactoryMock, sqlSessionMock, organisationIbatisDAOMock);
    }


}