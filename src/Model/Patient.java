
package Model;

import java.io.Serializable;
/**
 * 
 * @author João Erick Barbosa
 */
public class Patient implements Serializable, Comparable<Patient> {
    private String id;
    private String userName;
    private String respiratoryFrequency;
    private String temperature;
    private String bloodOxygen;
    private String heartRate;
    private String bloodPressure;
    
    private String situation = "Não grave";
    private boolean seriousness = false;
    private double scoreSeriousness = 0.0;
    
    /**
     * Método construtor de paciente.
     * 
     * @param id
     * @param userName
     * @param respiratoryFrequency
     * @param temperature
     * @param bloodOxygen
     * @param heartRate
     * @param bloodPressure 
     */
    public Patient(String id, String userName, String respiratoryFrequency, String temperature, String bloodOxygen, String heartRate, String bloodPressure) {
        this.id = id;
        this.userName = userName;
        this.respiratoryFrequency = respiratoryFrequency;
        this.temperature = temperature;
        this.bloodOxygen = bloodOxygen;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        
        verifySeriousness();
    }
    
    /**
     * Método que verifica se o paciente criado está em estado grave de COVID-19.
     */
    public void verifySeriousness(){
        if(Integer.parseInt(this.respiratoryFrequency) >= 21 
                || Integer.parseInt(this.heartRate) >= 111 
                || Integer.parseInt(this.bloodPressure) <= 100 
                || Double.parseDouble(this.temperature) >= 38.6 
                || Double.parseDouble(this.bloodOxygen) <= 96.0){
            
            this.seriousness = true;
            this.situation = "Grave";
            calculateScoreSeriousness();
        } else{
            this.seriousness = false;
            this.situation = "Não grave";
            this.scoreSeriousness = 0.0;
        }
    }
    
    public void calculateScoreSeriousness(){
        double score = 0.0;
        if(Integer.parseInt(this.respiratoryFrequency) >= 21 ){
            score += (double) (Integer.parseInt(this.respiratoryFrequency)) * 1;
        }
        if(Integer.parseInt(this.heartRate) >= 111 ){
            score += (double) (Integer.parseInt(this.heartRate)) * 1;
        }
        if(Integer.parseInt(this.bloodPressure) <= 100 ){
            score += (double) (100 - Integer.parseInt(this.bloodPressure)) * 1;
        }
        if(Double.parseDouble(this.temperature) >= 38.6 ){
            score += (double) (Double.parseDouble(this.temperature)) * 1;
        }
        if(Double.parseDouble(this.bloodOxygen) <= 96.0){
            score += (double) (100 - Double.parseDouble(this.bloodOxygen)) * 2;
        }
        this.scoreSeriousness = (double) score/6;
    }

    public Patient() {
    }

    /**
     * Método que retorna o ID do paciente.
     * 
     * @return String
     */
    public String getId() {
        return id;
    }
    
    /**
     * Método que retorna o nome do paciente.
     * 
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Método que altera o nome do paciente.
     * 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Método que retorna o valor da frequência respiratória do paciente.
     * 
     * @return String
     */
    public String getRespiratoryFrequency() {
        return respiratoryFrequency;
    }

    /**
     * Método que altera o valor da frequência respiratória do paciente.
     * 
     */
    public void setRespiratoryFrequency(String respiratoryFrequency) {
        this.respiratoryFrequency = respiratoryFrequency;
    }

    /**
     * Método que retorna o valor da temperatura do paciente.
     * 
     * @return String
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Método que altera o valor da temperatura do paciente.
     * 
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    /**
     * Método que retorna o valor da oxigenação do sangue do paciente.
     * 
     * @return String
     */
    public String getBloodOxygen() {
        return bloodOxygen;
    }

    /**
     * Método que altera o valor da oxigenação do sangue do paciente.
     * 
     */
    public void setBloodOxygen(String bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    /**
     * Método que retorna o valor dos batimentos cardíacos por minuto do paciente.
     * 
     * @return String
     */
    public String getHeartRate() {
        return heartRate;
    }

    /**
     * Método que altera o valor dos batimentos cardíacos por minuto do paciente.
     * 
     */
    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    /**
     * Método que retorna o valor da pressão arterial do paciente.
     * 
     * @return String
     */
    public String getBloodPressure() {
        return bloodPressure;
    }

    /**
     * Método que altera o valor da pressão arterial do paciente.
     * 
     */
    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
    
    /**
     * Método que retorna se o paciente está grave ou não.
     * 
     * @return boolean
     */
    public boolean isSeriousness() {
        return seriousness;
    }
    
    /**
     * Método que retorna se o paciente está grave ou não para exibição na lista.
     * 
     * @return boolean
     */
    public String getSituation() {
        return situation;
    }

    /**
     * Método que retorna a pontuação que expressa o quão grave ele se encontra.
     * 
     * @return boolean
     */
    public double getScoreSeriousness() {
        return scoreSeriousness;
    }
    
    @Override
    public int compareTo(Patient patient) {
        if (this.scoreSeriousness > patient.getScoreSeriousness()) { 
            return -1;
        }
        if (this.scoreSeriousness < patient.getScoreSeriousness()) {
            return 1;
        }
        return 0;
    }
    
}
