package sample;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

public class Controller implements Initializable {
    @FXML
    public TextField maxTextfield;
    @FXML
    public TextField minTextfield;
    @FXML
    JFXButton create;
    @FXML
    JFXListView numbersListView;
    @FXML
    public Button run1;
    @FXML
    JFXButton delete;
    @FXML
    JFXButton load;
    @FXML
    public Label result;

    int RandomNumber=0;

    final String DB_URL = "jdbc:derby:NumbersDB;create=true"; //protocol:subprotocol:databaseName
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Numbers random=new Numbers();



        run1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                int FieldMin = Integer.parseInt(minTextfield.getText());
                int FieldMax = Integer.parseInt(maxTextfield.getText());
                RandomNumber = (int) (Math.random() * (FieldMin - FieldMax) + FieldMax);
                result.setText(RandomNumber + " ");
                //Numbers pass = new Numbers();
                //pass.NumberClass = 4;
            }
        });


        final String DB_URL = "jdbc:derby:EmployeeDB;create=true";

       delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try{
                    Connection conn = DriverManager.getConnection(DB_URL);
                    Statement stmt = conn.createStatement();
                    stmt.execute("DROP TABLE Numbers");
                    stmt.close();
                    conn.close();
                    System.out.println("TABLE DROPPED");
                }
                catch (Exception ex)
                {
                    var msg = ex.getMessage();
                    System.out.println("TABLE NOT DROPPED");
                    System.out.println(msg);
                }
            }
        });


        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{

                    try{

                        Connection conn = DriverManager.getConnection(DB_URL);
                        Statement stmt = conn.createStatement();

                        try
                        {
                            stmt.execute("CREATE TABLE Numbers (" +
                                    "Random CHAR(32) )");

                            System.out.println("TABLE CREATED");
                        }
                        catch (Exception ex)
                        {
                            System.out.println("TABLE ALREADY EXISTS, NOT CREATED");
                        }

                        String sql = "INSERT INTO Numbers VALUES" +
                                "('" + RandomNumber+"')";
                        stmt.executeUpdate(sql);

                        System.out.println("TABLE FILLED");

                    }
                    catch (Exception ex)
                    {
                        var msg = ex.getMessage();
                        System.out.println(msg);
                    }

                    Connection conn = DriverManager.getConnection(DB_URL);

                    Statement stmt = conn.createStatement();

                    String sqlStatement = "SELECT Random FROM Numbers";
                    ResultSet result = stmt.executeQuery(sqlStatement);
                    ObservableList<Numbers> dbNumbersList = FXCollections.observableArrayList();
                    while (result.next())
                    {
                       Numbers num = new Numbers();
                      String str1 = Integer.toString(RandomNumber);
                      num.NumberClass=str1;
                        num.NumberClass = result.getString("Random");

                        dbNumbersList.add(num);
                    }

                    numbersListView.setItems(dbNumbersList);
                    System.out.println("DATA LOADED");

                    stmt.close();
                    conn.close();
                }
                catch (Exception ex)
                {
                    var msg = ex.getMessage();
                    System.out.println("DATA NOT LOADED");
                    System.out.println(msg);
                }
            }
        });


    }
}

