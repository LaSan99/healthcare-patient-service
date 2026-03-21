package com.healthcare.patient_service.service;

import com.healthcare.patient_service.model.Patient;
import com.healthcare.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient createPatient(Patient patient) {
        return repository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    public Patient getPatientById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Patient updatePatient(String id, Patient updatedPatient) {

        Patient patient = repository.findById(id).orElse(null);

        if(patient != null){
            patient.setName(updatedPatient.getName());
            patient.setPhone(updatedPatient.getPhone());
            patient.setAge(updatedPatient.getAge());
            patient.setMedicalHistory(updatedPatient.getMedicalHistory());
            return repository.save(patient);
        }

        return null;
    }

    public void deletePatient(String id){
        repository.deleteById(id);
    }

    public Patient findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
