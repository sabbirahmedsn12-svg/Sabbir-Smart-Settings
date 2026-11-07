package com.sabbir.smartsettings;

import android.app.*;
import android.os.*;
import android.content.*;
import android.provider.Settings;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import android.graphics.Color;

public class MainActivity extends Activity {

    LinearLayout main;
    EditText search;

    android.content.SharedPreferences prefs;

    final int GREEN = Color.rgb(20, 115, 75);
    final int DARK = Color.rgb(25, 55, 43);
    final int LIGHT = Color.rgb(235, 246, 239);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("smart_help", MODE_PRIVATE);
        showHome();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs != null &&
            prefs.getBoolean("waiting", false)) {

            prefs.edit().putBoolean("waiting", false).apply();

            new Handler().postDelayed(() -> {
                askAfterSettings();
            }, 400);
        }
    }

    void showHome() {
        ScrollView scroll = new ScrollView(this);

        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(18, 20, 18, 25);
        main.setBackgroundColor(Color.rgb(246, 249, 247));

        scroll.addView(main);
        setContentView(scroll);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(20, 25, 20, 25);
        header.setBackgroundColor(DARK);

        TextView logo = new TextView(this);
        logo.setText("SABBIR SMART");
        logo.setTextSize(27);
        logo.setTextColor(Color.WHITE);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, 1);
        header.addView(logo);

        TextView slogan = new TextView(this);
        slogan.setText("আপনার ফোনের স্মার্ট সমস্যা সমাধানকারী");
        slogan.setTextSize(15);
        slogan.setTextColor(Color.WHITE);
        slogan.setGravity(Gravity.CENTER);
        slogan.setPadding(0, 8, 0, 0);
        header.addView(slogan);

        main.addView(header);

        TextView welcome = new TextView(this);
        welcome.setText("আপনার ফোনে কী সমস্যা হচ্ছে?");
        welcome.setTextSize(20);
        welcome.setTextColor(DARK);
        welcome.setTypeface(null, 1);
        welcome.setPadding(0, 22, 0, 10);
        main.addView(welcome);

        search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(16);
        search.setHint("সমস্যা লিখুন...");
        search.setPadding(15, 12, 15, 12);
        search.setBackgroundColor(Color.WHITE);
        main.addView(search);

        addButton("সমাধান খুঁজুন", () ->
            diagnose(search.getText().toString()));

        addHeading("জনপ্রিয় সমস্যা");

        addProblem("🌡 ফোন গরম হয়ে যায়", "heat");
        addProblem("🐢 ফোন স্লো বা হ্যাং করে", "slow");
        addProblem("🔋 ব্যাটারি দ্রুত শেষ হয়", "battery");
        addProblem("🔔 নোটিফিকেশন আসে না", "notification");
        addProblem("📶 ইন্টারনেট স্লো", "internet");
        addProblem("💾 স্টোরেজ পূর্ণ", "storage");
        addProblem("🔊 সাউন্ড কাজ করে না", "sound");
        addProblem("📞 কল বা SIM সমস্যা", "network");
        addProblem("🔵 Bluetooth কাজ করে না", "bluetooth");
        addProblem("📍 Location সমস্যা", "location");

        addHeading("সব ফোন সেটিংস");

        addProblem("Wi-Fi", "wifi");
        addProblem("Display", "display");
        addProblem("Security ও Privacy", "security");
        addProblem("অ্যাপ ম্যানেজমেন্ট", "apps");
        addProblem("কিবোর্ড ও ভাষা", "language");
        addProblem("তারিখ ও সময়", "datetime");
        addProblem("Do Not Disturb", "dnd");
        addProblem("ফোনের তথ্য", "about");

        TextView footer = new TextView(this);
        footer.setText("Developed by Sabbir Ahmed");
        footer.setTextColor(GREEN);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(5, 25, 5, 10);
        main.addView(footer);
    }

    void addHeading(String text) {
        TextView heading = new TextView(this);
        heading.setText(text);
        heading.setTextSize(19);
        heading.setTypeface(null, 1);
        heading.setTextColor(GREEN);
        heading.setPadding(0, 22, 0, 10);
        main.addView(heading);
    }

    void addButton(String text, Runnable action) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setTextColor(DARK);
        button.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(LIGHT));

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 5, 0, 5);

        main.addView(button, lp);
        button.setOnClickListener(v -> action.run());
    }

    void addProblem(String title, String type) {
        addButton(title, () -> diagnose(type));
    }

    void diagnose(String problem) {
        if (problem == null || problem.trim().isEmpty()) {
            message("সমস্যাটি লিখুন অথবা নিচের তালিকা থেকে বেছে নিন।");
            return;
        }

        String p = problem.toLowerCase();

        if (p.contains("heat") || p.contains("গরম")) {
            showHeatGuide();
        } else if (p.contains("slow") ||
                   p.contains("স্লো") ||
                   p.contains("হ্যাং")) {
            showSlowGuide();
        } else if (p.contains("battery") ||
                   p.contains("ব্যাটারি")) {
            showBatteryGuide();
        } else if (p.contains("notification") ||
                   p.contains("নোটিফিকেশন") ||
                   p.contains("মেসেজ আসে না")) {
            showNotificationGuide();
        } else if (p.contains("internet") ||
                   p.contains("ইন্টারনেট")) {
            openSetting("internet");
        } else if (p.contains("storage") ||
                   p.contains("স্টোরেজ")) {
            openSetting("storage");
        } else if (p.contains("sound") ||
                   p.contains("সাউন্ড")) {
            openSetting("sound");
        } else if (p.contains("wifi") ||
                   p.contains("ওয়াইফাই")) {
            openSetting("wifi");
        } else if (p.contains("bluetooth")) {
            openSetting("bluetooth");
        } else if (p.contains("network") ||
                   p.contains("sim") ||
                   p.contains("কল")) {
            openSetting("network");
        } else {
            message("এই সমস্যার নির্দেশিকা এখনো যোগ করা হয়নি। অন্যভাবে সমস্যাটি লিখুন।");
        }
    }

    void showHeatGuide() {
        String guide =
            "ফোন গরম হওয়ার সম্ভাব্য কারণ:\n\n" +
            "১. বেশি অ্যাপ একসঙ্গে চলছে।\n" +
            "২. গেম বা ভিডিওতে বেশি চাপ পড়ছে।\n" +
            "৩. চার্জের সময় ফোন ব্যবহার হচ্ছে।\n" +
            "৪. ব্যাকগ্রাউন্ডে অ্যাপ কাজ করছে।\n\n" +
            "প্রথমে অপ্রয়োজনীয় অ্যাপ বন্ধ করুন। " +
            "ফোন ঠান্ডা হতে দিন এবং চার্জের সময় ব্যবহার কমান।";

        new AlertDialog.Builder(this)
            .setTitle("🌡 ফোন গরম হচ্ছে")
            .setMessage(guide)
            .setPositiveButton("ব্যাটারি সেটিংস", (d,w) ->
                openSetting("battery"))
            .setNeutralButton("অ্যাপের ব্যবহার দেখুন", (d,w) ->
                openSetting("apps"))
            .setNegativeButton("আরও নির্দেশনা", (d,w) ->
                showHeatMore())
            .show();
    }

    void showHeatMore() {
        new AlertDialog.Builder(this)
            .setTitle("ফোন ঠান্ডা রাখার উপায়")
            .setMessage(
                "• গেম বা ভারী অ্যাপ কিছুক্ষণ বন্ধ রাখুন।\n\n" +
                "• Hotspot দরকার না হলে বন্ধ রাখুন।\n\n" +
                "• স্ক্রিনের উজ্জ্বলতা কমান।\n\n" +
                "• ফোন রোদে রাখবেন না।\n\n" +
                "• ফোন অতিরিক্ত গরম হলে চার্জার খুলে ব্যবহার বন্ধ করুন।\n\n" +
                "• ফুলে যাওয়া ব্যাটারি বা অস্বাভাবিক গরম হলে ব্যবহার বন্ধ করে সার্ভিস সেন্টারে যোগাযোগ করুন।"
            )
            .setPositiveButton("ঠিক আছে", null)
            .show();
    }

    void showSlowGuide() {
        new AlertDialog.Builder(this)
            .setTitle("🐢 ফোন স্লো বা হ্যাং")
            .setMessage(
                "ফোন দ্রুত করার নিরাপদ উপায়:\n\n" +
                "১. অপ্রয়োজনীয় অ্যাপ বন্ধ করুন।\n" +
                "২. স্টোরেজ খালি রাখুন।\n" +
                "৩. ফোন Restart করুন।\n" +
                "৪. অপ্রয়োজনীয় অ্যাপ আনইনস্টল করুন।\n" +
                "৫. অ্যাপের Cache পরিষ্কার করতে পারেন।\n\n" +
                "কোনো গুরুত্বপূর্ণ ফাইল বা সিস্টেম অ্যাপ মুছবেন না।"
            )
            .setPositiveButton("স্টোরেজ দেখুন", (d,w) ->
                openSetting("storage"))
            .setNeutralButton("অ্যাপ দেখুন", (d,w) ->
                openSetting("apps"))
            .setNegativeButton("আরও নির্দেশনা", (d,w) ->
                showSlowMore())
            .show();
    }

    void showSlowMore() {
        new AlertDialog.Builder(this)
            .setTitle("ফোন স্লো হলে করণীয়")
            .setMessage(
                "• ফোন Restart করুন।\n\n" +
                "• অপ্রয়োজনীয় Download ফাইল পর্যালোচনা করুন।\n\n" +
                "• Play Store থেকে অ্যাপ আপডেট করুন।\n\n" +
                "• অচেনা Cleaner বা Booster অ্যাপ ইনস্টল করবেন না।\n\n" +
                "• Factory Reset করার আগে অবশ্যই ডেটা Backup রাখুন।"
            )
            .setPositiveButton("ঠিক আছে", null)
            .show();
    }

    void showBatteryGuide() {
        new AlertDialog.Builder(this)
            .setTitle("🔋 ব্যাটারি দ্রুত শেষ")
            .setMessage(
                "১. Battery Usage পরীক্ষা করুন।\n\n" +
                "২. স্ক্রিনের উজ্জ্বলতা কমান।\n\n" +
                "৩. প্রয়োজন না হলে Hotspot ও GPS বন্ধ রাখুন।\n\n" +
                "৪. অপ্রয়োজনীয় Background Activity সীমিত করুন।"
            )
            .setPositiveButton("Battery Settings", (d,w) ->
                openSetting("battery"))
            .setNegativeButton("বাতিল", null)
            .show();
    }

    void showNotificationGuide() {
        new AlertDialog.Builder(this)
            .setTitle("🔔 নোটিফিকেশন সমস্যা")
            .setMessage("কোন অ্যাপে নোটিফিকেশন আসে না, সেটি নির্বাচন করুন।")
            .setPositiveButton("অ্যাপ নির্বাচন", (d,w) ->
                chooseApp())
            .setNegativeButton("বাতিল", null)
            .show();
    }

    void chooseApp() {
        android.content.pm.PackageManager pm = getPackageManager();

        Intent launcher = new Intent(Intent.ACTION_MAIN);
        launcher.addCategory(Intent.CATEGORY_LAUNCHER);

        java.util.List<android.content.pm.ResolveInfo> apps =
            pm.queryIntentActivities(launcher, 0);

        java.util.ArrayList<String> names = new java.util.ArrayList<>();
        java.util.ArrayList<String> packages = new java.util.ArrayList<>();
        java.util.HashSet<String> seen = new java.util.HashSet<>();

        for (android.content.pm.ResolveInfo app : apps) {
            String pkg = app.activityInfo.packageName;

            if (!pkg.equals(getPackageName()) && seen.add(pkg)) {
                names.add(app.loadLabel(pm).toString());
                packages.add(pkg);
            }
        }

        new AlertDialog.Builder(this)
            .setTitle("সমস্যার অ্যাপ নির্বাচন করুন")
            .setItems(names.toArray(new String[0]), (d, which) -> {
                String pkg = packages.get(which);

                prefs.edit()
                    .putString("package", pkg)
                    .putString("stage", "notification")
                    .apply();

                try {
                    Intent i = new Intent(
                        Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    i.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                    prefs.edit().putBoolean("waiting", true).apply();
                    startActivity(i);
                } catch (Exception e) {
                    openAppInfo(pkg);
                }
            })
            .setNegativeButton("বাতিল", null)
            .show();
    }

    void openAppInfo(String pkg) {
        try {
            Intent i = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + pkg));
            prefs.edit().putBoolean("waiting", true).apply();
            startActivity(i);
        } catch (Exception e) {
            message("সেটিংস খোলা যাচ্ছে না।");
        }
    }

    void askAfterSettings() {
        String stage = prefs.getString("stage", "");

        if (stage.equals("notification")) {
            new AlertDialog.Builder(this)
                .setTitle("সমস্যা ঠিক হয়েছে?")
                .setMessage("নোটিফিকেশন সেটিংস পরীক্ষা করেছেন?")
                .setPositiveButton("হ্যাঁ", (d,w) ->
                    finishCheck())
                .setNegativeButton("না, ব্যাটারি পরীক্ষা", (d,w) -> {
                    prefs.edit().putString("stage", "battery").apply();
                    openAppInfo(prefs.getString("package", ""));
                })
                .show();

        } else if (stage.equals("battery")) {
            new AlertDialog.Builder(this)
                .setTitle("ব্যাটারি পরীক্ষা")
                .setMessage("অ্যাপের ব্যাটারি বা Background Activity পরীক্ষা করেছেন?")
                .setPositiveButton("ঠিক হয়েছে", (d,w) ->
                    finishCheck())
                .setNegativeButton("না, DND পরীক্ষা", (d,w) -> {
                    prefs.edit().putString("stage", "dnd").apply();
                    openSetting("dnd");
                })
                .show();

        } else if (stage.equals("dnd")) {
            new AlertDialog.Builder(this)
                .setTitle("শেষ পরীক্ষা")
                .setMessage("Do Not Disturb পরীক্ষা করে দেখুন। সমস্যা কি ঠিক হয়েছে?")
                .setPositiveButton("হ্যাঁ", (d,w) ->
                    finishCheck())
                .setNegativeButton("না", (d,w) ->
                    finishCheck())
                .show();
        }
    }

    void finishCheck() {
        prefs.edit()
            .remove("stage")
            .remove("package")
            .putBoolean("waiting", false)
            .apply();

        message("পরীক্ষা শেষ হয়েছে।");
    }

    void openSetting(String type) {
        Intent i = null;

        try {
            switch (type) {
                case "wifi":
                    i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    break;
                case "internet":
                    i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    break;
                case "battery":
                    i = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                    break;
                case "storage":
                    i = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                    break;
                case "apps":
                    i = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                    break;
                case "sound":
                    i = new Intent(Settings.ACTION_SOUND_SETTINGS);
                    break;
                case "bluetooth":
                    i = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    break;
                case "network":
                    i = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    break;
                case "display":
                    i = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                    break;
                case "location":
                    i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    break;
                case "security":
                    i = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    break;
                case "language":
                    i = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                    break;
                case "datetime":
                    i = new Intent(Settings.ACTION_DATE_SETTINGS);
                    break;
                case "dnd":
                    i = new Intent(Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS);
                    break;
                case "about":
                    i = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
                    break;
            }

            if (i != null) startActivity(i);

        } catch (Exception e) {
            message("এই সেটিংস আপনার ফোনে অন্য জায়গায় থাকতে পারে।");
        }
    }

    void message(String text) {
        new AlertDialog.Builder(this)
            .setMessage(text)
            .setPositiveButton("ঠিক আছে", null)
            .show();
    }
                       }
        
