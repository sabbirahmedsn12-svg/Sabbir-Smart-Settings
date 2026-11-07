package com.sabbir.smartsettings;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.*;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.*;
import android.widget.*;
import android.graphics.Color;
import android.net.Uri;

import java.util.*;

public class MainActivity extends Activity {

    LinearLayout main;
    EditText problemInput;
    TextView result;
    static final int VOICE_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
    }

    void showHome() {
        ScrollView scroll = new ScrollView(this);
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(22, 25, 22, 25);
        main.setBackgroundColor(Color.rgb(245,248,246));

        scroll.addView(main);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("SABBIR SMART SETTINGS");
        title.setTextSize(24);
        title.setTextColor(Color.rgb(0,120,70));
        title.setGravity(Gravity.CENTER);
        title.setPadding(5,20,5,20);
        main.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("আপনার ফোনের সমস্যা লিখুন, সমাধানের পথ দেখুন");
        subtitle.setTextSize(16);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);
        main.addView(subtitle);

        problemInput = new EditText(this);
        problemInput.setHint("যেমন: নোটিফিকেশন আসে না");
        problemInput.setTextSize(16);
        main.addView(problemInput);

        addButton("সমস্যা খুঁজুন", () ->
                diagnose(problemInput.getText().toString()));

        addButton("🎤 কথা বলে সমস্যা বলুন", this::startVoice);

        result = new TextView(this);
        result.setTextSize(16);
        result.setTextColor(Color.BLACK);
        result.setPadding(8,20,8,15);
        main.addView(result);

        addHeading("ফোনের সেটিংস");

        String[][] categories = {
            {"নোটিফিকেশন সমস্যা","notification"},
            {"Wi-Fi সমস্যা","wifi"},
            {"মোবাইল নেটওয়ার্ক ও SIM","network"},
            {"Bluetooth সমস্যা","bluetooth"},
            {"সাউন্ড ও ভলিউম","sound"},
            {"ব্যাটারি সমস্যা","battery"},
            {"ডিসপ্লে ও উজ্জ্বলতা","display"},
            {"লোকেশন সমস্যা","location"},
            {"নিরাপত্তা ও গোপনীয়তা","security"},
            {"অ্যাপ ম্যানেজমেন্ট","apps"},
            {"স্টোরেজ সমস্যা","storage"},
            {"কিবোর্ড ও ভাষা","language"},
            {"তারিখ ও সময়","datetime"},
            {"Accessibility","accessibility"},
            {"Do Not Disturb","dnd"},
            {"Default Apps","defaults"},
            {"অ্যাকাউন্ট ও Sync","accounts"},
            {"Privacy Settings","privacy"},
            {"Developer Options","developer"},
            {"ফোনের তথ্য","about"},
            {"Hotspot","hotspot"},
            {"Airplane Mode","airplane"}
        };

        for (String[] item : categories) {
            addButton(item[0], () -> openCategory(item[1]));
        }
    }

    void addHeading(String text) {
        TextView heading = new TextView(this);
        heading.setText(text);
        heading.setTextSize(19);
        heading.setTextColor(Color.rgb(0,120,70));
        heading.setPadding(0,25,0,10);
        main.addView(heading);
    }

    void addButton(String text, Runnable action) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(
                Color.rgb(220,240,228)));
        button.setTextColor(Color.rgb(0,90,50));

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                -1, -2);
        params.setMargins(0,7,0,7);

        main.addView(button, params);
        button.setOnClickListener(v -> action.run());
    }

    void diagnose(String problem) {
        if (problem.trim().isEmpty()) {
            result.setText("আগে আপনার সমস্যাটি লিখুন।");
            return;
        }

        String p = problem.toLowerCase();

        if (p.contains("notification") ||
            p.contains("নোটিফিকেশন") ||
            p.contains("মেসেজ আসে না") ||
            p.contains("নোটিশ আসে না")) {

            result.setText(
                "নোটিফিকেশন সমস্যার সম্ভাব্য কারণ:\n\n" +
                "১. অ্যাপের নোটিফিকেশন বন্ধ\n" +
                "২. ব্যাটারির সীমাবদ্ধতা\n" +
                "৩. Do Not Disturb চালু\n\n" +
                "প্রথমে যে অ্যাপে সমস্যা হচ্ছে সেটি নির্বাচন করুন।"
            );
            chooseApp();
        }
        else if (p.contains("wifi") || p.contains("ওয়াইফাই")) {
            result.setText("Wi-Fi সেটিংস খুলছি।");
            openCategory("wifi");
        }
        else if (p.contains("battery") || p.contains("ব্যাটারি")) {
            result.setText("ব্যাটারি সেটিংস খুলছি।");
            openCategory("battery");
        }
        else if (p.contains("bluetooth")) {
            openCategory("bluetooth");
        }
        else if (p.contains("sound") || p.contains("সাউন্ড")) {
            openCategory("sound");
        }
        else if (p.contains("storage") || p.contains("স্টোরেজ")) {
            openCategory("storage");
        }
        else {
            result.setText(
                "এই সমস্যাটি এখনো শনাক্ত করতে পারিনি। " +
                "আরেকটু বিস্তারিত লিখুন।"
            );
        }
    }

    void startVoice() {
        Intent intent = new Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "bn-BD");
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "আপনার ফোনের সমস্যা বলুন");

        try {
            startActivityForResult(intent, VOICE_CODE);
        } catch (Exception e) {
            Toast.makeText(this,
                "ভয়েস সার্চ চালু করা যাচ্ছে না",
                Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(
        int requestCode, int resultCode, Intent data) {

        super.onActivityResult(
            requestCode, resultCode, data);

        if (requestCode == VOICE_CODE &&
            resultCode == RESULT_OK &&
            data != null) {

            ArrayList<String> words =
                data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            if (words != null && !words.isEmpty()) {
                problemInput.setText(words.get(0));
                diagnose(words.get(0));
            }
        }
    }

    void chooseApp() {
        PackageManager pm = getPackageManager();

        Intent launcher = new Intent(
            Intent.ACTION_MAIN, null);
        launcher.addCategory(
            Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps =
            pm.queryIntentActivities(launcher, 0);

        Collections.sort(apps,
            (a,b) -> a.loadLabel(pm).toString()
                .compareToIgnoreCase(
                    b.loadLabel(pm).toString()));

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> packages = new ArrayList<>();

        HashSet<String> seen = new HashSet<>();

        for (ResolveInfo app : apps) {
            String packageName =
                app.activityInfo.packageName;

            if (!packageName.equals(getPackageName())
                && seen.add(packageName)) {

                names.add(
                    app.loadLabel(pm).toString());
                packages.add(packageName);
            }
        }

        if (names.isEmpty()) {
            Toast.makeText(this,
                "কোনো অ্যাপ পাওয়া যায়নি",
                Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(this)
            .setTitle("কোন অ্যাপে নোটিফিকেশন আসে না?")
            .setItems(names.toArray(new String[0]),
                (dialog, which) ->
                    openAppNotificationSettings(
                        packages.get(which)))
            .setNegativeButton("বাতিল", null)
            .show();
    }

    void openAppNotificationSettings(String packageName) {
        try {
            Intent intent = new Intent(
                Settings.ACTION_APP_NOTIFICATION_SETTINGS);

            intent.putExtra(
                Settings.EXTRA_APP_PACKAGE,
                packageName);

            startActivity(intent);

        } catch (Exception e) {
            try {
                Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + packageName));

                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this,
                    "সেটিংস খোলা যাচ্ছে না",
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    void openCategory(String category) {
        Intent intent = null;

        try {
            switch (category) {

                case "wifi":
                    intent = new Intent(
                        Settings.ACTION_WIFI_SETTINGS);
                    break;

                case "network":
                    intent = new Intent(
                        Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    break;

                case "bluetooth":
                    intent = new Intent(
                        Settings.ACTION_BLUETOOTH_SETTINGS);
                    break;

                case "sound":
                    intent = new Intent(
                        Settings.ACTION_SOUND_SETTINGS);
                    break;

                case "battery":
                    intent = new Intent(
                        Settings.ACTION_BATTERY_SAVER_SETTINGS);
                    break;

                case "display":
                    intent = new Intent(
                        Settings.ACTION_DISPLAY_SETTINGS);
                    break;

                case "location":
                    intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    break;

                case "security":
                    intent = new Intent(
                        Settings.ACTION_SECURITY_SETTINGS);
                    break;

                case "apps":
                    intent = new Intent(
                        Settings.ACTION_APPLICATION_SETTINGS);
                    break;

                case "storage":
                    intent = new Intent(
                        Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                    break;

                case "language":
                    intent = new Intent(
                        Settings.ACTION_INPUT_METHOD_SETTINGS);
                    break;

                case "datetime":
                    intent = new Intent(
                        Settings.ACTION_DATE_SETTINGS);
                    break;

                case "accessibility":
                    intent = new Intent(
                        Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    break;

                case "dnd":
                    intent = new Intent(
                        Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS);
                    break;

                case "defaults":
                    intent = new Intent(
                        Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                    break;

                case "accounts":
                    intent = new Intent(
                        Settings.ACTION_SYNC_SETTINGS);
                    break;

                case "privacy":
                    intent = new Intent(
                        Settings.ACTION_PRIVACY_SETTINGS);
                    break;

                case "developer":
                    intent = new Intent(
                        Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                    break;

                case "about":
                    intent = new Intent(
                        Settings.ACTION_DEVICE_INFO_SETTINGS);
                    break;

                case "hotspot":
                case "airplane":
                    intent = new Intent(
                        Settings.ACTION_WIRELESS_SETTINGS);
                    break;

                case "notification":
                    chooseApp();
                    return;
            }

            if (intent != null) {
                startActivity(intent);
            }

        } catch (Exception e) {
            Toast.makeText(this,
                "এই সেটিংস আপনার ফোনে আলাদা জায়গায় থাকতে পারে।",
                Toast.LENGTH_LONG).show();
        }
    }
    }
