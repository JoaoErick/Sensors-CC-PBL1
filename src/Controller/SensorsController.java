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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Classe controladora responsável por executar funções dispararadas por ações 
 * realizadas na janela da aplicação do emulador de sensores.
 * 
 * @author João Erick Barbosa
 */
public class SensorsController implements Initializable {
    /**
     * Componentes do nome do paciente.
     */
    @FXML
    private TextField txtUserName;
    
    @FXML
    private Label lblUserName;
    
    /**
     * Componentes do sensor de frequência respiratória.
     */
    @FXML
    private Button btnSumRespiratoryFrequency;

    @FXML
    private TextField txtRespiratoryFrequency;

    @FXML
    private Button btnDecRespiratoryFrequency;
    
    /**
     * Componentes do sensor de temperatura.
     */
    @FXML
    private Button btnSumTemperature;

    @FXML
    private TextField txtTemperature;

    @FXML
    private Button btnDecTemperature;
    
    /**
     * Componentes do sensor de oxigenação do sangue.
     */
    @FXML
    private Button btnSumBloodOxygen;

    @FXML
    private TextField txtBloodOxygen;

    @FXML
    private Button btnDecBloodOxygen;
    
    /**
     * Componentes do sensor de frequência cardíaca.
     */
    @FXML
    private Button btnSumHeartRate;

    @FXML
    private TextField txtHeartRate;

    @FXML
    private Button btnDecHeartRate;
    
    /**
     * Componentes do sensor de pressão arterial.
     */
    @FXML
    private Button btnSumBloodPressure;

    @FXML
    private TextField txtBloodPressure;

    @FXML
    private Button btnDecBloodPressure;
    
    /**
     * Botão que envia os dados dos sensores para o servidor.
     */
    @FXML
    private Button btnSendData;
    
    /**
     * Valores de cada sensor inicializados com resultados arbitrários.
     */
    private int valueRF = 11;
    private double valueT = 35.8;
    private double valueBO = 96.5;
    private int valueHR = 72;
    private int valueBP = 112;
    
    /**
     * Cliente socket que fará a conexão com o servidor ServerSocket.
     */
    private static Socket client;
    
    /**
     * Permite que o método POST seja utilizado uma vez a cada instância dessa aplicação. 
     * Após esse o uso, os próximos envios de dados serão feitos pelo método PUT.
     */
    private static int flag = 0;
    
    /**
     * Gera o ID do paciente em um conjunto de 4 caracteres aleatórios.
     */
    private static String patientID = UUID.randomUUID().toString().substring(9, 13);
    
    /**
     * Recebe a mensagem do servidor.
     */
    private static String response;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Faz a conexão do cliente com o servidor.
        initClient();
        
        //Ao pressionar o botão, a função que envia os dados ao servidor é acionada.
        btnSendData.setOnMouseClicked((MouseEvent e)->{
            
            try {
                if(!txtUserName.getText().equals("")){
                    if(sendMessage(txtUserName.getText(), txtRespiratoryFrequency.getText(), txtTemperature.getText(), txtBloodOxygen.getText(), txtHeartRate.getText(), txtBloodPressure.getText())){
                        System.out.println("Mensagem enviada com sucesso!");
                        txtUserName.setVisible(false);
                        lblUserName.setText(txtUserName.getText());
                    } else{
                        System.out.println("Erro, falha ao enviar a mensagem!");
                    }
                } else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Atenção");
                    alert.setHeaderText("É necessário preencher o nome do paciente para prosseguir.");
                    alert.show();
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SensorsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        //Inicializando os campos de texto dos sensores com valores arbitrários.
        txtRespiratoryFrequency.setText(Integer.toString(valueRF));
        txtTemperature.setText(Double.toString(valueT));
        txtBloodOxygen.setText(Double.toString(valueBO));
        txtHeartRate.setText(Integer.toString(valueHR));
        txtBloodPressure.setText(Integer.toString(valueBP));
        
        //Limitando os tipos de caracteres que podem ser digitados nos campos de texto.
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
        
        //A cada alteração no campo de texto do respectivo sensor, é
        //verificado se o valor coloca não ultrapassa o limite possível.
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
        
        
        //A cada clique no botão '+', o somador do respectivo sensor é acionado.
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
        
        //A cada clique no botão '-', o decrementador do respectivo sensor é acionado.
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
    
    /**
     * Método que inicializa um cliente socket fazendo a conexão com o servidor.
     */
    private static void initClient(){
        try {
            client = new Socket("127.0.0.2", 60000);
            System.out.println("Conexão estabelecida!");
        } catch (IOException ex) {
            System.out.println("Erro, a conexão com o servidor não foi estabelecida!");
        }
    }
    
    /**
     * Método que envia os dados do paciente para o servidor com o objetivo de armazená-los ou alterar algum valor.
     * 
     * @param respiratoryFrequency
     * @param temperature
     * @param bloodOxygen
     * @param heartRate
     * @param bloodPressure
     * @return boolean
     * @throws ClassNotFoundException 
     */
    private static boolean sendMessage(String userName, String respiratoryFrequency, String temperature, String bloodOxygen, String heartRate, String bloodPressure) throws ClassNotFoundException{
        try {
            PrintStream data = new PrintStream(client.getOutputStream());
            if(flag == 0){
                data.println("POST /create");
                flag++;
            } else{
                data.println("PUT /update");
            }
            data.println(patientID);
            data.println(userName);
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
    
    /**
     * Método que corrige o valor do sensor caso este ultrapasse do limite.
     * 
     * @param txt TextField - Campo que texto que conterá o valor do sensor.
     * @param limit int - Valor limite que o sensor pode atingir. 
     */
    private void verifyTextLegth(TextField txt, int limit){
        if (txt.getText().length() > limit) {
            txt.setText(txt.getText().substring(0, limit));
        }
    
    }
    
    /**
     * Método que soma em 1 no valor do sensor cada vez que é invocado.
     * 
     * @param txt TextField - Campo que texto que conterá o valor do sensor.
     * @param value int - Valor atual o sensor.
     * @param limit int - Valor limite que o sensor pode atingir.
     */
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
    
    /**
     * Método que subtrai em 1 no valor do sensor cada vez que é invocado.
     * 
     * @param txt TextField - Campo que texto que conterá o valor do sensor.
     * @param value int - Valor atual o sensor.
     * @param limit int - Valor limite que o sensor pode atingir.
     */
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
    
    /**
     * Método que soma em 0.1 no valor do sensor cada vez que é invocado.
     * 
     * @param txt TextField - Campo que texto que conterá o valor do sensor.
     * @param value double - Valor atual o sensor.
     * @param limit int - Valor limite que o sensor pode atingir.
     */
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
    
    /**
     * Método que subtrai em 0.1 no valor do sensor cada vez que é invocado.
     * 
     * @param txt TextField - Campo que texto que conterá o valor do sensor.
     * @param value double - Valor atual o sensor.
     * @param limit int - Valor limite que o sensor pode atingir.
     */
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
