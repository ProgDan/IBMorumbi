package com.progdan.ibmorumbi;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.progdan.ibmorumbi.route.GMapV2GetRouteDirection;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentMap extends Fragment implements LocationListener {
	// Google Map
	private GoogleMap googleMap;
	private int mapType = GoogleMap.MAP_TYPE_NORMAL;
	
	// Route calculation
	Document document;
	GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    MarkerOptions markerOptions;

	
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
		
		toPosition = new LatLng(latitude,longitude);

		// Changing Map Type
		googleMap.setMapType(mapType);
		
		// create marker
		MarkerOptions marker = new MarkerOptions()
										.position(new LatLng(latitude, longitude))
										.title("IB Morumbi")
										.snippet("Igreja Batista do Morumbi");

		// adding marker
		googleMap.addMarker(marker);
		
		v2GetRouteDirection = new GMapV2GetRouteDirection();
		
		// Enable all gestures on Map
		googleMap.getUiSettings().setAllGesturesEnabled(true);
		
		// Showing Current Location
		googleMap.setMyLocationEnabled(true);
		LocationManager lm = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		
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
		
		// Marca o item selecionado
		item.setChecked(true);
		
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
		case R.id.make_route:
			// Calculate Route
			GetRouteTask getRoute = new GetRouteTask();
			getRoute.execute();
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
	
	private class GetRouteTask extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		String response = "";
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Loading route...");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {
			// Get All Route values
			document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
			response = "Success";
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			googleMap.clear();
			
			// latitude and longitude
			double latitude = -23.633165;
			double longitude = -46.7394826;
			
			toPosition = new LatLng(latitude,longitude);

			// create marker
			MarkerOptions marker = new MarkerOptions()
											.position(toPosition)
											.title("IB Morumbi")
											.snippet("Igreja Batista do Morumbi");

			// adding marker
			googleMap.addMarker(marker);

			if(response.equalsIgnoreCase("Success")) {
				ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
				PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
				for(int i=0; i<directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}
				// Adding route on the map
				googleMap.addPolyline(rectLine);
				fixZoom(rectLine);
			}
			dialog.dismiss();
		}		
	}
	
	private void fixZoom(PolylineOptions route) {
		List<LatLng> points = route.getPoints();
		LatLngBounds.Builder bc = new LatLngBounds.Builder();
		for (LatLng item : points) {
	        bc.include(item);
	    }
		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
	}

	@Override
	public void onLocationChanged(Location location) {
		fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
