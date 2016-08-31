/*
 * Author:Aristotelis Papaioannou
 * Date Created: 12/10/15
 * Assignment: Capstone
 * Description: Make a Character Creation screen using the Javafx GUI
 */
package capstone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Capstone extends Application
{
    
    private int totalPoints = 100;
    private int remPoints = 100;
    Label pointsSpent = new Label("Points Spent: " + (100-totalPoints));//Beggining labels for points spent/rem
    Label pointsRem = new Label("Points Remaining: " + totalPoints);
    private final String[] sliderStats = {"Strength:", "Intelligence:", "Dexterity:", "Wisdom:"};//slider labels
    final TextField nameTxt = new TextField("");    //setting name to blank
    final FileChooser loadFile = new FileChooser(); //load file
    final Stage stage = new Stage();
    Label previewLbl = new Label("Preview:");   //all other label names
    Label nameLbl = new Label("Name:");         //
    Label raceLbl = new Label("Race:");         //
    Label classLbl = new Label("Class");        //
    Label genderLbl = new Label("Gender:");     //
    ToggleGroup genGroup = new ToggleGroup();   //toggle group for gender buttons

    Slider strength = new Slider(0,100,0);      //slider setup
    Slider intelligence = new Slider(0,100,0);
    Slider dexterity = new Slider(0,100,0);
    Slider wisdom = new Slider(0,100,0);
    Slider[] statArray = new Slider[]{strength, intelligence, dexterity, wisdom};
    
    Image vaultDweller = new Image("vaultdweller.png"); //images for classes
    Image ncr = new Image("ncr.png");                   //
    Image bos = new Image("bos.png");                   //
    Image raider = new Image("raider.png");             //
    Image enclave = new Image("enclave.png");           //
    ImageView photo = new ImageView();                  //
    
    
    String name = "";   //Initializing traits
    String race = "";   //    
    String cla = "";
    String gender = ""; //
    double str;         //
    double intel;       //
    double dex;         //
    double wis;         //
    
    Pane pane = new Pane();
    
    @Override
    public void start(Stage primaryStage) 
    {
        
        final ComboBox raceComboBox = new ComboBox();   //dropdown box for races
        raceComboBox.getItems().addAll(
            "Alien",
            "Humanoid Robot",
            "Human",
            "Ghoul",
            "Super Mutant", 
            "Synth"
        );   
        raceComboBox.setValue("Human");                 //set default to Human  
        
        final ComboBox classComboBox = new ComboBox();  //Dropdown box for classes
        classComboBox.getItems().addAll(
            "Raider",
            "NCR",
            "Vault Dweller",
            "Brotherhood of Steel",
            "Enclave"
        );
        classComboBox.setValue("Vault Dweller");    //set default class to Vaultdweller
        photo.setImage(vaultDweller);               //setting the default image to vault dweller
        photo.setLayoutX(5);
        photo.setLayoutY(17);
        photo.setPreserveRatio(true);
        pane.getChildren().add(photo);
        
        
       classComboBox.valueProperty().addListener(new ChangeListener<String>() 
       {
        @Override 
        public void changed(ObservableValue orig, String prevClass, String newClass)//changing the image based on class choice
        {
            if("Raider".equals(newClass))
            {
                photo.setImage(raider);
            }
            else if("NCR".equals(newClass))
            {
                photo.setImage(ncr);
            }
            else if("Vault Dweller".equals(newClass))
            {
                photo.setImage(vaultDweller);
            }
            else if("Brotherhood of Steel".equals(newClass))
            {
                photo.setImage(bos);
            }
            else if("Enclave".equals(newClass))
            {
                photo.setImage(enclave);
            }
        }    
        });
       
        RadioButton maleBut = new RadioButton("Male");      //gender button for male
        RadioButton femaleBut = new RadioButton("Female");  //button for femal
        maleBut.setSelected(true);                          //default gender Male
        maleBut.setToggleGroup(genGroup);                   //add the buttons to a group
        femaleBut.setToggleGroup(genGroup);                 //
        Button saveBut = new Button("Save");                //Save Button
        Button loadBut = new Button("Load");                //Load button
                
        
        GridPane grid = new GridPane(); 
        GridPane.setColumnSpan(loadBut, 3);
        grid.setHgap(10);   //setting the horizontal gap between buttons
        grid.setVgap(10);   //setting the vertical gap between buttons  
        grid.add(previewLbl, 1, 0);
        GridPane.setColumnSpan(previewLbl, 4);
        GridPane.setRowSpan(previewLbl, 2);
        grid.add(nameLbl, 21, 1);
        grid.add(nameTxt, 22, 1);
        grid.add(raceLbl, 21, 2);
        grid.add(raceComboBox, 22, 2);
        grid.add(classLbl, 21, 3);
        grid.add(classComboBox, 22, 3);
        grid.add(saveBut, 15, 19);
        grid.add(loadBut, 18, 19);  
        pane.getChildren().add(grid);   //adding everything to the grid
        
        genderLbl.setLayoutX(5);    //Layouts
        genderLbl.setLayoutY(220);  //
        maleBut.setLayoutX(55);
        maleBut.setLayoutY(220);    //
        femaleBut.setLayoutX(110);  //
        femaleBut.setLayoutY(220);  //
        
        
        sliderSetup();  //set up the sliders for character points
              
        pane.getChildren().add(genderLbl);
        pane.getChildren().add(maleBut);
        pane.getChildren().add(femaleBut);
        primaryStage.setScene(new Scene(pane, 500, 300));   //window size
        primaryStage.setResizable(false);   //window size does not change
        primaryStage.show();    //Show everything
     
        saveBut.setOnAction(new EventHandler()  //what to do when save button is pressed
        {
            @Override
                public void handle(Event event) 
                {
                    int nameSize = nameTxt.getLength(); //get the length of the users name
                    if(nameSize < 2)    //if the length of the name is less than 2 characters
                    {
                     nameErrorPopUp();  //error if name is less than 2 characters
                    }
                    else
                    {
                        
                        name = nameTxt.getText();
                        race = raceComboBox.getValue().toString();
                        cla = classComboBox.getValue().toString();
                        if(genGroup.getSelectedToggle().equals(maleBut))
                            gender = "M";
                        else
                            gender = "F";
                        str = Math.round(statArray[0].getValue());
                        intel = Math.round(statArray[1].getValue());
                        dex = Math.round(statArray[2].getValue());
                        wis = Math.round(statArray[3].getValue());
                        
                        String[] finStats = new String[]{name, cla, gender, race, Double.toString(str), Double.toString(intel), 
                                Double.toString(dex), Double.toString(wis)}; //array for things that are going to be printed
                        
                        FileChooser saveFile = new FileChooser();   //filechooser for saving the file
                        saveFile.setInitialFileName(name);          //setting default file name
                        FileChooser.ExtensionFilter saveFilter = new FileChooser.ExtensionFilter("umdchar (*.umdchar)", "*.umdchar"); //save file default entension
                        saveFile.getExtensionFilters().add(saveFilter);      
                        File file = saveFile.showSaveDialog(stage); //open the save dialog
                        
                        if(file != null)    //if nothing become null save the file
                        {
                            try
                            {
                                FileWriter fstream = new FileWriter(file);   // Create file 
                                try (BufferedWriter out = new BufferedWriter(fstream)) {
                                    for(int i = 0; i < finStats.length; i++)
                                    {
                                        out.write(finStats[i]); //write out the array to the file
                                        out.newLine();          //new line after each stat written out
                                    }
                                    out.close();    //Close the output stream
                                }
                            }catch (Exception e)    //exception error
                            {   
                                System.err.println("Error: " + e.getMessage());//Catch exception if any
                            }
                        }
                    }
                }                 
       });
        
        loadBut.setOnAction(new EventHandler()  //when load button is pressed
        {
           @Override
            public void handle(Event event)
            {
                loadFile.setTitle("Open Character File");   
                loadFile.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("umdchar (*.umdchar)","*.umdchar"));    //only allow .umdchar files to be opened    
                
                File file = loadFile.showOpenDialog(stage); //open load dialog
                    if (file != null) 
                    {
                        try 
                        {
                            openFile(file); //open the file
                        } catch (IOException ex) 
                        {
                            Logger.getLogger(Capstone.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
                 private void openFile(File file) throws FileNotFoundException, IOException //read the file
                {
                    BufferedReader buff = new BufferedReader(new FileReader(file));
                    String fileLine;
                    if(( fileLine = buff.readLine()) != null) 
                    {
                        nameTxt.setText(fileLine);  //read the first line
                        classComboBox.setValue(buff.readLine());
                        
                        if(buff.readLine().equals("M")) //setting the gender from the file
                            genGroup.selectToggle(maleBut);
                        else 
                            genGroup.selectToggle(femaleBut);
                        
                        raceComboBox.setValue(buff.readLine());
                        statArray[0].setValue(Double.parseDouble(buff.readLine()));
                        statArray[1].setValue(Double.parseDouble(buff.readLine()));
                        statArray[2].setValue(Double.parseDouble(buff.readLine()));
                        statArray[3].setValue(Double.parseDouble(buff.readLine()));
                    }
                 }
        });
    }
   
    private  void sliderSetup()     //setting up the sliders
    {
        int y = 147;    //Y locationa for the first slider Label
        int y2 = 150;   //Y Location for the first slider
        
        
        //For loop to set the labels for each slider
        for(int i = 0; i < 4; i++)
        {
            Label stat = new Label(sliderStats[i]); //Create the Label for the Slider
            stat.setLayoutX(269);                   //Layout for X of each Label
            stat.setLayoutY(y);                     // Y Location for each Label
            pane.getChildren().add(stat);           //Add the Label to the Pane
            
            y = y + 25;                             //Changes Y Location
        }
        
        //For loop to make each slider
        for(int i = 0; i < 4; i++)
        {
            statArray[i].setBlockIncrement(1.000);
            statArray[i].setLayoutX(335);
            statArray[i].setLayoutY(y2);
            statArray[i].setValue(0.00);
            
            Label statVal = new Label(Integer.toString((int) statArray[i].getValue()));//how many points are in the stat
            statVal.setLayoutX(480);
            statVal.setLayoutY(y2 - 3);
            pane.getChildren().add(statVal);
            
            statArray[i].valueProperty().addListener(new ChangeListener()
            {
                @Override 
                public void changed(ObservableValue arg0,Object arg1, Object arg2)
                {
                  
                    Double newVal = Double.parseDouble(arg2.toString());    //make the newVal arg2
                    
                    totalPoints =(int)(statArray[0].getValue() + statArray[1].getValue()
                        + statArray[2].getValue() + statArray[3].getValue());   //calculate how many points have been used
                    remPoints = 100 - totalPoints;                                  //how many points remain
                        
                    if(totalPoints <= 100 && remPoints >=0)
                    {
                        Math.floor(newVal);
                        
                       statVal.setText(newVal.toString()); //set the label to newVal
                        
                        totalPoints =(int)(statArray[0].getValue() + statArray[1].getValue()
                            + statArray[2].getValue() + statArray[3].getValue());   //calculate how many points have been used
                        
                        remPoints = 100 - totalPoints;                                  //how many points remain
                        pointsRem.setText("Points Remaining: " + (remPoints));  //update the label
                        pointsSpent.setText("Points Spent: " + (totalPoints));          //update the label
                        
                    }
                    
                }
            });
            
            pane.getChildren().add(statArray[i]);//add the slider to the pane

            y2 = y2 + 25;   //change the Y layout
        }
        
       
        pointsSpent.setLayoutX(285);
        pointsSpent.setLayoutY(110);
        pointsRem.setLayoutX(285);
        pointsRem.setLayoutY(125);   
        pane.getChildren().add(pointsSpent);
        pane.getChildren().add(pointsRem);
        
              
    }
    private void nameErrorPopUp() //pop up if the name is less than 2 characters
    {
        final Stage nameError = new Stage();
        nameError.initModality(Modality.WINDOW_MODAL);

        Label errorLabel = new Label("   Error: Name must be atleast 2 characters ");//the error
        errorLabel.setAlignment(Pos.BASELINE_CENTER);

        Button okayBtn = new Button("Okay");//button to exit the error popup
        okayBtn.setOnAction(new EventHandler<ActionEvent>() //button handler
        {
            @Override
            public void handle(ActionEvent butt) 
            {
                nameError.close();  //close the error pop up
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().add(okayBtn);    //add okay button 
        hBox.getChildren().add(errorLabel); //add the message
 
        nameError.setScene(new Scene(hBox));
        nameError.show();                 
    }

     public static void main(String[] args) 
    {
        launch(args);   //launch the application
    }
}
