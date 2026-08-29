package com.vetcare.model;

public class MedicalRecordMedicine {

    private Long id;
    private Long medicalRecordId;
    private Long medicineId;
    private Integer cantidadUtilizada;

    public MedicalRecordMedicine (){

    }

    public MedicalRecordMedicine(Long medicalRecordId, Long medicineId, Integer cantidadUtilizada ){
        this.medicalRecordId = medicalRecordId;
        this.medicineId =  medicineId;
        this.cantidadUtilizada = cantidadUtilizada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(Long medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(Integer cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }

    @Override
    public String toString() {
        return "MedicalRecordMedicine{" +
                "id=" + id +
                ", medicalRecordId=" + medicalRecordId +
                ", medicineId=" + medicineId +
                ", cantidadUtilizada=" + cantidadUtilizada +
                '}';
    }

}
