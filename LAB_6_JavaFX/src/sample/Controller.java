package sample;


import com.sun.javafx.scene.control.skin.TextFieldSkin;
import files.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;


import java.io.File;
import java.util.ArrayList;


public class Controller {
    private static Stage plotStage;
    private static DataFrame mainDataFrame;
    private static DataFrame groupedBySthDF;
    //GroupDataFrame mainGroupedDataFrame;

    private void printCommand(String commandToPrint) {
        Scene scene = Main.getPrimaryStage().getScene();
        Label command = (Label) scene.lookup("#commandLine");
        command.setUnderline(true);
        command.setText(commandToPrint);
    }

    public void LoadFromFile(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.setInitialDirectory(new File("C:\\Users\\resta\\Desktop"));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile;
            selectedFile = fileChooser.showOpenDialog(Main.getPrimaryStage());

            //tu trzeba raczej poprosić o typy kolumn albo coś, ale to wymagałoby stworzenia praktycznie drugiej aplikacji do projektowania kolumn XD
            // ... więc zostaje na "sztywno"
            mainDataFrame = new DataFrame(selectedFile.getAbsolutePath(), new Class[]{StringObject.class, DateObject.class, DoubleObject.class, DoubleObject.class});
            groupedBySthDF = mainDataFrame.clone();
            printCommand("File loaded!");
            transfromDataFrameToTableView(mainDataFrame);
        } catch ( NullPointerException e) {
            this.printCommand("Any file has NOT been loaded!");
        }
        catch(Exception e){
            this.printCommand(e.toString());
        }
    }

    private void transfromDataFrameToTableView(DataFrame DF) {
        Scene scene = Main.getPrimaryStage().getScene();
        TableView table = (TableView) scene.lookup("#table");
        table.getItems().clear();
        table.getColumns().clear();

        for (int i = 0; i < DF.size() && i <= 1000; i++) {
            table.getItems().add(i);
        }
        for (Column cln : DF.tab) {

            TableColumn<Integer, String> tableColumn = new TableColumn<>(cln.name);
            tableColumn.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(cln.data.get(rowIndex).toString());
            });
            table.getColumns().add(tableColumn);
        }
        if (DF.size() > 100) {
            Label command = (Label) scene.lookup("#commandLine");
            command.setUnderline(true);
            command.setText("The loaded file is too large, only the first 1000 results were displayed.");
        } else {
            printCommand("");
        }
        table.setVisible(true);
    }

    public void maxClicked(ActionEvent event) {
        try {
            Scene scene = Main.getPrimaryStage().getScene();
            if (mainDataFrame == null) {
                this.printCommand("Please load DataFrame!");
            } else {
                TextField k = (TextField) scene.lookup("#groupby");
                String[] keys = k.getText().split(", ");
                if (keys.length == 1 && keys[0].isEmpty()) {
                    keys = new String[]{};
                }
                try {
                    groupedBySthDF=mainDataFrame.groupBy(keys).max();
                    transfromDataFrameToTableView(groupedBySthDF);
                } catch (RuntimeException e) {
                    this.printCommand(e.toString());
                }
            }
        } catch (Exception e) {
            this.printCommand(e.toString());
        }
    }

    public void minClicked(ActionEvent event) {
        try {
            Scene scene = Main.getPrimaryStage().getScene();
            if (mainDataFrame == null) {
                this.printCommand("Please load DataFrame!");
            } else {
                TextField k = (TextField) scene.lookup("#groupby");
                String[] keys = k.getText().split(", ");
                if (keys.length == 1 && keys[0].isEmpty()) {
                    keys = new String[]{};
                }
                try {
                    groupedBySthDF=mainDataFrame.groupBy(keys).min();
                    transfromDataFrameToTableView(groupedBySthDF);
                } catch (RuntimeException e) {
                    this.printCommand(e.toString());
                }
            }
        } catch (Exception e) {
            this.printCommand(e.toString());
        }
    }

    public void meanClicked(ActionEvent event) {
        try {
            Scene scene = Main.getPrimaryStage().getScene();
            if (mainDataFrame == null) {
                this.printCommand("Please load DataFrame!");
            } else {
                TextField k = (TextField) scene.lookup("#groupby");
                String[] keys = k.getText().split(", ");
                if (keys.length == 1 && keys[0].isEmpty()) {
                    keys = new String[]{};
                }
                try {
                    groupedBySthDF=mainDataFrame.groupBy(keys).mean();
                    transfromDataFrameToTableView(groupedBySthDF);
                } catch (RuntimeException e) {
                    this.printCommand(e.toString());
                }
            }
        } catch (Exception e) {
            this.printCommand(e.toString());
        }
    }

    public void stdClicked(ActionEvent event) {
        try{
        Scene scene = Main.getPrimaryStage().getScene();
        if (mainDataFrame == null) {
            this.printCommand("Please load DataFrame!");
        } else {
            TextField k = (TextField) scene.lookup("#groupby");
            String[] keys = k.getText().split(", ");
            if (keys.length == 1 && keys[0].isEmpty()) {
                keys = new String[]{};
            }
            try {
                groupedBySthDF=mainDataFrame.groupBy(keys).std();
                transfromDataFrameToTableView(groupedBySthDF);
            } catch (RuntimeException e) {
                this.printCommand(e.toString());
            }
        }
        }
        catch(Exception e){
            this.printCommand(e.toString());
        }
    }

    public void sumClicked(ActionEvent event){
        Scene scene = Main.getPrimaryStage().getScene();
        if (mainDataFrame == null) {
            this.printCommand("Please load DataFrame!");
        } else {
            TextField k = (TextField) scene.lookup("#groupby");
            String[] keys = k.getText().split(", ");
            if (keys.length == 1 && keys[0].isEmpty()) {
                keys = new String[]{};
            }
            try {
                groupedBySthDF=mainDataFrame.groupBy(keys).sum();
                transfromDataFrameToTableView(groupedBySthDF);
            } catch (RuntimeException e) {
                this.printCommand(e.toString());
            }
        }
    }

    public void varClicked(ActionEvent event) {
        Scene scene = Main.getPrimaryStage().getScene();
        if (mainDataFrame == null) {
            this.printCommand("Please load DataFrame!");
        } else {
            TextField k = (TextField) scene.lookup("#groupby");
            String[] keys = k.getText().split(", ");
            if (keys.length == 1 && keys[0].isEmpty()) {
                keys = new String[]{};
            }
            try {
                groupedBySthDF=mainDataFrame.groupBy(keys).var();
                transfromDataFrameToTableView(groupedBySthDF);
            } catch (RuntimeException e) {
                this.printCommand(e.toString());
            }
        }
    }

    public void medianaClicked(ActionEvent event) {
        Scene scene = Main.getPrimaryStage().getScene();
        if (mainDataFrame == null) {
            this.printCommand("Please load DataFrame!");
        } else {
            TextField k = (TextField) scene.lookup("#groupby");
            String[] keys = k.getText().split(", ");
            if (keys.length == 1 && keys[0].isEmpty()) {
                keys = new String[]{};
            }
            try {
                groupedBySthDF=mainDataFrame.groupBy(keys).apply(new Mediana());
                transfromDataFrameToTableView(groupedBySthDF);
            } catch (RuntimeException e) {
                this.printCommand(e.toString());
            }
        }
    }

    public void plotClicked(ActionEvent event){
        try {
            if (mainDataFrame == null) {
                this.printCommand("Please load DataFrame!");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("plotwindow.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                this.plotStage = new Stage();
                plotStage.setScene(new Scene(root1));
                plotStage.setTitle("Plot from DataFrame");
                plotStage.show();

                ArrayList<String> possibleWords = new ArrayList<>();
                for (Column cln : mainDataFrame.tab) {
                    possibleWords.add(cln.name);
                }
                TextField axisX = (TextField) getPlotScene().lookup("#axisX");
                TextField axisY = (TextField) getPlotScene().lookup("#axisY");
                TextFields.bindAutoCompletion(axisX, possibleWords);
                TextFields.bindAutoCompletion(axisY, possibleWords);

                ChoiceBox plotFrom= (ChoiceBox) getPlotScene().lookup("#plotFrom");
                plotFrom.getItems().addAll(new String[]{"Original DataFrame","Grouped DataFrame"});
            }
            //generowanie wykresu po kliknięciu plotButton

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void generatePlotClicked(ActionEvent event){
        ChoiceBox plotFrom= (ChoiceBox)  getPlotScene().lookup("#plotFrom");
        Label plotCommand= (Label) getPlotScene().lookup("#plotCommand");
        TextField axisX = (TextField) getPlotScene().lookup("#axisX");
        TextField axisY = (TextField) getPlotScene().lookup("#axisY");
        plotCommand.setVisible(false);
        if(plotFrom.getValue()== null){
            plotCommand.setVisible(true);
            plotCommand.setText("Please select the DataFrame from which you want to make a chart" );
        }
        else if(((String) plotFrom.getValue()).equals("Original DataFrame")){
            ArrayList<String> possibleWords = new ArrayList<>();
            for (Column cln : mainDataFrame.tab) {
                possibleWords.add(cln.name);
            }
            if(!possibleWords.contains((String) axisX.getText()) || !possibleWords.contains((String) axisY.getText())){
                plotCommand.setVisible(true);
                plotCommand.setText("Choose the correct name of columns");
            }
        }
        else if(((String) plotFrom.getValue()).equals("Grouped DataFrame")){
            ArrayList<String> possibleWords = new ArrayList<>();
            for (Column cln : groupedBySthDF.tab) {
                possibleWords.add(cln.name);
            }
            if(!possibleWords.contains((String) axisX.getText()) || !possibleWords.contains((String) axisY.getText())){
                plotCommand.setVisible(true);
                plotCommand.setText("Choose the correct name of GROUPED columns");
            }
        }
    }
    Scene getMainScene(){
        return Main.getPrimaryStage().getScene();
    }
    Scene getPlotScene(){
        return plotStage.getScene();
    }




}
