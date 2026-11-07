package com.sabbir.smartsettings;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.*;
import android.provider.Settings;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends Activity {

    LinearLayout main;
    EditText search;

    final int GREEN = Color.rgb(22,105,68);
    final int DARK = Color.rgb(20,55,40);
    final int BG = Color.rgb(243,248,245);
    final int TEXT = Color.rgb(35,55,45);

    class Problem {
        String title, subtitle, icon, guide, setting;

        Problem(String t,String s,String i,String g,String a) {
            title=t;
            subtitle=s;
            icon=i;
            guide=g;
            setting=a;
        }
    }

    ArrayList<Problem> problems = new ArrayList<>();

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        addProblems();
        showHome();
    }

    void addProblems() {

        problems.add(new Problem(
            "ফোন গরম হচ্ছে","অতিরিক্ত তাপ কমান","🌡",
            "১. ভারী গেম ও ভিডিও অ্যাপ কিছুক্ষণ বন্ধ রাখুন।\n\n"+
            "২. চার্জ দেওয়ার সময় ফোন ব্যবহার কমান।\n\n"+
            "৩. অপ্রয়োজনীয় Hotspot বন্ধ রাখুন।\n\n"+
            "৪. ফোন রোদে রাখবেন না।\n\n"+
            "৫. অতিরিক্ত গরম হলে চার্জার খুলে ফোন ঠান্ডা হতে দিন।",
            "battery"));

        problems.add(new Problem(
            "ফোন স্লো বা হ্যাং","ফোনের গতি উন্নত করুন","🐢",
            "১. ফোন Restart করুন।\n\n"+
            "২. অপ্রয়োজনীয় অ্যাপ বন্ধ করুন।\n\n"+
            "৩. স্টোরেজে জায়গা খালি করুন।\n\n"+
            "৪. অপ্রয়োজনীয় অ্যাপ আনইনস্টল করুন।\n\n"+
            "৫. গুরুত্বপূর্ণ ফাইল না বুঝে মুছবেন না।",
            "storage"));

        problems.add(new Problem(
            "ব্যাটারি দ্রুত শেষ","ব্যাটারি ব্যবহার পরীক্ষা করুন","🔋",
            "১. Battery Usage পরীক্ষা করুন।\n\n"+
            "২. স্ক্রিনের উজ্জ্বলতা কমান।\n\n"+
            "৩. দরকার না হলে Hotspot বন্ধ রাখুন।\n\n"+
            "৪. অপ্রয়োজনীয় Background Activity সীমিত করুন।",
            "battery"));

        problems.add(new Problem(
            "নোটিফিকেশন আসে না","অ্যাপের নোটিফিকেশন পরীক্ষা করুন","🔔",
            "১. যে অ্যাপে নোটিফিকেশন আসে না সেটি নির্বাচন করুন।\n\n"+
            "২. App Notifications সেটিংস খুলুন।\n\n"+
            "৩. Allow Notifications চালু আছে কি না দেখুন।\n\n"+
            "৪. না এলে অ্যাপের Battery ও Background Activity পরীক্ষা করুন।",
            "notification"));

        problems.add(new Problem(
            "ইন্টারনেট স্লো","নেটওয়ার্ক পরীক্ষা করুন","📶",
            "১. Airplane Mode বন্ধ আছে কি না দেখুন।\n\n"+
            "২. Wi-Fi বা Mobile Data পরীক্ষা করুন।\n\n"+
            "৩. রাউটার ব্যবহার করলে Restart করুন।\n\n"+
            "৪. প্রয়োজনে নেটওয়ার্ক অপারেটরের সঙ্গে যোগাযোগ করুন।",
            "internet"));

        problems.add(new Problem(
            "স্টোরেজ পূর্ণ","ফোনের জায়গা খালি করুন","💾",
            "১. Storage Settings খুলুন।\n\n"+
            "২. বড় ও অপ্রয়োজনীয় ফাইল পর্যালোচনা করুন।\n\n"+
            "৩. অপ্রয়োজনীয় Download মুছুন।\n\n"+
            "৪. গুরুত্বপূর্ণ ফাইল Backup ছাড়া মুছবেন না।",
            "storage"));

        problems.add(new Problem(
            "সাউন্ড কাজ করে না","শব্দের সেটিংস পরীক্ষা করুন","🔊",
            "১. Media Volume ও Ring Volume বাড়ান।\n\n"+
            "২. Silent Mode বন্ধ করুন।\n\n"+
            "৩. Bluetooth Headphone সংযুক্ত আছে কি না দেখুন।\n\n"+
            "৪. ফোন Restart করে পরীক্ষা করুন।",
            "sound"));

        problems.add(new Problem(
            "কল বা SIM সমস্যা","মোবাইল নেটওয়ার্ক পরীক্ষা করুন","📞",
            "১. SIM সঠিকভাবে বসানো আছে কি না দেখুন।\n\n"+
            "২. Airplane Mode বন্ধ করুন।\n\n"+
            "৩. Network Settings পরীক্ষা করুন।\n\n"+
            "৪. SIM কাজ না করলে অপারেটরের সঙ্গে যোগাযোগ করুন।",
            "network"));

        problems.add(new Problem(
            "Bluetooth সমস্যা","Bluetooth সংযোগ ঠিক করুন","🔵",
            "১. Bluetooth বন্ধ করে আবার চালু করুন।\n\n"+
            "২. অন্য ডিভাইসের Bluetooth চালু আছে কি না দেখুন।\n\n"+
            "৩. ডিভাইসটি কাছাকাছি রাখুন।\n\n"+
            "৪. প্রয়োজনে Pairing আবার করুন।",
            "bluetooth"));

        problems.add(new Problem(
            "Location কাজ করে না","লোকেশন সেটিংস পরীক্ষা করুন","📍",
            "১. Location চালু করুন।\n\n"+
            "২. সংশ্লিষ্ট অ্যাপের Location Permission পরীক্ষা করুন।\n\n"+
            "৩. খোলা জায়গায় GPS পরীক্ষা করুন।",
            "location"));
    }

    void showHome() {

        ScrollView scroll = new ScrollView(this);

        main = new LinearLayout(this);
        main.setOrientation(1);
        main.setPadding(16,18,16,25);
        main.setBackgroundColor(BG);

        scroll.addView(main);
        setContentView(scroll);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(1);
        header.setPadding(20,25,20,25);
        header.setBackground(round(DARK,24));

        TextView logo = text("SABBIR SMART",26,Color.WHITE,true);
        logo.setGravity(Gravity.CENTER);
        header.addView(logo);

        TextView sub = text(
            "আপনার ফোনের স্মার্ট সমস্যা সমাধানকারী",
            14,Color.WHITE,false);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0,8,0,0);
        header.addView(sub);

        main.addView(header);

        TextView welcome = text(
            "আপনার ফোনে কী সমস্যা হচ্ছে?",
            20,TEXT,true);
        welcome.setPadding(2,24,2,12);
        main.addView(welcome);

        search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(15);
        search.setHint("যেমন: ফোন গরম হচ্ছে...");
        search.setPadding(15,12,15,12);
        search.setBackground(round(Color.WHITE,14));
        main.addView(search);

        Button find = new Button(this);
        find.setText("সমাধান খুঁজুন");
        find.setAllCaps(false);
        find.setTextColor(Color.WHITE);
        find.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(GREEN));
        main.addView(find);

        find.setOnClickListener(v -> searchProblem());

        heading("জনপ্রিয় সমস্যা");

        for(int i=0;i<problems.size();i+=2) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(0);

            addProblemCard(row,problems.get(i));

            if(i+1<problems.size()) {
                addProblemCard(row,problems.get(i+1));
            }

            main.addView(row);
        }

        heading("সব ফোন সেটিংস");

        addSettingCard("Wi-Fi","ওয়্যারলেস নেটওয়ার্ক","📡","wifi");
        addSettingCard("Display","স্ক্রিন ও উজ্জ্বলতা","🖥","display");
        addSettingCard("Security ও Privacy","নিরাপত্তা ও গোপনীয়তা","🔐","security");
        addSettingCard("App Management","অ্যাপ পরিচালনা","📱","apps");
        addSettingCard("Keyboard ও Language","ভাষা ও কিবোর্ড","⌨","language");
        addSettingCard("Date ও Time","তারিখ ও সময়","🕒","datetime");
        addSettingCard("Do Not Disturb","বিরক্ত করবেন না","🔕","dnd");
        addSettingCard("Location","লোকেশন সেটিংস","📍","location");
        addSettingCard("Phone Information","ফোনের তথ্য","ℹ","about");

        TextView footer = text(
            "Developed by Sabbir Ahmed",13,GREEN,true);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0,25,0,10);
        main.addView(footer);
    }

    void addProblemCard(LinearLayout row,Problem p) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(1);
        card.setPadding(14,16,14,16);
        card.setBackground(round(Color.WHITE,18));

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(0,-1,1);
        lp.setMargins(4,5,4,5);

        card.addView(text(p.icon,28,GREEN,true));

        TextView title = text(p.title,15,TEXT,true);
        title.setPadding(0,9,0,4);
        card.addView(title);

        card.addView(text(p.subtitle,11,Color.GRAY,false));

        row.addView(card,lp);
        card.setOnClickListener(v -> showGuide(p));
    }

    void addSettingCard(
        String title,String sub,String icon,String type) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(0);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(16,14,16,14);
        card.setBackground(round(Color.WHITE,16));

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1,-2);
        lp.setMargins(0,5,0,5);

        card.addView(text(icon,23,GREEN,true));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(1);
        info.setPadding(14,0,0,0);
        info.addView(text(title,16,TEXT,true));
        info.addView(text(sub,12,Color.GRAY,false));

        card.addView(info,new LinearLayout.LayoutParams(0,-2,1));
        card.addView(text("›",27,GREEN,true));

        main.addView(card,lp);
        card.setOnClickListener(v -> showSettingGuide(title,type));
    }

    void heading(String title) {
        TextView h = text(title,19,GREEN,true);
        h.setPadding(2,20,2,10);
        main.addView(h);
    }

    TextView text(String value,int size,int color,boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if(bold) t.setTypeface(null,Typeface.BOLD);
        return t;
    }

    GradientDrawable round(int color,int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    void searchProblem() {

        String q = search.getText().toString()
            .toLowerCase().trim();

        if(q.isEmpty()) {
            message("আপনার সমস্যাটি লিখুন।");
            return;
        }

        if(q.contains("গরম") || q.contains("heat")) {
            showGuide(problems.get(0));
        }
        else if(q.contains("স্লো") || q.contains("হ্যাং")) {
            showGuide(problems.get(1));
        }
        else if(q.contains("ব্যাটারি") || q.contains("battery")) {
            showGuide(problems.get(2));
        }
        else if(q.contains("নোটিফিকেশন") || q.contains("notification")) {
            showGuide(problems.get(3));
        }
        else if(q.contains("ইন্টারনেট") || q.contains("internet") || q.contains("নেট")) {
            showGuide(problems.get(4));
        }
        else if(q.contains("স্টোরেজ") || q.contains("storage")) {
            showGuide(problems.get(5));
        }
        else if(q.contains("সাউন্ড") || q.contains("শব্দ")) {
            showGuide(problems.get(6));
        }
        else if(q.contains("সিম") || q.contains("কল")) {
            showGuide(problems.get(7));
        }
        else if(q.contains("bluetooth")) {
            showGuide(problems.get(8));
        }
        else if(q.contains("লোকেশন") || q.contains("location")) {
            showGuide(problems.get(9));
        }
        else {
            message("সমস্যাটি শনাক্ত করতে পারিনি। নিচের কার্ড থেকে নির্বাচন করুন।");
        }
    }

    void showGuide(Problem p) {

        if(p.setting.equals("notification")) {
            new AlertDialog.Builder(this)
                .setTitle(p.icon+" "+p.title)
                .setMessage(p.guide)
                .setPositiveButton("অ্যাপ নির্বাচন করুন",
                    (d,w)->chooseApp())
                .setNegativeButton("বন্ধ করুন",null)
                .show();
            return;
        }

        new AlertDialog.Builder(this)
            .setTitle(p.icon+" "+p.title)
            .setMessage(p.guide)
            .setPositiveButton("সেটিংস খুলুন",
                (d,w)->openSetting(p.setting))
            .setNegativeButton("বন্ধ করুন",null)
            .show();
    }

    void chooseApp() {

        PackageManager pm = getPackageManager();

        Intent launcher = new Intent(Intent.ACTION_MAIN);
        launcher.addCategory(Intent.CATEGORY_LAUNCHER);

        java.util.List<ResolveInfo> apps =
            pm.queryIntentActivities(launcher,0);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> packages = new ArrayList<>();
        HashSet<String> seen = new HashSet<>();

        for(ResolveInfo app:apps) {
            String pkg = app.activityInfo.packageName;

            if(!pkg.equals(getPackageName()) && seen.add(pkg)) {
                names.add(app.loadLabel(pm).toString());
                packages.add(pkg);
            }
        }

        if(names.isEmpty()) {
            message("কোনো অ্যাপ পাওয়া যায়নি।");
            return;
        }

        new AlertDialog.Builder(this)
            .setTitle("সমস্যার অ্যাপ নির্বাচন করুন")
            .setItems(names.toArray(new String[0]),(d,which)->{
                String pkg = packages.get(which);

                try {
                    Intent i = new Intent(
                        Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    i.putExtra(Settings.EXTRA_APP_PACKAGE,pkg);
                    startActivity(i);
                } catch(Exception e) {
                    openAppInfo(pkg);
                }
            })
            .setNegativeButton("বাতিল",null)
            .show();
    }

    void openAppInfo(String pkg) {
        try {
            Intent i = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:"+pkg));
            startActivity(i);
        } catch(Exception e) {
            message("অ্যাপ সেটিংস খোলা যাচ্ছে না।");
        }
    }

    void showSettingGuide(String title,String type) {

        String guide =
            "এখান থেকে "+title+" পরিচালনা করতে পারবেন।\n\n"+
            "সেটিংস খুলে প্রয়োজনীয় অপশন খুঁজে পরিবর্তন করুন। "+
            "কোনো অপশন না বুঝে পরিবর্তন করবেন না।";

        if(type.equals("dnd")) {
            guide =
                "Do Not Disturb চালু থাকলে কল ও নোটিফিকেশন নীরব থাকতে পারে।\n\n"+
                "প্রয়োজন না হলে এটি বন্ধ আছে কি না পরীক্ষা করুন।";
        }

        if(type.equals("security")) {
            guide =
                "এখানে ফোনের নিরাপত্তা ও Privacy সংক্রান্ত অপশন পাবেন।\n\n"+
                "অজানা অ্যাপকে অপ্রয়োজনীয় Permission দেবেন না।";
        }

        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(guide)
            .setPositiveButton("সেটিংস খুলুন",
                (d,w)->openSetting(type))
            .setNegativeButton("বন্ধ করুন",null)
            .show();
    }

    void openSetting(String type) {

        Intent i = null;

        try {
            switch(type) {

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

            if(i != null) {
                startActivity(i);
            } else {
                message("এই সেটিংস খুঁজে পাওয়া যায়নি।");
            }

        } catch(Exception e) {
            message("আপনার ফোনে এই সেটিংসের অবস্থান আলাদা হতে পারে।");
        }
    }

    void message(String msg) {
        new AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton("ঠিক আছে",null)
            .show();
    }
            }
            
