package dev.khtml.hackathon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.khtml.hackathon.dto.MedicineStoreDTO;
import dev.khtml.hackathon.entity.MedicineStore;
import dev.khtml.hackathon.repository.MedicineStoreRepository;
import dev.khtml.hackathon.support.util.LatLonCalculator;
import dev.khtml.hackathon.support.util.MedicineStoreMapper;

@Service
public class MedicineStoreService {

	private final LatLonCalculator latLonCalculator;
	private final MedicineStoreRepository medicineStoreRepository;
	private MedicineStoreMapper medicineStoreMapper;

	public MedicineStoreService(
		LatLonCalculator latLonCalculator,
		MedicineStoreRepository medicineStoreRepository
	) {
		this.latLonCalculator = latLonCalculator;
		this.medicineStoreRepository = medicineStoreRepository;
		this.medicineStoreMapper = new MedicineStoreMapper(latLonCalculator);
	}

	public List<MedicineStoreDTO.MedicineStoreResponse> getNearMedicineStores(Double latitude, Double longitude,
		Integer radiusMeter) {

		double maxLatitude = latitude + LatLonCalculator.calculateLatDifference(radiusMeter);
		double minLatitude = latitude - LatLonCalculator.calculateLatDifference(radiusMeter);

		List<MedicineStore> medicineStoreList = medicineStoreRepository.findByLatitudeBetween(minLatitude, maxLatitude);

		List<MedicineStoreDTO> nearbyLocations = LatLonCalculator.filterLocationsWithinRadius(
			medicineStoreMapper.toInfoList(medicineStoreList), latitude, longitude, radiusMeter);

		return medicineStoreMapper.toNearMedicineStoreResponseList(latitude, longitude, nearbyLocations);
	}

}
