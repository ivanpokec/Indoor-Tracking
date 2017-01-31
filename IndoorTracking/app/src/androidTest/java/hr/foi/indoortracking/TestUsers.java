package hr.foi.indoortracking;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

/**
 * Created by Zana on 31.1.2017..
 */

public class TestUsers extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "hr.foi.indoortracking.Users";


    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public TestUsers() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }


    public void testUsers() {
        solo.waitForActivity("Users", 2000);
        Timeout.setSmallTimeout(15425);
        solo.clickOnText("Žana Zekić");
        solo.waitForActivity("UserProfile", 2000000);
        Timeout.setLargeTimeout(900000000);
        solo.clickOnActionBarHomeButton();
        solo.waitForActivity("Users", 2000000);
        Timeout.setSmallTimeout(2000000);
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

}
