package com.eisgroup.cb.service;

import com.eisgroup.cb.dao.AdminDAO;
import com.eisgroup.cb.mock.AdminDAOMock;
import com.eisgroup.cb.model.Admin;
import com.eisgroup.cb.service.impl.AdminServiceImpl;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.NonUniqueResultException;
import java.util.concurrent.TimeUnit;

@ContextConfiguration("classpath:spring-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AdminServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceTest.class);
    private static StringBuilder results = new StringBuilder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            LOG.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        LOG.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------\n" +
                results +
                "---------------------------------\n");
    }

    private AdminService service;

    public AdminServiceTest() {
        AdminDAO dao = new AdminDAOMock();
        this.service = new AdminServiceImpl(dao);
    }


    @Test
    public void testisAdminValid() throws Exception {
        Admin admin = new Admin();
        admin.setLogin("admin");
        admin.setPass("admin");
        Assert.assertTrue(service.isAdminValid(admin));
    }

    @Test
    public void testisAdminValidNot() throws Exception {
        Admin admin = new Admin();
        admin.setLogin("user");
        admin.setPass("user");
        Assert.assertFalse(service.isAdminValid(admin));
    }

    @Test
    public void testisAdminValidNotConsistent() throws Exception {
        thrown.expect(NonUniqueResultException.class);
        Admin admin = new Admin();
        admin.setLogin("multiple");
        admin.setPass("multiple");
        service.isAdminValid(admin);
    }
}