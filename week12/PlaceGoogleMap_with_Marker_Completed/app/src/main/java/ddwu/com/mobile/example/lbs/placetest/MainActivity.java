package ddwu.com.mobile.example.lbs.placetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ddwu.mobile.place.placebasic.OnPlaceBasicResult;
import ddwu.mobile.place.placebasic.pojo.PlaceBasic;
import ddwu.mobile.place.placebasic.PlaceBasicManager;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "MainActivity";
    final static int PERMISSION_REQ_CODE = 100;

    private EditText etKeyword;

    //    Map & Place
    private GoogleMap mGoogleMap;
    private PlaceBasicManager placeBasicManager;
    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKeyword = findViewById(R.id.etKeyword);

        /* 1. PlaceBasicManager ?????? */
        placeBasicManager = new PlaceBasicManager(getString(R.string.api_key));
        /* 2. placeBasicManager.setOnPlaceBasicResult() ??????
         * ?????????  ??????/??????, ?????????, PlaceID ??? ???????????? ????????? ????????? ?????? ??????
         * placeID ??? Marker.setTag(placeID)??? ???????????? ??? ????????? ??????*/
        placeBasicManager.setOnPlaceBasicResult(onPlaceBasicResult);

        if (checkPermission()) mapLoad();

        // Places ????????? ??? ??????????????? ??????
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(this);
    }

    OnPlaceBasicResult onPlaceBasicResult = new OnPlaceBasicResult() {
        @Override
        public void onPlaceBasicResult(List<PlaceBasic> list) {
            for (PlaceBasic place : list) {
                MarkerOptions options = new MarkerOptions()
                        .title(place.getName())
                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                Marker marker = mGoogleMap.addMarker(options);
                /*?????? ????????? place_id ??? ????????? ????????? ??????*/
                marker.setTag(place.getPlaceId());
            }
        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                if (etKeyword.getText().toString().equals("??????")) {
                    searchStart(Double.parseDouble(getString(R.string.init_lat)),
                            Double.parseDouble(getString(R.string.init_lng)),
                            100, PlaceTypes.CAFE); // ?????? 100m ?????? ?????? ??????
                } else if (etKeyword.getText().toString().equals("??????")) {
                    searchStart(Double.parseDouble(getString(R.string.init_lat)),
                            Double.parseDouble(getString(R.string.init_lng)),
                            100, PlaceTypes.RESTAURANT); // ?????? 100m ?????? ?????? ??????
                }
                break;
        }
    }


    /*????????? ????????? ?????? ????????? ??????
     * PlaceBasicManager ??? ????????? type ??? ????????? PlaceBasic ??? ???????????? ???????????? ????????? ???????????? ?????? */
    private void searchStart(double lat, double lng, int radius, String type) {
        placeBasicManager.searchPlaceBasic(lat, lng, radius, type);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true); // ??? ?????? ?????? ?????????

        /*????????? InfoWindow ?????? ??? marker??? Tag ??? ????????? placeID ???
        * Google PlacesAPI ??? ???????????? ????????? ????????????*/
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
//                1. ???????????? Marker.getTag() ??? ???????????? placeID ??????
//                2. getPlaceDetail() ??? ???????????? Place ??????
//                3. callDetailActivity() ??? Place ????????? ???????????? DetailActivity ?????? (callDetailActivity() ??????)
                String placeId = marker.getTag().toString();    // ????????? setTag() ??? ????????? Place ID ??????
                getPlaceDetail(placeId);
            }
        });
    }


    /*Place ID ??? ????????? ?????? ???????????? ???????????? ??????
    * ????????? InfoWindow ?????? ??? ??????*/
    private Place getPlaceDetail(String placeId) {
        List<Place.Field> placeFields       // ??????????????? ????????? ????????? ?????? ??????
                = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();    // ?????? ??????

        // ?????? ?????? ??? ?????? ??????/?????? ????????? ??????
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override                    // ?????? ?????? ??? ?????? ????????? ??????
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {  // ?????? ?????? ???
                final Place place = fetchPlaceResponse.getPlace();
                Log.i(TAG, "Place found: " + place.getName());  // ?????? ??? ?????? ???
                Log.i(TAG, "Phone: " + place.getPhoneNumber());
                Log.i(TAG, "Address: " + place.getAddress());
                Log.i(TAG, "ID: " + place.getId());
                callDetailActivity(place);          // ?????? ????????? DetailActivity ??????
            }
        }).addOnFailureListener(new OnFailureListener() {   // ?????? ?????? ??? ?????? ????????? ??????
            @Override
            public void onFailure(@NonNull Exception exception) {   // ?????? ?????? ???
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();  // ?????? ??? ??????
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                }
            }
        });
        return null;
    }


//    Google PlacesAPI ??? place ????????? ?????? ?????? DetailActivity ??? ???????????? ???????????? ??????
    private void callDetailActivity(Place place) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("name",place.getName());
                        intent.putExtra("phone",place.getPhoneNumber());
                        intent.putExtra("address",place.getAddress());

        startActivity(intent);
    }



    /*???????????? ??????????????? ??????*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // ???????????? this: MainActivity ??? OnMapReadyCallback ??? ???????????????
    }


    /* ?????? permission ?????? */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                // ???????????? ??????????????? ?????? ??? ?????? ??????
                mapLoad();
            } else {
                // ????????? ????????? ??? ???????????? ??????
                Toast.makeText(this, "??? ????????? ?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




    /*????????? ?????? ????????? ?????? ????????? ???????????? ?????? ?????? ?????? ?????????*/

/*
    private void startCPSearch() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ID);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).

        if (checkPermission()) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                            Log.i(TAG, String.format("Place ID: %s", placeLikelihood.getPlace().getId()));
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getLikelihood()));
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        }
    }
*/

}
