package ar.edu.itba.hci.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ar.edu.itba.hci.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Switch themeSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        themeSwitch = root.findViewById(R.id.theme_switch);

        themeSwitchHandler();

        return root;
    }

    public void themeSwitchHandler() {

        int switchValue = requireActivity().getSharedPreferences("smarthome.darkmode", MODE_PRIVATE).getInt("smarthome.darkmode", -1);

        if(switchValue == -1 || switchValue == AppCompatDelegate.MODE_NIGHT_NO) {
            themeSwitch.setChecked(false);
        }
        else if(switchValue == AppCompatDelegate.MODE_NIGHT_YES){
            themeSwitch.setChecked(true);
        }

        themeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = requireActivity().getSharedPreferences("smarthome.darkmode", MODE_PRIVATE).edit();
                if(themeSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putInt("smarthome.darkmode", AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putInt("smarthome.darkmode", AppCompatDelegate.MODE_NIGHT_NO);
                }
                editor.apply();
            }
        });
    }

}