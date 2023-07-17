package com.rhdevelopers.summit.workshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhdevelopers.summit.workshop.BackendRegistry.Backend;
import com.rhdevelopers.summit.workshop.BackendRegistry.Coordinates;

@Path("fake/")
public class FakeBackendResource {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final Backend FAKE_BACKEND_INFO = new Backend(
        "fake-backend",
        "WeAreDevs POI",
        new Coordinates(0.0, 0.0),
        4
    );
    
    public static final List<PoiRecord> POI_RECORDS = new ArrayList<>();

    @PostConstruct
    void initializePoiRecords() {
        POI_RECORDS.add(new PoiRecord("Red Hat Headquarters", "Raleigh,NC,USA", List.of(35.787743, -78.644257)));
        POI_RECORDS.add(new PoiRecord("Red Hat Summit","Boston,MA,USA", List.of(42.361145, -71.057083)));
    }

    @GET
    @Path("ws/info")
    public Response getInfo() throws IOException {
        return Response.ok(
            OBJECT_MAPPER.writeValueAsString(FAKE_BACKEND_INFO),
            MediaType.APPLICATION_JSON
        ).build();
    }

    @GET
    @Path("poi/find/all")
    public Response getAllDataPoints() throws IOException {
        return Response.ok(
            OBJECT_MAPPER.writeValueAsString(POI_RECORDS),
            MediaType.APPLICATION_JSON
        ).build();
    }

    @GET
    @Path("poi/find/{id}")
    public Response getOneDataPoint(@PathParam("id") int id) throws IOException {
        if(id < 0 || id >= POI_RECORDS.size()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("no data point with id "+id+" found").build();
        }
        return Response.ok(
            OBJECT_MAPPER.writeValueAsString(POI_RECORDS.get(id)),
            MediaType.APPLICATION_JSON
        ).build();
    }

    //Uncomment the Java method below to add an API endpoint to add your own data points.
    
    /*
    @POST
    @Path("poi/add/point")
    public Response loadCustomPoi(PoiRecord record) {
        POI_RECORDS.add(record);
        return Response.created(
            UriBuilder.fromPath("fake/poi/find/"+(POI_RECORDS.size()-1)).build()
        ).entity("inserted custom data point").build();
    }
    */

    //How to use it:
    //The coordinates number array in the JSON snippet is specified [lat,lng] -> so latitude first, then longitude.
    //1. You can retrieve the GPS coordinates e.g. for your home town or some other city from here https://www.latlong.net/
    //2. You can use curl to send the actual POST request against the gateway's API endpoint like so

    /*

    curl --location 'http://localhost:8080/fake/poi/add/point/' \
    --header 'Content-Type: application/json' \
    --data '{
    "name":"Milano, Italy",
    "description":"example city",
    "coordinates":[45.464203,9.189982]
    }'

    curl --location 'http://localhost:8080/fake/poi/add/point/' \
    --header 'Content-Type: application/json' \
    --data '{
    "name":"Graz, Austria",
    "description":"my home town",
    "coordinates":[47.070713,15.439504]
    }'

    curl --location 'http://localhost:8080/fake/poi/add/point/' \
    --header 'Content-Type: application/json' \
    --data '{
    "name":"Berlin, Germany",
    "description":"We Are Developers 2023",
    "coordinates":[52.520008,13.404954]
    }'

    */
    

}
