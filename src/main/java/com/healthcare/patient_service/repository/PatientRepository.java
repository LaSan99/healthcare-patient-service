package com.healthcare.patient_service.repository;

import com.healthcare.patient_service.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {

    Patient findByEmail(String email);
}
