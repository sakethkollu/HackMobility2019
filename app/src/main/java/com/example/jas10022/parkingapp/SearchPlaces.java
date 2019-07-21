//package com.example.jas10022.parkingapp;
//
//import com.here.android.mpa.common.GeoCoordinate;
//import com.here.android.mpa.search.DiscoveryRequest;
//import com.here.android.mpa.search.DiscoveryResult;
//import com.here.android.mpa.search.DiscoveryResultPage;
//import com.here.android.mpa.search.ErrorCode;
//import com.here.android.mpa.search.Place;
//import com.here.android.mpa.search.PlaceLink;
//import com.here.android.mpa.search.ResultListener;
//import com.here.android.mpa.search.SearchRequest;
//
//public class SearchPlaces {
//
//    private DiscoveryResultPage mResultPage = null;
//
//    private double currentlat = MainActivity.currentLatitude;
//    private double currentlon = MainActivity.currentLongitude;
//
//    private GeoCoordinate destinable;
//
//    public SearchPlaces(String search){
//
//        GeoCoordinate presentLocal = new GeoCoordinate(currentlat, currentlon);
//
//        DiscoveryRequest request =
//                new SearchRequest(search).setSearchCenter(presentLocal);
//
//        // limit number of items in each result page to 10
//        request.setCollectionSize(1);
//
//    }
//
//    private void destCoord(){
//        DiscoveryResult st = mResultPage.getItems().get(0);
//        if (st.getResultType() == DiscoveryResult.ResultType.PLACE){
//            PlaceLink place = (PlaceLink) st;
//            destinable = place.getPosition();
//        }
//    }
//
//    public GeoCoordinate getDestinable(){
//        return destinable;
//    }
//
//    class SearchRequestListener implements ResultListener<DiscoveryResultPage> {
//
//        @Override
//        public void onCompleted(DiscoveryResultPage data, ErrorCode error) {
//            if (error != ErrorCode.NONE) {
//                // Handle error
//
//            } else {
//                // Store the last DiscoveryResultPage for later processing
//                mResultPage = data;
//
//            }
//        }
//    }
//}
//

/*

WHY TF IS THIS EVEN A THING

 */