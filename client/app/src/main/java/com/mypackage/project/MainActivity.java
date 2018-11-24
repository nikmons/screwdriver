package com.mypackage.project;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AlertDialogRadio.AlertPositiveListener {

    private List<DevicesToRepairModel> devicesList = new ArrayList<>();
    private List<DevicesToRepairModel> currentDevices = new ArrayList<>();
    private List<DevicesToRepairModel> pendingDevices = new ArrayList<>();
    private List<DevicesToRepairModel> notFixedDevices = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewTechnicianAdapter technicianAdapter;
    private RecyclerViewCourierAdapter courierAdapter;
    private Menu menu;
    private int recyclerViewPosition, selectedId;
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
        userModel.roles = "1";
        String[] parts = userModel.roles.split(",");
        int min = Integer.parseInt(parts[0]);
        if (min == 4) {
            min = 1;
            isCourier = true;
        }
        MenuItem menuItem;
        for (int i = 0; i < parts.length; i++) {
            if (Integer.parseInt(parts[i].trim()) == 4) {
                menuItem = menu.getItem(0);
                menu.getItem(0).getSubMenu().getItem(1).setVisible(false);
                menu.getItem(0).getSubMenu().getItem(2).setVisible(false);
                isCourier = true;
            } else {
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
                if (selectedId != R.id.nav_notFixed) {
                    recyclerViewPosition = position;
                    DevicesToRepairModel devicesToRepairModel = devicesList.get(position);
                    Intent intent = new Intent(mainActivity, QRCodeActivity.class);
                    intent.putExtra("serviceId", devicesToRepairModel.serviceId);
                    if (isCourier)
                        intent.putExtra("deviceStateOrTrackingNumber", devicesToRepairModel.trackingNumber);
                    else
                        intent.putExtra("deviceStateOrTrackingNumber", devicesToRepairModel.state);
                    String newState;
                    if (devicesToRepairModel.currentState.equals("state1"))
                        newState = "state2";
                    else
                        newState = "state3";
                    intent.putExtra("newState", newState);
                    intent.putExtra("isCourier", isCourier);
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (selectedId != R.id.nav_notFixed && !isCourier) {
                    recyclerViewPosition = position;
                    changeDeviceState();
                }
            }
        }));
        menu.getItem(min - 1).setChecked(true);
        selectedId = menu.getItem(min - 1).getItemId();
        if (min == 1) {
            selectedId = menu.getItem(0).getSubMenu().getItem(0).getItemId();
            menu.getItem(0).getSubMenu().getItem(0).setChecked(true);
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
                menu.getItem(0).getSubMenu().getItem(0).setChecked(false);
            }
            selectedId = id;
            if (id == R.id.nav_current) {
                devicesList = new ArrayList<>();
                for (DevicesToRepairModel itemModel : currentDevices) {
                    devicesList.add(itemModel);
                }
                technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                recyclerView.setAdapter(technicianAdapter);
            } else if (id == R.id.nav_pending) {
                devicesList = new ArrayList<>();
                for (DevicesToRepairModel itemModel : pendingDevices) {
                    devicesList.add(itemModel);
                }
                technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                recyclerView.setAdapter(technicianAdapter);
            } else if (id == R.id.nav_notFixed) {
                devicesList = new ArrayList<>();
                for (DevicesToRepairModel itemModel : notFixedDevices) {
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
        devicesToRepairModel.currentState = "state1";
        devicesToRepairModel.state = "current";
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "10/11/2018";
        list.add(devicesToRepairModel);

        devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 2;
        devicesToRepairModel.device = "LG G1";
        devicesToRepairModel.state = "current";
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.currentState = "state3";
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "20/11/2018";
        list.add(devicesToRepairModel);

        devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 3;
        devicesToRepairModel.device = "LG G2";
        devicesToRepairModel.state = "pending";
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.currentState = "state1";
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "20/11/2018";
        list.add(devicesToRepairModel);

        devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 3;
        devicesToRepairModel.device = "LG G3";
        devicesToRepairModel.state = "current";
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.trackingNumber = "GB25678890";
        devicesToRepairModel.address = "Αγίας Φωτεινής 60";
        devicesToRepairModel.name_surname = "Νίκος Χανταπάκης";
        devicesToRepairModel.currentState = "state2";
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "20/11/2018";
        list.add(devicesToRepairModel);

        devicesToRepairModel = new DevicesToRepairModel();
        devicesToRepairModel.serviceId = 3;
        devicesToRepairModel.device = "LG G4";
        devicesToRepairModel.state = "current";
        devicesToRepairModel.deliveredToHome = 1;
        devicesToRepairModel.currentState = "state2";
        devicesToRepairModel.trackingNumber = "GB14566543";
        devicesToRepairModel.address = "Λεωφ. Αλεξάνδρας 83";
        devicesToRepairModel.name_surname = "Κωνσταντίνος Μανωλάτος";
        devicesToRepairModel.problem = "Δεν λειουργει η οθονη, προβλημα με την καμερα και μερικες φορες κανει επανεκινηησ απο μονμο του";
        devicesToRepairModel.endDate = "20/11/2018";
        list.add(devicesToRepairModel);

        for (DevicesToRepairModel item : list) {
            if (!isCourier) {
                if ((!item.currentState.equals("state2") && item.deliveredToHome == 0) || (!item.currentState.equals("state3") && item.deliveredToHome == 1)) {
                    if (item.state.equals("current"))
                        currentDevices.add(item);
                    else if (item.state.equals("pending"))
                        pendingDevices.add(item);
                    else
                        notFixedDevices.add(item);
                }
            } else {
                if (item.currentState.equals("state2") && item.deliveredToHome == 1) {
                    currentDevices.add(item);
                }
            }
        }
        devicesList = currentDevices;
        if (isCourier) {
            menu.getItem(0).getSubMenu().getItem(0).setTitle("Devices (" + currentDevices.size() + ")");
            courierAdapter = new RecyclerViewCourierAdapter(devicesList);
            recyclerView.setAdapter(courierAdapter);
        } else {
            updateDevicesMenuCounts();
            technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
            recyclerView.setAdapter(technicianAdapter);
        }
    }

    private void updateDevicesMenuCounts() {
        menu.getItem(0).getSubMenu().getItem(0).setTitle("Current (" + currentDevices.size() + ")");
        menu.getItem(0).getSubMenu().getItem(1).setTitle("Pending (" + pendingDevices.size() + ")");
        menu.getItem(0).getSubMenu().getItem(2).setTitle("Not Fixed (" + notFixedDevices.size() + ")");
    }

    private void changeDeviceState() {
        if (selectedId == R.id.nav_current) {
            FragmentManager manager = getFragmentManager();
            AlertDialogRadio alert = new AlertDialogRadio();
            Bundle b = new Bundle();
            b.putInt("position", 0);
            alert.setArguments(b);
            alert.show(manager, "alert_dialog_radio");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure the device cannot be fixed?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    DevicesToRepairModel devicesToRepairModel = devicesList.get(recyclerViewPosition);
                    devicesToRepairModel.state = "not fixed";
                    notFixedDevices.add(devicesToRepairModel);
                    pendingDevices.remove(recyclerViewPosition);
                    devicesList = new ArrayList<>();
                    for (DevicesToRepairModel itemModel : pendingDevices) {
                        devicesList.add(itemModel);
                    }
                    updateDevicesMenuCounts();
                    technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                    recyclerView.setAdapter(technicianAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            long serviceId = data.getLongExtra("serviceId", -1);
            if (serviceId == -1) {
                if (selectedId == R.id.nav_pending) {
                    DevicesToRepairModel devicesToRepairModel = devicesList.get(recyclerViewPosition);
                    devicesToRepairModel.state = "not fixed";
                    notFixedDevices.add(devicesToRepairModel);
                    pendingDevices.remove(recyclerViewPosition);
                    devicesList = new ArrayList<>();
                    for (DevicesToRepairModel itemModel : pendingDevices) {
                        devicesList.add(itemModel);
                    }
                    updateDevicesMenuCounts();
                    technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                    recyclerView.setAdapter(technicianAdapter);
                } else {
                    changeDeviceState();
                }
            } else {
                for (DevicesToRepairModel item : devicesList) {
                    if (item.serviceId == serviceId) {
                        devicesList.remove(item);
                        if (selectedId == R.id.nav_current) {
                            currentDevices.remove(item);
                        } else {
                            pendingDevices.remove(item);
                        }
                        break;
                    }
                }
                if (isCourier) {
                    courierAdapter.notifyDataSetChanged();
                    menu.getItem(0).getSubMenu().getItem(0).setTitle("Devices (" + currentDevices.size() + ")");
                } else {
                    technicianAdapter.notifyDataSetChanged();
                    updateDevicesMenuCounts();
                }
            }
        }
    }

    @Override
    public void onPositiveClick(final int position) {
        final int pos = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DevicesToRepairModel devicesToRepairModel = devicesList.get(recyclerViewPosition);
                if (pos == 0) {
                    devicesToRepairModel.state = "pending";
                    pendingDevices.add(devicesToRepairModel);
                } else {
                    devicesToRepairModel.state = "not fixed";
                    notFixedDevices.add(devicesToRepairModel);
                }
                currentDevices.remove(recyclerViewPosition);
                devicesList = new ArrayList<>();
                for (DevicesToRepairModel itemModel : currentDevices) {
                    devicesList.add(itemModel);
                }
                updateDevicesMenuCounts();
                technicianAdapter = new RecyclerViewTechnicianAdapter(devicesList);
                recyclerView.setAdapter(technicianAdapter);
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
