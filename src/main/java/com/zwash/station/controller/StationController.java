package com.zwash.station.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zwash.common.dto.StationDTO;
import com.zwash.common.pojos.CarWashingProgram;
import com.zwash.common.pojos.Station;
import com.zwash.station.exceptions.StationNotCreatedException;
import com.zwash.station.exceptions.StationNotExistsException;
import com.zwash.station.service.StationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("v1/stations")
public class StationController {

	@Autowired
	private StationService stationService;

	@ApiOperation("Get all stations")
	@GetMapping
	public ResponseEntity<List<Station> > getAllStations() {
		  List<Station> list = stationService.getAllStations();
	        return ResponseEntity.ok(list);
	}
	

	@ApiOperation("Get all station washes")
	@GetMapping("/washes")
	public ResponseEntity<List<CarWashingProgram>> getStationWashes(@RequestParam("id") Long id) throws StationNotExistsException {
		//getStationWashed
		List<CarWashingProgram> list = stationService.getStationWashed(id);
	
		return ResponseEntity.ok(list);
	}
	
	@ApiOperation("Get a station by ID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = Station.class),
			@ApiResponse(code = 404, message = "Station not found") })
	@GetMapping("/{id}")
	public ResponseEntity<Station> getStationById(@PathVariable Long id) throws StationNotExistsException {
		Station station = stationService.getStation(id);
		return ResponseEntity.ok(station);
	}
	
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = Station.class),
		@ApiResponse(code = 404, message = "Station not found") })
@GetMapping("/service-provider/{id}")
public ResponseEntity<List<Station>> getStationByServiceProvider(@PathVariable Long id) throws StationNotExistsException {
	List<Station> stations = stationService.getAllServiceProviderStations(id);
	return ResponseEntity.ok(stations);
}


	@ApiOperation("Create a new station")
	@PostMapping("/")
	public ResponseEntity<String> createStation(@RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("serviceProvider") long serviceProvider
     
       ) 
         throws StationNotCreatedException, Exception {

	
		    StationDTO stationDTO= new StationDTO(name,address,latitude,longitude);
		    try {
		Station stationcreated = stationService.createStation( stationDTO, serviceProvider) ;
		    
		if(stationcreated instanceof Station)
		{
			return ResponseEntity.ok("Station created successfully");
		}
		    }catch(Exception ex)
		    {
		    	throw new StationNotCreatedException(ex.getMessage());
		    }
		return ResponseEntity.status(500).body("Station not created");
	}


	@ApiOperation("Update a station by ID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = Station.class),
		@ApiResponse(code = 404, message = "Station not found") })
	@PutMapping("/{id}")
	public ResponseEntity<Station> updateStation(@PathVariable Long id, @RequestBody Station station) throws StationNotExistsException {
		Station updatedStation = stationService.updateStation(station);

		if (updatedStation != null) {
			return ResponseEntity.ok(updatedStation); // Station was successfully updated
		} else {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build(); // Station was not updated
		}
	}
	@ApiOperation("Update the address of a station by ID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = Station.class),
	    @ApiResponse(code = 404, message = "Station not found") })
	@PutMapping("/address/{id}")
	public ResponseEntity<Station> updateStationAddress(@PathVariable Long id, @RequestBody Station newStation) throws StationNotExistsException {
	    Station station = stationService.getStation(id);

	    if (station != null) {
	  
           station.setLatitude(newStation.getLatitude());
           station.setLongitude(newStation.getLongitude());
	        // Save the updated station
	        station = stationService.updateStation(station);

	        return ResponseEntity.ok(station); // Station address was successfully updated
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Station not found
	    }
	}

	@ApiOperation("Delete a station by ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "No Content"),
		@ApiResponse(code = 404, message = "Station not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStation(@PathVariable Long id) throws StationNotExistsException {
		// Check if the station exists

		if (stationService.getStation(id) == null) {
			throw new StationNotExistsException(id );
		}

		// Delete the station
		stationService.removeStation(id);

		return ResponseEntity.status(200).body("Station "+id+" was deleted!");
	}


}
