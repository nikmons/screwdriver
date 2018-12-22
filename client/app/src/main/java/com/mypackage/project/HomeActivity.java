package com.mypackage.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public List<DeviceModel> devicesList = new ArrayList<>();
    private DeviceModel[] currentDevices;
    private int h, w;
    private RecyclerView recyclerView;
    private RecyclerViewTechnicianAdapter technicianAdapter;
    private RecyclerViewCourierAdapter courierAdapter;
    private Menu menu;
    private int selectedId, min;
    private boolean isCourier = false, isTechnician = true;
    private TextView toastMessage;
    private ProgressBar progressBar;
    private RelativeLayout rl;
    private Toast toast;
    private Gson gson;
    private Helper helper;
    private HomeActivity homeActivity;
    private String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Display display = getWindowManager().getDefaultDisplay();
        w = display.getWidth();
        h = display.getHeight();
        homeActivity = this;
        helper = new Helper();
        rl = (RelativeLayout) findViewById(R.id.rl);
        parts = helper.getPrefs(this);
        gson = new Gson();
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 2 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        for (int i = 0; i < menu.size() - 1; i++) {
            menu.getItem(i).setVisible(false);
        }
        UserModel userModel = new UserModel();
        userModel.roles = "3, 1";
        String[] userParts = userModel.roles.split(",");
        min = Integer.parseInt(userParts[0]);
        if (min == 4) {
            min = 1;
            isCourier = true;
        }
        MenuItem menuItem;
        for (int i = 0; i < userParts.length; i++) {
            if (Integer.parseInt(userParts[i].trim()) == 3)
            {
                isTechnician = false;
            }
            if (Integer.parseInt(userParts[i].trim()) == 4) {
                isCourier = true;
                menuItem = menu.getItem(0);
            } else {
                menuItem = menu.getItem(Integer.parseInt(userParts[i].trim()) - 1);
            }
            if (Integer.parseInt(userParts[i].trim()) < min) {
                min = Integer.parseInt(userParts[i].trim());
            }
            menuItem.setVisible(true);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        if (isCourier) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
            final SearchView searchView = new SearchView(this);
            rl.addView(searchView);
            searchView.setQueryHint("Tracking Number");
            searchView.setId(R.id.searchView);
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    /*devicesList = new ArrayList<>();
                    for (DeviceModel itemModel : currentDevices) {
                        if (itemModel.trackingNumber.toLowerCase().startsWith(newText.toLowerCase()))
                            devicesList.add(itemModel);
                    }
                    courierAdapter = new RecyclerViewCourierAdapter(devicesList);
                    recyclerView.setAdapter(courierAdapter);*/
                    return false;
                }
            });
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) searchView.getLayoutParams();
            relativeParams.setMargins(w * -2 / 100, h * 2 / 100, 0, h * 3 / 100);
            relativeParams.height = h * 7 / 100;
            relativeParams.width = w;
            searchView.setLayoutParams(relativeParams);
            relativeParams = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
            relativeParams.addRule(RelativeLayout.BELOW, R.id.searchView);
            courierAdapter = new RecyclerViewCourierAdapter(devicesList);
        } else {
            technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (isCourier)
            recyclerView.setAdapter(courierAdapter);
        else
            recyclerView.setAdapter(technicianAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DeviceModel deviceModel = devicesList.get(position);
                /*Intent intent = new Intent(mainActivity, QRCodeActivity.class);
                intent.putExtra("serviceId", deviceModel.serviceId);
                if (!isCourier)
                    intent.putExtra("nextToAction", deviceModel.nextToAction);
                intent.putExtra("isCourier", isCourier);
                startActivityForResult(intent, 1);*/
            }

            @Override
            public void onLongClick(View view, final int position) {
                if (!isTechnician) {
                    final DeviceModel deviceModel = devicesList.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete the selected device?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String res = new Helper.Delete(rl, parts, "devices", deviceModel.Dev_id).execute().get();
                                if (res.contains("true")) {
                                    toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                                    toastMessage.setText("Deleted Successfully!");
                                    toast.setView(toastMessage);
                                    toast.show();
                                    for (DeviceModel item : devicesList) {
                                        if (item.Dev_id == deviceModel.Dev_id) {
                                            devicesList.remove(item);
                                            break;
                                        }
                                    }
                                    menu.getItem(0).setTitle("Devices (" + devicesList.size() + ")");
                                    technicianAdapter.notifyDataSetChanged();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }));
        menu.getItem(min - 1).setChecked(true);
        selectedId = menu.getItem(min - 1).getItemId();
        if (min == 1) {
            selectedId = menu.getItem(0).getItemId();
            getDevices();
        }
        else
        {
            if (selectedId == R.id.nav_statistics) {

            } else if (selectedId == R.id.nav_insert) {
                Intent intent = new Intent(this, InsertDeviceActivity.class);
                intent.putExtra("min", min);
                startActivityForResult(intent, 2);

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            getDevices();
        } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Confirmation");
                alertDialogBuilder
                        .setMessage("Are you sure you want to exit application?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (Build.VERSION.SDK_INT >= 21) {
                                            finishAndRemoveTask();
                                        } else {
                                            finish();
                                        }
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id != selectedId) {
            selectedId = id;
            if (id == R.id.nav_current) {
                devicesList = new ArrayList<>();
                for (DeviceModel itemModel : currentDevices) {
                    devicesList.add(itemModel);
                }
                technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                recyclerView.setAdapter(technicianAdapter);
            } else if (id == R.id.nav_statistics) {

            } else if (id == R.id.nav_insert) {
                Intent intent = new Intent(this, InsertDeviceActivity.class);
                intent.putExtra("min", min);
                startActivityForResult(intent, 2);
            }
            else
            {
                try {
                    new Helper.Delete(rl, parts, "logout", -1).execute().get();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("access_token", "");
                    editor.putString("refresh_token", "");
                    editor.commit();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void getDevices()
    {
        try {
            String json = new Helper.Get(rl, parts, "devices").execute().get();
            currentDevices = gson.fromJson(json, DeviceModel[].class);
            devicesList = new ArrayList<>();
            for (DeviceModel itemModel : currentDevices) {
                devicesList.add(itemModel);
            }
            if (isCourier) {
                courierAdapter = new RecyclerViewCourierAdapter(devicesList);
                recyclerView.setAdapter(courierAdapter);
            } else {
                technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                recyclerView.setAdapter(technicianAdapter);
            }
            menu.getItem(0).setTitle("Devices (" + currentDevices.length + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == 2) {
                int min = data.getIntExtra("min", -1);
                if (min == 1)
                    menu.getItem(2).setChecked(false);
                    menu.getItem(0).setChecked(true);
                    selectedId = menu.getItem(0).getItemId();
                    getDevices();
            }
            /*long serviceId = data.getLongExtra("serviceId", -1);
            for (DeviceModel item : devicesList) {
                if (item.serviceId == serviceId) {
                    devicesList.remove(item);
                    if (selectedId == R.id.nav_current) {
                        currentDevices.remove(item);
                        break;
                    }
                }
            }
            if (isCourier) {
                courierAdapter.notifyDataSetChanged();
            } else {
                technicianAdapter.notifyDataSetChanged();
            }
            updateDevicesMenuCounts();*/
        }
    }
}