package assignment.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestBoardModel.class, TestColorHolder.class,
		TestViewModel.class })
public class AllTests {

}
