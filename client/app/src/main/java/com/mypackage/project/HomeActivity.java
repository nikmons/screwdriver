package com.mypackage.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<DeviceModel> devicesList = new ArrayList<>();
    private List<CourierModel> courierModelList = new ArrayList<>();
    private List<CourierModel> currentCourierModelList = new ArrayList<>();
    private List<CustomerModel> customersList = new ArrayList<>();
    private List<DeviceModel> currentDevices = new ArrayList<>();
    private List<Integer> issueIds = new ArrayList<>();
    private CustomerModel[] currentCustomers;
    private int h, w;
    private TextView average, total_customers, total_devices, total_issues;
    private RecyclerView recyclerView;
    private RecyclerViewTechnicianAdapter technicianAdapter;
    private RecyclerViewCourierAdapter courierAdapter;
    private RecyclerViewHelpDeskAdapter helpDeskAdapter;
    private Menu menu;
    private int selectedId;
    private boolean isCourier = false, isQA = false, isTechnician = true;
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
        setTitle(parts[3]);
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
        String json = null;
        try {
            json = new Helper.Get(rl, parts, "employees/" + Integer.parseInt(parts[2]) + "/roles").execute().get();
            RoleModel[] roleModels = gson.fromJson(json, RoleModel[].class);
            String roles = String.valueOf(roleModels[0].Role_id);
            if (roleModels[0].Role_id == 3)
                roles += ", 1";
            for (int i = 1; i < roleModels.length; i++) {
                roles += ", " + String.valueOf(roleModels[i].Role_id);
                if (roleModels[i].Role_id == 3)
                    roles += ", 1";
            }
            String[] userParts = roles.split(",");
            int min = Integer.parseInt(userParts[0]);
            if (min == 4 || min == 5) {
                min = 1;
                if (min == 4)
                    isCourier = true;
                else
                    isQA = true;
            }
            MenuItem menuItem;
            for (int i = 0; i < userParts.length; i++) {
                if (Integer.parseInt(userParts[i].trim()) == 3) {
                    menuItem = menu.getItem(3);
                    menuItem.setVisible(true);
                    isTechnician = false;
                }
                if (Integer.parseInt(userParts[i].trim()) == 4 || Integer.parseInt(userParts[i].trim()) == 5) {
                    if (Integer.parseInt(userParts[i].trim()) == 4)
                        isCourier = true;
                    else
                        isQA = true;
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
                        courierModelList = new ArrayList<>();
                        for (CourierModel itemModel : currentCourierModelList) {
                            if (itemModel.trackingNumber.toLowerCase().startsWith(newText.toLowerCase()))
                                courierModelList.add(itemModel);
                        }
                        courierAdapter = new RecyclerViewCourierAdapter(courierModelList);
                        recyclerView.setAdapter(courierAdapter);
                        menu.getItem(0).setTitle("Devices (" + courierModelList.size() + ")");
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
                courierAdapter = new RecyclerViewCourierAdapter(courierModelList);
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
                    if (isTechnician || isCourier || isQA) {
                        Intent intent = new Intent(homeActivity, QRCodeActivity.class);
                        intent.putExtra("issueId", issueIds.get(position));
                        startActivityForResult(intent, 1);
                    }
                }

                @Override
                public void onLongClick(View view, final int position) {
                    if (selectedId == R.id.nav_customers) {
                        final CustomerModel model = customersList.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to delete the selected customer?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    String res = new Helper.Delete(rl, parts, "customers", model.Cust_id).execute().get();
                                    if (res.contains("true")) {
                                        toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                                        toastMessage.setText("Deleted Successfully!");
                                        toast.setView(toastMessage);
                                        toast.show();
                                        for (CustomerModel item : customersList) {
                                            if (item.Cust_id == model.Cust_id) {
                                                customersList.remove(item);
                                                break;
                                            }
                                        }
                                        menu.getItem(2).setTitle("Customers (" + customersList.size() + ")");
                                        helpDeskAdapter.notifyDataSetChanged();
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
                    } else if (!isTechnician && !isCourier && !isQA) {
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
                if (isCourier)
                    getDevicesForCourier();
                else
                    getDevicesForTechnician();
                if (!isTechnician && !isCourier) {
                    getCustomers();
                }
            } else if (min == 2) {
                startStatisticsActivity();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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


        if (id == R.id.refresh) {
            if (!Helper.isOnline(getApplicationContext())) {
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText("No internet connection!");
                toast.setView(toastMessage);
                toast.show();
            } else {
                if (selectedId == R.id.nav_statistics)
                {
                    getStatistics();
                }
                else {
                    if (isCourier)
                        getDevicesForCourier();
                    else
                        getDevicesForTechnician();
                    if (!isTechnician && !isCourier) {
                        getCustomers();
                        if (selectedId == R.id.nav_customers) {
                            helpDeskAdapter = new RecyclerViewHelpDeskAdapter(customersList);
                            recyclerView.setAdapter(helpDeskAdapter);
                        }
                    }
                }
            }
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
            } else if (id == R.id.nav_insert_device) {
                Intent intent = new Intent(this, InsertDeviceActivity.class);
                startActivityForResult(intent, 2);
            } else if (id == R.id.nav_insert_problem) {
                Intent intent = new Intent(this, InsertProblemActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_insert_customer) {
                Intent intent = new Intent(this, InsertCustomerActivity.class);
                startActivityForResult(intent, 3);
            } else if (id == R.id.nav_insert_state) {
                Intent intent = new Intent(this, InsertIssueActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_customers) {
                helpDeskAdapter = new RecyclerViewHelpDeskAdapter(customersList);
                recyclerView.setAdapter(helpDeskAdapter);
            } else {
                if (!Helper.isOnline(getApplicationContext())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No internet connection!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void getDevicesForTechnician() {
        try {
            currentDevices = new ArrayList<>();
            if (!isTechnician)
            {
                String json = new Helper.Get(rl, parts, "devices").execute().get();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                    DeviceModel devModel = gson.fromJson(obj.toString(), DeviceModel.class);
                    currentDevices.add(devModel);
                }
            }
            else {
                String res = new Helper.Get(rl, parts, "myissues").execute().get();
                JSONArray jsonArray = new JSONArray(res);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                    issueIds.add(obj.getInt("Issue_id"));
                    String result = new Helper.Get(rl, parts, "devices/" + obj.getInt("Dev_id")).execute().get();
                    DeviceModel devModel = gson.fromJson(result, DeviceModel.class);
                    currentDevices.add(devModel);
                }
            }
            devicesList = new ArrayList<>();
            for (DeviceModel itemModel : currentDevices) {
                devicesList.add(itemModel);
            }
            technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
            recyclerView.setAdapter(technicianAdapter);
            menu.getItem(0).setTitle("Devices (" + currentDevices.size() + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDevicesForCourier() {
        try {
            currentCourierModelList = new ArrayList<>();
            String res = new Helper.Get(rl, parts, "myissues").execute().get();
            JSONArray jsonArray = new JSONArray(res);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                issueIds.add(obj.getInt("Issue_id"));
                CourierModel model = new CourierModel();
                String result = new Helper.Get(rl, parts, "customers/" + obj.getInt("Cust_id")).execute().get();
                CustomerModel custModel = gson.fromJson(result, CustomerModel.class);
                model.address = custModel.Cust_Address_Name;
                model.nameSurname = custModel.Cust_First_Name + " " + custModel.Cust_Last_Name;

                result = new Helper.Get(rl, parts, "issues/" + obj.getInt("Issue_id")).execute().get();
                JSONObject objRes = new JSONObject(result);
                model.trackingNumber = objRes.getString("Issue_Track_Num");
                currentCourierModelList.add(model);
            }
            courierModelList = new ArrayList<>();
            for (CourierModel itemModel : currentCourierModelList) {
                courierModelList.add(itemModel);
            }
            courierAdapter = new RecyclerViewCourierAdapter(courierModelList);
            recyclerView.setAdapter(courierAdapter);
            menu.getItem(0).setTitle("Devices (" + currentCourierModelList.size() + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startStatisticsActivity() {
        Helper helper = new Helper();
        String[] parts = helper.getPrefs(this);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
            average = new TextView(this);
            total_customers = new TextView(this);
            total_devices = new TextView(this);
            total_issues = new TextView(this);
            Display display = getWindowManager().getDefaultDisplay();
            w = display.getWidth();
            h = display.getHeight();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.getLayoutParams().height = h * 95 / 100;
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.getIndeterminateDrawable().setColorFilter(
                    getResources().getColor(R.color.colorPrimaryDark),
                    PorterDuff.Mode.SRC_IN);
            RelativeLayout.LayoutParams relativeParams;
            toastMessage = new TextView(this);
            toastMessage.setTextColor(Color.WHITE);
            toastMessage.setTypeface(null, Typeface.BOLD);
            toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
            toastMessage.setTextSize((float) (w * 2 / 100));
            toast = Toast.makeText(getApplicationContext(), null,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
            rl.addView(average);
            rl.addView(total_customers);
            rl.addView(total_devices);
            rl.addView(total_issues);
            relativeParams = (RelativeLayout.LayoutParams) average.getLayoutParams();
            relativeParams.setMargins(w * 10 / 100, h * 25 / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            average.setLayoutParams(relativeParams);
            average.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);

            relativeParams = (RelativeLayout.LayoutParams) total_customers.getLayoutParams();
            relativeParams.setMargins(w * 10 / 100, h * 35 / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            total_customers.setLayoutParams(relativeParams);
            total_customers.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);

            relativeParams = (RelativeLayout.LayoutParams) total_devices.getLayoutParams();
            relativeParams.setMargins(w * 10 / 100, h * 45 / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            total_devices.setLayoutParams(relativeParams);
            total_devices.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);

            relativeParams = (RelativeLayout.LayoutParams) total_issues.getLayoutParams();
            relativeParams.setMargins(w * 10 / 100, h * 55 / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            total_issues.setLayoutParams(relativeParams);
            total_issues.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            getStatistics();
    }

    private void getStatistics()
    {
        String res = null;
        try {
            res = new Helper.Get(rl, parts, "statistics").execute().get();
            JSONObject obj = new JSONObject(res);
            obj = new JSONObject(obj.getString("stats"));
            JSONObject json = new JSONObject(obj.getString("avg_issue_lifetime"));
            average.setText("AVG: " + json.getString("days") + " days, " + json.getString("hours") + " hours, " + json.getString("minutes") + " minutes");
            total_customers.setText("Total Customers: " + obj.getString("total_customers"));
            total_devices.setText("Total Devices: " + obj.getString("total_devices"));
            total_issues.setText("Total Issues: " + obj.getString("total_issues"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCustomers() {
        try {
            String json = new Helper.Get(rl, parts, "customers").execute().get();
            currentCustomers = gson.fromJson(json, CustomerModel[].class);
            customersList = new ArrayList<>();
            for (CustomerModel itemModel : currentCustomers) {
                customersList.add(itemModel);
            }
            menu.getItem(2).setTitle("Customers (" + currentCustomers.length + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == 1) {
                if (isCourier)
                    getDevicesForCourier();
                else
                    getDevicesForTechnician();
            } else if (resultCode == 2 || resultCode == 3) {
                if (resultCode == 2) {
                    menu.getItem(0).setChecked(true);
                    selectedId = menu.getItem(0).getItemId();
                    getDevicesForTechnician();
                } else {
                    menu.getItem(2).setChecked(true);
                    selectedId = menu.getItem(2).getItemId();
                    getCustomers();
                    helpDeskAdapter = new RecyclerViewHelpDeskAdapter(customersList);
                    recyclerView.setAdapter(helpDeskAdapter);
                }
            }
        }
    }
}