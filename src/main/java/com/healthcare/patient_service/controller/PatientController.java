package com.healthcare.patient_service.controller;

import com.healthcare.patient_service.model.Patient;
import com.healthcare.patient_service.model.Role;
import com.healthcare.patient_service.service.PatientService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;

    public PatientController(PatientService patientService, PasswordEncoder passwordEncoder) {
        this.patientService = patientService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient){
        return patientService.createPatient(patient);
    }

    @GetMapping
    public List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable String id){
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable String id,
                                 @RequestBody Patient patient){
        return patientService.updatePatient(id, patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable String id){
        patientService.deletePatient(id);
    }

    @PostMapping("/register")
    public Patient registerPatient(@RequestBody Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRole(Role.PATIENT);
        return patientService.createPatient(patient);
    }

    @PostMapping("/login")
    public String login() {
        return "Login successful";
    }

    @GetMapping("/profile")
    public Patient getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return patientService.findByEmail(userDetails.getUsername());
    }

    @PutMapping("/profile")
    public Patient updateMyProfile(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody Patient patient) {
        Patient currentPatient = patientService.findByEmail(userDetails.getUsername());
        return patientService.updatePatient(currentPatient.getId(), patient);
    }

    @DeleteMapping("/profile")
    public void deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        Patient currentPatient = patientService.findByEmail(userDetails.getUsername());
        patientService.deletePatient(currentPatient.getId());
    }

    @PostMapping("/admin/create")
    public Patient createAdmin(@RequestBody Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRole(Role.ADMIN);
        return patientService.createPatient(patient);
    }

    @GetMapping("/admin/all")
    public List<Patient> getAllPatientsForAdmin() {
        return patientService.getAllPatients();
    }

    @GetMapping("/admin/{id}")
    public Patient getPatientByIdForAdmin(@PathVariable String id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/admin/{id}")
    public Patient updatePatientForAdmin(@PathVariable String id,
                                       @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @DeleteMapping("/admin/{id}")
    public void deletePatientForAdmin(@PathVariable String id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/admin/{id}/role")
    public Patient updateUserRole(@PathVariable String id,
                                  @RequestBody Role roleUpdate) {
        Patient patient = patientService.getPatientById(id);
        patient.setRole(roleUpdate);
        return patientService.updatePatient(id, patient);
    }

    @GetMapping("/admin/stats")
    public String getAdminStats() {
        List<Patient> allPatients = patientService.getAllPatients();
        long patientCount = allPatients.stream().filter(p -> p.getRole() == Role.PATIENT).count();
        long adminCount = allPatients.stream().filter(p -> p.getRole() == Role.ADMIN).count();
        return String.format("Admin Statistics - Total Users: %d, Patients: %d, Admins: %d", 
                           allPatients.size(), patientCount, adminCount);
    }
}
