package de.hse.ws18swa10;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hse.ws18swa10.dao.FileRequestDaoTest;
import de.hse.ws18swa10.dao.FileShareDaoTest;
import de.hse.ws18swa10.dao.PermissionDaoTest;
import de.hse.ws18swa10.dao.UserDaoTest;

@RunWith(Suite.class)
@SuiteClasses({
	UserDaoTest.class,
	PermissionDaoTest.class,
	FileRequestDaoTest.class,
	FileShareDaoTest.class
})
public class DaoIntegrationTestSuite
{
	@BeforeClass
	public static void setUp()
	{		
		System.out.println("Initializing/wiping test database...");
		
		try {
			new GradleRunner().runGradleInitializeTestDbTask();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}		
	}
}
