package com.sabbir.smartsettings;

import android.app.*;
import android.os.*;
import android.content.*;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    LinearLayout root, grid, resultBox;
    EditText search;
    TextView resultTitle, resultText;
    final int GREEN = Color.rgb(8,127,88);

    String[] names = {
        "নোটিফিকেশন","Wi-Fi","মোবাইল নেটওয়ার্ক",
        "Bluetooth","সাউন্ড","ব্যাটারি",
        "ডিসপ্লে","লোকেশন","নিরাপত্তা",
        "অ্যাপস","স্টোরেজ","ভাষা ও কিবোর্ড",
        "তারিখ ও সময়","Accessibility","Do Not Disturb",
        "ডিফল্ট অ্যাপ","অ্যাকাউন্ট","ব্যাকআপ",
        "Developer Options","System Update",
        "কল সেটিংস","SIM সেটিংস","Hotspot",
        "Airplane Mode","গোপনীয়তা","অন্যান্য সেটিংস"
    };

    String[] icons = {
        "🔔","📶","🌐","🔵","🔊","🔋",
        "📱","📍","🔐","📦","💾","⌨️",
        "📅","♿","🌙","📲","👤","☁️",
        "🛠️","🔄","📞","📡","📡","✈️",
        "🛡️","⚙️"
    };

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(GREEN);
        getWindow().setNavigationBarColor(Color.BLACK);
        buildUI();
    }

    int dp(float v) {
        return (int)(v * getResources().getDisplayMetrics().density);
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    void buildUI() {
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(dp(16),dp(15),dp(16),dp(20));
        root.setBackgroundColor(Color.rgb(242,247,244));
        scroll.addView(root);
        setContentView(scroll);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(1);
        header.setPadding(dp(18),dp(22),dp(18),dp(22));
        header.setBackgroundColor(GREEN);

        TextView title = text("⚙ Sabbir Smart Settings",22,Color.WHITE);
        title.setTypeface(null,Typeface.BOLD);
        header.addView(title);

        TextView sub = text("আপনার ফোন, আপনার নিয়ন্ত্রণ",15,Color.WHITE);
        sub.setPadding(0,dp(12),0,dp(10));
        header.addView(sub);

        search = new EditText(this);
        search.setSingleLine(true);
        search.setHint("সমস্যা লিখুন... যেমন নোটিফিকেশন আসছে না");
        search.setTextSize(14);
        search.setPadding(dp(12),dp(10),dp(12),dp(10));
        search.setBackgroundColor(Color.WHITE);
        header.addView(search);

        LinearLayout buttons = new LinearLayout(this);
        Button find = new Button(this);
        find.setText("সমস্যা খুঁজুন");
        find.setOnClickListener(v -> searchProblem());
        buttons.addView(find,new LinearLayout.LayoutParams(0,dp(50),1));

        Button voice = new Button(this);
        voice.setText("🎙 বলুন");
        voice.setOnClickListener(v -> voiceSearch());
        buttons.addView(voice,new LinearLayout.LayoutParams(0,dp(50),1));

        header.addView(buttons);
        root.addView(header);

        TextView heading = text("📱 ফোনের সব সেটিংস",19,Color.BLACK);
        heading.setTypeface(null,Typeface.BOLD);
        heading.setPadding(0,dp(24),0,dp(15));
        root.addView(heading);

        grid = new LinearLayout(this);
        grid.setOrientation(1);
        root.addView(grid);

        for(int i=0;i<names.length;i+=3) {
            LinearLayout row = new LinearLayout(this);
            for(int j=i;j<Math.min(i+3,names.length);j++) {
                final int index=j;
                LinearLayout card = new LinearLayout(this);
                card.setOrientation(1);
                card.setGravity(Gravity.CENTER);
                card.setPadding(dp(5),dp(15),dp(5),dp(15));
                card.setBackgroundColor(Color.WHITE);

                TextView ico = text(icons[j],27,Color.BLACK);
                ico.setGravity(Gravity.CENTER);
                card.addView(ico);

                TextView label = text(names[j],12,Color.DKGRAY);
                label.setGravity(Gravity.CENTER);
                label.setPadding(0,dp(8),0,0);
                card.addView(label);

                LinearLayout.LayoutParams cp =
                    new LinearLayout.LayoutParams(0,dp(100),1);
                cp.setMargins(dp(3),dp(3),dp(3),dp(3));
                row.addView(card,cp);

                card.setOnClickListener(v -> openCategory(index));
            }
            grid.addView(row);
        }

        resultBox = new LinearLayout(this);
        resultBox.setOrientation(1);
        resultBox.setPadding(dp(15),dp(15),dp(15),dp(15));
        resultBox.setBackgroundColor(Color.WHITE);
        resultBox.setVisibility(View.GONE);
        root.addView(resultBox);

        resultTitle=text("",19,GREEN);
        resultBox.addView(resultTitle);

        resultText=text("",14,Color.DKGRAY);
        resultText.setPadding(0,dp(10),0,dp(10));
        resultBox.addView(resultText);

        Button open = new Button(this);
        open.setText("সংশ্লিষ্ট ফোন সেটিংস খুলুন");
        open.setOnClickListener(v -> openActualSetting(selected));
        resultBox.addView(open);

        TextView footer=text("Developed by Sabbir Ahmed",12,Color.GRAY);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0,dp(25),0,dp(10));
        root.addView(footer);
    }

    String selected = "";

    void showResult(String title, String message) {
        selected=title;
        resultTitle.setText(title);
        resultText.setText(message);
        resultBox.setVisibility(View.VISIBLE);
    }

    void openCategory(int index) {
        String name=names[index];
        showResult(name+" সেটিংস",
            name+"-এর সেটিংস পৃষ্ঠা খোলার জন্য নিচের বাটনে চাপ দিন।");
    }

    void searchProblem() {
        String q=search.getText().toString().toLowerCase().trim();
        if(q.isEmpty()) {
            Toast.makeText(this,"সমস্যা লিখুন",Toast.LENGTH_SHORT).show();
            return;
        }

        String[] keys={
            "নোটিফিকেশন","notification","message",
            "wifi","ওয়াইফাই","ওয়াইফাই",
            "bluetooth","ব্লুটুথ",
            "সাউন্ড","sound","ভলিউম",
            "ব্যাটারি","battery",
            "ডিসপ্লে","display",
            "লোকেশন","location",
            "ইন্টারনেট","internet",
            "স্টোরেজ","storage"
        };

        int[] indexes={0,0,0,1,1,1,3,3,4,4,4,5,5,6,6,7,7,2,2,10,10};

        for(int i=0;i<keys.length;i++) {
            if(q.contains(keys[i])) {
                openCategory(indexes[i]);
                return;
            }
        }

        showResult("সমস্যা শনাক্ত হয়নি",
            "অন্য শব্দ দিয়ে চেষ্টা করুন অথবা সেটিংসের বিভাগ নির্বাচন করুন।");
    }

    void voiceSearch() {
        try {
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"bn-BD");
            startActivityForResult(intent,100);
        } catch(Exception e) {
            Toast.makeText(this,"ভয়েস সার্চ পাওয়া যাচ্ছে না",
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==100 && resultCode==RESULT_OK && data!=null) {
            ArrayList<String> results=data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS);
            if(results!=null && !results.isEmpty()) {
                search.setText(results.get(0));
                searchProblem();
            }
        }
    }

    void openActualSetting(String name) {
        Intent intent=new Intent(Settings.ACTION_SETTINGS);

        try {
            switch(name) {
                case "নোটিফিকেশন সেটিংস":
                case "নোটিফিকেশন":
                    intent=new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
                    break;

                case "Wi-Fi সেটিংস":
                case "Wi-Fi":
                    intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                    break;

                case "Bluetooth সেটিংস":
                case "Bluetooth":
                    intent=new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    break;

                case "সাউন্ড সেটিংস":
                case "সাউন্ড":
                    intent=new Intent(Settings.ACTION_SOUND_SETTINGS);
                    break;

                case "ব্যাটারি সেটিংস":
                case "ব্যাটারি":
                    intent=new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                    break;

                case "ডিসপ্লে সেটিংস":
                case "ডিসপ্লে":
                    intent=new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                    break;

                case "লোকেশন সেটিংস":
                case "লোকেশন":
                    intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    break;

                case "নিরাপত্তা সেটিংস":
                case "নিরাপত্তা":
                    intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    break;

                case "অ্যাপস সেটিংস":
                case "অ্যাপস":
                    intent=new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                    break;

                case "স্টোরেজ সেটিংস":
                case "স্টোরেজ":
                    intent=new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                    break;

                case "ভাষা ও কিবোর্ড সেটিংস":
                case "ভাষা ও কিবোর্ড":
                    intent=new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                    break;

                case "তারিখ ও সময় সেটিংস":
                case "তারিখ ও সময়":
                    intent=new Intent(Settings.ACTION_DATE_SETTINGS);
                    break;

                case "Accessibility সেটিংস":
                case "Accessibility":
                    intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    break;

                case "Do Not Disturb সেটিংস":
                case "Do Not Disturb":
                    intent=new Intent(Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS);
                    break;

                case "ডিফল্ট অ্যাপ সেটিংস":
                case "ডিফল্ট অ্যাপ":
                    intent=new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                    break;

                case "অ্যাকাউন্ট সেটিংস":
                case "অ্যাকাউন্ট":
                    intent=new Intent(Settings.ACTION_SYNC_SETTINGS);
                    break;

                case "ব্যাকআপ সেটিংস":
                case "ব্যাকআপ":
                    intent=new Intent(Settings.ACTION_PRIVACY_SETTINGS);
                    break;

                case "Developer Options সেটিংস":
                case "Developer Options":
                    intent=new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                    break;

                case "System Update সেটিংস":
                case "System Update":
                    intent=new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
                    break;

                case "SIM সেটিংস":
                case "মোবাইল নেটওয়ার্ক":
                case "SIM":
                    intent=new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    break;

                case "Hotspot সেটিংস":
                case "Hotspot":
                case "Airplane Mode সেটিংস":
                case "Airplane Mode":
                    intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    break;

                case "গোপনীয়তা সেটিংস":
                case "গোপনীয়তা":
                    intent=new Intent(Settings.ACTION_PRIVACY_SETTINGS);
                    break;

                default:
                    intent=new Intent(Settings.ACTION_SETTINGS);
            }

            startActivity(intent);

        } catch(Exception e) {
            try {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            } catch(Exception ex) {
                Toast.makeText(this,"সেটিংস খোলা যায়নি",
                    Toast.LENGTH_SHORT).show();
            }
        }
    }
            }
          
