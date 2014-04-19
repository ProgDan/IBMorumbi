package com.progdan.ibmorumbi;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentMap extends Fragment {
	// Google Map
	private GoogleMap googleMap;
	private int mapType = GoogleMap.MAP_TYPE_NORMAL;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.map, container, false);
		setHasOptionsMenu(true);
				
		try {
			// Loading map
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		configureMap();
		
		return view;
	}
	
	/**
	 * function to load map. If map is not created it will create it for you
	 */
	private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
	}
	
	private void configureMap(){
		// latitude and longitude
		double latitude = -23.633165;
		double longitude = -46.7394826;

		// Changing Map Type
		googleMap.setMapType(mapType);
		
		// create marker
		MarkerOptions marker = new MarkerOptions()
										.position(new LatLng(latitude, longitude))
										.title("IB Morumbi")
										.snippet("Igreja Batista do Morumbi");

		// adding marker
		googleMap.addMarker(marker);
		
		// Enable all gestures on Map
		googleMap.getUiSettings().setAllGesturesEnabled(true);
		
		// Moving Camera to a Location with animation
		float cameraZoom = 16;		
		
/*		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(cameraZoom).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/		
		// Showing Current Location
		googleMap.setMyLocationEnabled(true);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initilizeMap();
		configureMap();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapView));
		FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
		googleMap = null;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.map_styles_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
		case R.id.normal_map:
			mapType = GoogleMap.MAP_TYPE_NORMAL;
			break;
		case R.id.satellite_map:
			mapType = GoogleMap.MAP_TYPE_SATELLITE;
			break;
		case R.id.terrain_map:
			mapType = GoogleMap.MAP_TYPE_TERRAIN;
			break;
		case R.id.hybrid_map:
			mapType = GoogleMap.MAP_TYPE_HYBRID;
			break;
		}
		googleMap.setMapType(mapType);
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// save the map type so when we change orientation, the mape type can be restored
		LatLng cameraLatLng = googleMap.getCameraPosition().target;
		float cameraZoom = googleMap.getCameraPosition().zoom;
		outState.putInt("map_type", mapType);
		outState.putDouble("lat", cameraLatLng.latitude);
		outState.putDouble("lng", cameraLatLng.longitude);
		outState.putFloat("zoom", cameraZoom);
	}
	
}
