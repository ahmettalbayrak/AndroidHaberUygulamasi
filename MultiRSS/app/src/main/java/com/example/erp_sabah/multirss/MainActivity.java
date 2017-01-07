package com.example.erp_sabah.multirss;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Haber> dS = new ArrayList<>();

    public static boolean enableDH = true, enableMilliyet = true;
    ProgressDialog pd;
    ListView lv;
    BaseAdapter ba;
    LayoutInflater li;
    int nid = 1;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);
        li = LayoutInflater.from(this);

        ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return dS.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup)
            {
                if (view == null)
                    view = li.inflate(android.R.layout.simple_list_item_1, null);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                Haber h = dS.get(i);

                tv.setText(h.baslik);

                if (h.from.equals("dh"))
                    tv.setTextColor(Color.BLUE);
                else
                tv.setTextColor(Color.RED);
                return tv;
            }
        };

        lv.setAdapter(ba);

        fetchData();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("RSS Seçimi")
                .setIcon(android.R.drawable.ic_menu_myplaces)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("Yenile")
                .setIcon(android.R.drawable.ic_menu_rotate)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        String ne = ""+item.getTitle();

        if (ne.equals("RSS Seçimi"))
        {
            Intent i = new Intent(this, SecimActivity.class);
            startActivity(i);
        }

        if (ne.equals("Yenile"))
        {
            fetchData();
        }
        return super.onOptionsItemSelected(item);
    }

    void fetchData()
    {
        new AsyncTask<String,String,String>()
        {
            protected void onPreExecute()
            {
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Lütfen Bekleyin");
                pd.show();
            }


            protected String doInBackground(String... strings)
            {
                try
                {
                    dS.clear();
                    if (enableDH)
                    {
                        Log.e("x","DonanimHaber'den RSS Aliniyor");
                        Elements hl = Jsoup.connect("https://www.donanimhaber.com/rss/tum/")
                                .timeout(30000)
                                .userAgent("Mozilla")
                                .get()
                                .select("item");

                        for (Element hE : hl)
                        {
                            String baslik = hE.select("title").text();
                            String img = hE.select("enclosure").attr("url");
                            String url = hE.select("guid").text();
                            Haber h = new Haber(baslik,img,url);
                            h.from="dh";
                            Log.e("x","DH -> "+h.toString());
                            dS.add(h);
                        }

                    }

                    if (enableMilliyet)
                    {
                        Log.e("x","Milliyet'ten RSS Aliniyor");
                        Elements hl = Jsoup.connect("http://www.milliyet.com.tr/rss/rssNew/diyetRss.xml")
                                .timeout(30000)
                                .userAgent("Mozilla")
                                .get()
                                .select("item");

                        for (Element hE : hl)
                        {
                            String baslik = hE.select("title").text();
                            String img = hE.select("ImageURL").text();
                            String url = hE.select("link").text();
                            Haber h = new Haber(baslik,img,url);
                            h.from="milliyet";

                            Log.e("x","Milliyet -> "+h.toString());
                            dS.add(h);
                        }
                    }
                } catch (Exception e) { }
                return null;
            }


            protected void onPostExecute(String s)
            {
                ba.notifyDataSetChanged();
                pd.dismiss();
                showNotification("Haberler Alındı");
            }
        }.execute();
    }

    public void showNotification(String mesaj)
    {

        NotificationCompat.Builder nc = new NotificationCompat.Builder(this);
        NotificationManager nManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        nc.setTicker("Haber Sayısı : "+dS.size());
        nc.setContentTitle("Haber Sayısı : "+dS.size());
        nc.setContentText(mesaj);
        nc.setSmallIcon(R.mipmap.ic_launcher);
        nc.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        Notification bildirim = nc.build();

        nManager.notify(nid, bildirim);
        nid++;


    }
}
