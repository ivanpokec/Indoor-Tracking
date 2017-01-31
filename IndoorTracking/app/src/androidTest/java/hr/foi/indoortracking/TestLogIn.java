package hr.foi.indoortracking;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Zana on 31.1.2017..
 */

public class TestLogIn extends  ActivityInstrumentationTestCase2 {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "hr.foi.indoortracking.Login";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public TestLogIn() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    public void testLogin() {
        solo.waitForActivity("Login", 2000);
        Timeout.setSmallTimeout(15425);
        solo.clearEditText((android.widget.EditText) solo.getView("editText_User"));
        solo.enterText((android.widget.EditText) solo.getView("editText_User"), "zzekic");
        solo.clickOnView(solo.getView("editText_Password"));
        solo.clearEditText((android.widget.EditText) solo.getView("editText_Password"));
        solo.enterText((android.widget.EditText) solo.getView("editText_Password"), "zanaz");
        solo.clickOnView(solo.getView("button_LogIn"));
        solo.waitForActivity("MainActivity", 2000000);
        Timeout.setSmallTimeout(15425);
        //solo.sendKey(Solo.MENU);
        //solo.clickOnText("Odjava");
        //solo.waitForActivity("Login", 2000000);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
