package hr.foi.indoortracking;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

/**
 * Created by Zana on 31.1.2017..
 */

public class TestMyMovements extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "hr.foi.indoortracking.MyMovements";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public TestMyMovements() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    public void testMyMovements() {
        solo.waitForActivity("MyMovements", 2000);
        Timeout.setSmallTimeout(15425);
        /*solo.clearEditText((android.widget.EditText) solo.getView("from_editText"));
        solo.enterText((android.widget.EditText) solo.getView("from_editText"), "27.1.2017");
        solo.clickOnView(solo.getView("to_editText"));
        solo.clearEditText((android.widget.EditText) solo.getView("to_editText"));
        solo.enterText((android.widget.EditText) solo.getView("editText_Password"), "31.1.2017"); */
        solo.clickOnView(solo.getView("spinner"));
        solo.clickOnView(solo.getView(TextView.class, 1));
        solo.clickOnView(solo.getView("button_search_by_location"));
        solo.waitForActivity("HistoryByLocation", 2000000);
        Timeout.setSmallTimeout(15425);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
