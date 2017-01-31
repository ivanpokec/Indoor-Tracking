package hr.foi.indoortracking;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

/**
 * Created by Zana on 31.1.2017..
 */

public class TestCategory extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "hr.foi.indoortracking.Category";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public TestCategory() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    public void testCategory() {
        solo.waitForActivity("Category", 2000);
        Timeout.setSmallTimeout(15425);
        solo.clickOnText("Prodaja");
        solo.waitForActivity("Locations", 2000000);
        Timeout.setLargeTimeout(900000000);
        solo.clickOnText("Wc");
        Timeout.setLargeTimeout(900000000);
        solo.waitForActivity("LocationDetails", 2000000);
        Timeout.setLargeTimeout(900000000);
        solo.clickOnActionBarHomeButton();
        solo.waitForActivity("Locations", 2000000);
        Timeout.setSmallTimeout(2000000);
        solo.clickOnActionBarHomeButton();
        solo.waitForActivity("Category", 2000000);
        Timeout.setSmallTimeout(2000000);



    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
