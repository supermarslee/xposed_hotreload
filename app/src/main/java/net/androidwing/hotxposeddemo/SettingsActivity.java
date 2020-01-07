package net.androidwing.hotxposeddemo;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.socks.library.KLog;

import net.androidwing.hotxposed.CommonUtils;
import net.androidwing.hotxposed.ShellUtil;

import java.lang.reflect.Method;

import static net.androidwing.hotxposed.CommonUtils.restartTargetApp;


public class SettingsActivity extends AppCompatActivity {

    private SettingsFragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.pref_setting);


            Preference reset = findPreference("author");
            reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("https://github.com/firesunCN"));
                    startActivity(intent);
                    return true;
                }
            });

            Preference show_icon = findPreference("show_icon");
            show_icon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object isChecked) {
                    PackageManager packageManager = getActivity().getPackageManager();
                    int status = (boolean) isChecked ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                    packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), getActivity().getPackageName() + ".SettingsActivity_Alias"), status, PackageManager.DONT_KILL_APP);
                    return true;
                }
            });

        }


        private Application getApplication() {
            try {
                final Class<?> activityThreadClass =
                        Class.forName("android.app.ActivityThread");
                final Method method = activityThreadClass.getMethod("currentApplication");
                return (Application) method.invoke(null, (Object[]) null);
            } catch (Exception e) {
            }
            return null;
        }

    }


}
