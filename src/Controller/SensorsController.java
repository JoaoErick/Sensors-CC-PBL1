/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author user03
 */
public class SensorsController implements Initializable {
    
    @FXML
    private Button btnSumRespiratoryFrequency;

    @FXML
    private TextField txtRespiratoryFrequency;

    @FXML
    private Button btnDecRespiratoryFrequency;
    
    //------

    @FXML
    private Button btnSumTemperature;

    @FXML
    private TextField txtTemperature;

    @FXML
    private Button btnDecTemperature;
    
    //------

    @FXML
    private Button btnSumBloodOxygen;

    @FXML
    private TextField txtBloodOxygen;

    @FXML
    private Button btnDecBloodOxygen;
    
    //------

    @FXML
    private Button btnSumHeartRate;

    @FXML
    private TextField txtHeartRate;

    @FXML
    private Button btnDecHeartRate;
    
    //------

    @FXML
    private Button btnSumBloodPressure;

    @FXML
    private TextField txtBloodPressure;

    @FXML
    private Button btnDecBloodPressure;
    
    //------

    @FXML
    private Button btnSendData;
    
    private int valueRF = 11;
    private double valueT = 35.8;
    private double valueBO = 96.5;
    private int valueHR = 72;
    private int valueBP = 112;
    
    private static Socket client;
    
    private static String response;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initClient();
        
        btnSendData.setOnMouseClicked((MouseEvent e)->{
            
            try {
                if(sendMessage(txtRespiratoryFrequency.getText(), txtTemperature.getText(), txtBloodOxygen.getText(), txtHeartRate.getText(), txtBloodPressure.getText())){
                    System.out.println("Mensagem enviada com sucesso!");
                    if(!response.equals(" "))
                        System.out.println("Resposta do servidor: " + response);

                } else{
                    System.out.println("Erro, falha ao enviar a mensagem!");
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SensorsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        txtRespiratoryFrequency.setText(Integer.toString(valueRF));
        txtTemperature.setText(Double.toString(valueT));
        txtBloodOxygen.setText(Double.toString(valueBO));
        txtHeartRate.setText(Integer.toString(valueHR));
        txtBloodPressure.setText(Integer.toString(valueBP));
        
        //--------------
        
        txtRespiratoryFrequency.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtRespiratoryFrequency.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        txtTemperature.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTemperature.setText(newValue.replaceAll("[^\\d||.]", ""));
                }
            }
        });
        
