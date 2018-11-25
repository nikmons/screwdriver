package com.mypackage.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public List<DevicesToRepairModel> devicesList = new ArrayList<>();
    private List<DevicesToRepairModel> currentDevices = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewTechnicianAdapter technicianAdapter;
    private RecyclerViewCourierAdapter courierAdapter;
    private Menu menu;
    private int selectedId;
    private boolean isCourier = false;
    private MainActivity mainActivity;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainActivity = this;
        setTitle("Username");
        // new GetDevicesForRepair(mainActivity).execute();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }
        UserModel userModel = new UserModel();
        userModel.roles = "2,1";
        String[] parts = userModel.roles.split(",");
        int min = Integer.parseInt(parts[0]);
        if (min == 4) {
            min = 1;
            isCourier = true;
        }
        MenuItem menuItem;
        for (int i = 0; i < parts.length; i++) {
            if (Integer.parseInt(parts[i].trim()) == 4) {
                isCourier = true;
                menuItem = menu.getItem(0);
            }
            else {
                menuItem = menu.getItem(Integer.parseInt(parts[i].trim()) - 1);
            }
            if (Integer.parseInt(parts[i].trim()) < min) {
                min = Integer.parseInt(parts[i].trim());
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
                    devicesList = new ArrayList<>();
                    for (DevicesToRepairModel itemModel : currentDevices) {
                        if (itemModel.trackingNumber.toLowerCase().startsWith(newText.toLowerCase()))
                            devicesList.add(itemModel);
                    }
                    courierAdapter = new RecyclerViewCourierAdapter(devicesList);
                    recyclerView.setAdapter(courierAdapter);
                    return false;
                }
            });
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) searchView.getLayoutParams();
            relativeParams.setMargins(width * -2 / 100, height * 2 / 100, 0, height * 3 / 100);
            relativeParams.height = height * 7 / 100;
            relativeParams.width = width;
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
                DevicesToRepairModel devicesToRepairModel = devicesList.get(position);
                Intent intent = new Intent(mainActivity, QRCodeActivity.class);
                intent.putExtra("serviceId", devicesToRepairModel.serviceId);
                if (!isCourier)
                    intent.putExtra("nextToAction", devicesToRepairModel.nextToAction);
                intent.putExtra("isCourier", isCourier);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        menu.getItem(min - 1).setChecked(true);
        selectedId = menu.getItem(min - 1).getItemId();
        if (min == 1) {
            selectedId = menu.getItem(0).getItemId();
            devicesToRepair();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            return true;
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id != selectedId) {
            if (firstTime) {
                firstTime = false;
                menu.getItem(0).setChecked(false);
            }
            selectedId = id;
            if (id == R.id.nav_current) {
                devicesList = new ArrayList<>();
                for (DevicesToRepairModel itemModel : currentDevices) {
                    devicesList.add(itemModel);
                }
                technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                recyclerView.setAdapter(technicianAdapter);
            } else if (id == R.id.nav_statistics) {

            } else if (id == R.id.nav_insert) {

            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void devicesToRepair() {
        List<DevicesToRepairModel> list = new ArrayList<>();

        DevicesToRepairModel devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 1;
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.device = "IPhone 7";
        devicesToRepairModel.trackingNumber = "GB25678890";
        devicesToRepairModel.address = "Αγίας Φωτεινής 60";
        devicesToRepairModel.name_surname = "Νίκος Χανταπάκης";
        devicesToRepairModel.nextToAction = 1;
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "10/11/2018";
        list.add(devicesToRepairModel);


        devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 3;
        devicesToRepairModel.device = "LG G2";
        devicesToRepairModel.trackingNumber = "GB25678890";
        devicesToRepairModel.address = "Αγίας Φωτεινής 60";
        devicesToRepairModel.name_surname = "Νίκος Χανταπάκης";
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.nextToAction = 1;
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "20/11/2018";
        list.add(devicesToRepairModel);

        devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 3;
        devicesToRepairModel.device = "LG G3";
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.trackingNumber = "GB25678890";
        devicesToRepairModel.address = "Αγίας Φωτεινής 60";
        devicesToRepairModel.name_surname = "Νίκος Χανταπάκης";
        devicesToRepairModel.nextToAction = 0;
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "20/11/2018";
        list.add(devicesToRepairModel);

        for (DevicesToRepairModel item : list) {
            currentDevices.add(item);
        }
        devicesList = currentDevices;
        if (isCourier) {
            courierAdapter = new RecyclerViewCourierAdapter(devicesList);
            recyclerView.setAdapter(courierAdapter);
        } else {
            technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
            recyclerView.setAdapter(technicianAdapter);
        }
        updateDevicesMenuCounts();
    }

    private void updateDevicesMenuCounts() {
        menu.getItem(0).setTitle("Devices (" + currentDevices.size() + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            long serviceId = data.getLongExtra("serviceId", -1);
            for (DevicesToRepairModel item : devicesList) {
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
            updateDevicesMenuCounts();
        }
    }
}
