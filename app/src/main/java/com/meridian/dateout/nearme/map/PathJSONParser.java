package com.meridian.dateout.nearme.map;

/**
 * Created by Anvin on 4/11/2017.
 */

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PathJSONParser {
GoogleMapModel gmm;
    public GoogleMapModel parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        ArrayList<String> html_instructions=new ArrayList<String>();
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                System.out.println("++++++++++++++First Route  ");
                gmm=new GoogleMapModel();
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                String distance=(String) ((JSONObject) ((JSONObject) jLegs.get(i)).get("distance")).get("text");
                String duration=(String) ((JSONObject) ((JSONObject) jLegs.get(i)).get("duration")).get("text");
                String start_address=(String) ((JSONObject) jLegs.get(i)).get("start_address");
                String end_address=(String) ((JSONObject) jLegs.get(i)).get("end_address");
                Double start_lat=(Double) ((JSONObject) ((JSONObject) jLegs.get(i)).get("start_location")).get("lat");
                Double start_lng=(Double) ((JSONObject) ((JSONObject) jLegs.get(i)).get("start_location")).get("lng");
                Double end_lat=(Double) ((JSONObject) ((JSONObject) jLegs.get(i)).get("end_location")).get("lat");
                Double end_lng=(Double) ((JSONObject) ((JSONObject) jLegs.get(i)).get("end_location")).get("lng");

                System.out.println("++++++++++++++DISTANCE : "+ distance);
                System.out.println("++++++++++++++DURATION : "+ duration);
                System.out.println("++++++++++++++start_address : "+ start_address);
                System.out.println("++++++++++++++end_address : "+ end_address);
                System.out.println("++++++++++++++start_lat : "+ start_lat);
                System.out.println("++++++++++++++start_lng : "+ start_lng);
                System.out.println("++++++++++++++end_lat : "+ end_lat);
                System.out.println("++++++++++++++end_lng : "+ end_lng);

                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");


                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("polyline")).get("points");

                        List<LatLng> list = decodePoly(polyline);

                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat",
                                    Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng",
                                    Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }

                        String instructions=(String) ((JSONObject) jSteps.get(k)).get("html_instructions");

                        html_instructions.add(instructions);

                    }
                    routes.add(path);
                }
                gmm.setDistance(distance);
                gmm.setDuration(duration);
                gmm.setRoutes(routes);
                gmm.setStart_address(start_address);
                gmm.setEnd_address(end_address);
                gmm.setStart_lat(Double.toString(start_lat));
                gmm.setStart_lng(Double.toString(start_lng));
                gmm.setEnd_lat(Double.toString(end_lat));
                gmm.setEnd_lng(Double.toString(end_lng));
                gmm.setHtml_instructions(html_instructions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gmm;
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}