        txtBloodOxygen.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtBloodOxygen.setText(newValue.replaceAll("[^\\d||.]", ""));
                }
            }
        });
        
        txtHeartRate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtHeartRate.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        txtBloodPressure.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtBloodPressure.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        //--------------
        
        txtRespiratoryFrequency.setOnKeyReleased((KeyEvent e)->{
            verifyTextLegth(txtRespiratoryFrequency, 2);
        });
        txtTemperature.setOnKeyReleased((KeyEvent e)->{
            verifyTextLegth(txtTemperature, 4);
        });
        txtBloodOxygen.setOnKeyReleased((KeyEvent e)->{
            verifyTextLegth(txtBloodOxygen, 5);
        });
        txtHeartRate.setOnKeyReleased((KeyEvent e)->{
            verifyTextLegth(txtHeartRate, 3);
        });
        txtBloodPressure.setOnKeyReleased((KeyEvent e)->{
            verifyTextLegth(txtBloodPressure, 3);
        });
        
        //--------------
        
        btnSumRespiratoryFrequency.setOnMouseClicked((MouseEvent e)->{
            sum(txtRespiratoryFrequency, valueRF, 2);
        });
        
        btnSumTemperature.setOnMouseClicked((MouseEvent e)->{
            sumDouble(txtTemperature, valueT, 4);
        });
        
        btnSumBloodOxygen.setOnMouseClicked((MouseEvent e)->{
            sumDouble(txtBloodOxygen, valueBO, 5);
        });
        
        btnSumHeartRate.setOnMouseClicked((MouseEvent e)->{
            sumDouble(txtHeartRate, valueHR, 3);
        });
        
        btnSumBloodPressure.setOnMouseClicked((MouseEvent e)->{
            sumDouble(txtBloodPressure, valueBP, 3);
        });
        
        //--------------
        
        btnDecRespiratoryFrequency.setOnMouseClicked((MouseEvent e)->{
            dec(txtRespiratoryFrequency, valueRF, 2);
        });
        
        btnDecTemperature.setOnMouseClicked((MouseEvent e)->{
            decDouble(txtTemperature, valueT, 4);
        });
        
        btnDecBloodOxygen.setOnMouseClicked((MouseEvent e)->{
            decDouble(txtBloodOxygen, valueBO, 5);
        });
        
        btnDecHeartRate.setOnMouseClicked((MouseEvent e)->{
            decDouble(txtHeartRate, valueHR, 3);
        });
        
        btnDecBloodPressure.setOnMouseClicked((MouseEvent e)->{
            decDouble(txtBloodPressure, valueBP, 3);
        });
    }   
    
    //Inicializa a conexão cliente com o servidor 60000, o IP 127.0.0.2 indica que o servidor está na mesma máquina que o cliente.
    private static void initClient(){
        try {
            client = new Socket("127.0.0.2", 60000);
            System.out.println("Conexão estabelecida!");
        } catch (IOException ex) {
            System.out.println("Erro, a conexão com o servidor não foi estabelecida!");
        }
    }
    
    //Envia os dados ao servidor a partir do que for digitado.
    private static boolean sendMessage(String respiratoryFrequency, String temperature, String bloodOxygen, String heartRate, String bloodPressure) throws ClassNotFoundException{
        try {
            PrintStream data = new PrintStream(client.getOutputStream());
            data.println("POST");
            data.println("Fulano");
            data.println(respiratoryFrequency);
            data.println(temperature);
            data.println(bloodOxygen);
            data.println(heartRate);
            data.println(bloodPressure);
            
            ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
            String text = (String)entrada.readObject();
            System.out.println("Resposta do servidor: " + text);
            
            response = text;
            
            return true;
        } catch (IOException ex) {
            System.out.println("Erro ao enviar a mensagem!");
        }
        return false;
    }
    
    private void verifyTextLegth(TextField txt, int limit){
        if (txt.getText().length() > limit) {
            txt.setText(txt.getText().substring(0, limit));
        }
    
    }
    
    private void sum(TextField txt, int value, int limit){
        if(txt.getText().isEmpty()){
            value += 1;
            txt.setText(Integer.toString(value));
            verifyTextLegth(txt, limit);
        } else{
            value = Integer.parseInt(txt.getText());
            value += 1;
            txt.setText(Integer.toString(value));
            verifyTextLegth(txt, limit);
        }
    }
    
    private void dec(TextField txt, int value, int limit){
        if(txt.getText().isEmpty()){
            value -= 1;
            txt.setText(Integer.toString(value));
            verifyTextLegth(txt, limit);
        } else{
            value = Integer.parseInt(txt.getText());
            value -= 1;
            txt.setText(Integer.toString(value));
            verifyTextLegth(txt, limit);
        }
    }
    
    private void sumDouble(TextField txt, double value, int limit){
        if(txt.getText().isEmpty()){
            value += 0.10;
            txt.setText(Double.toString(value));
            verifyTextLegth(txt, limit);
        } else{
            value = Double.parseDouble(txt.getText());
            value += 0.10;
            txt.setText(Double.toString(value));
            if(!txt.getText().contains(".3") || !txt.getText().contains(".8")){
                verifyTextLegth(txt, limit);}
        }
    }
    
    private void decDouble(TextField txt, double value, int limit){
        if(txt.getText().isEmpty()){
            value -= 0.10;
            txt.setText(Double.toString(value));
            verifyTextLegth(txt, limit);
        } else{
            value = Double.parseDouble(txt.getText());
            value -= 0.10;
            txt.setText(Double.toString(value));
                verifyTextLegth(txt, limit);
        }
    }
    
}
