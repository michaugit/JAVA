package sample;


import files.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


import java.io.File;
import java.text.ParseException;


public class Controller {
    DataFrame mainDataFrame;
    GroupDataFrame mainGroupedDataFrame;

    public void LoadFromFile(ActionEvent event){
       try {
           FileChooser fileChooser = new FileChooser();
           fileChooser.setTitle("Open File");
           fileChooser.setInitialDirectory(new File("C:\\Users\\resta\\Desktop"));
           fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

           File selectedFile;
           selectedFile = fileChooser.showOpenDialog(Main.getPrimaryStage());

           //tu trzeba raczej poprosić o typy kolumn albo coś
           mainDataFrame = new DataFrame(selectedFile.getAbsolutePath(), new Class[]{StringObject.class, DateObject.class, DoubleObject.class, DoubleObject.class});
           printCommand("File loaded!");
           transfromDataFrameToTableView(mainDataFrame);
       }
       catch(Exception e){}
    }

    private void transfromDataFrameToTableView(DataFrame DF){
        Scene scene= Main.getPrimaryStage().getScene();
        TableView table = (TableView) scene.lookup("#table");
        table.getItems().clear();
        table.getColumns().clear();

        for (int i = 0; i < DF.size() && i <= 100 ; i++) {
            table.getItems().add(i);
        }
        for(Column cln : DF.tab) {

            TableColumn<Integer, String> tableColumn = new TableColumn<>(cln.name);
            tableColumn.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(cln.data.get(rowIndex).toString());
            });
            table.getColumns().add(tableColumn);
        }
        if(DF.size()>100){
            Label command = (Label) scene.lookup("#commandLine");
            command.setUnderline(true);
            command.setText("The loaded file is too large, only the first 100 results were displayed.");
        }
        else{
            printCommand("");
        }
        table.setVisible(true);
    }

    public void maxClicked(ActionEvent event) throws InconsistentTypeException, InstantiationException, ParseException, IllegalAccessException {
        Scene scene= Main.getPrimaryStage().getScene();
        if(mainDataFrame == null){
            this.printCommand("Please load DataFrame!");
        }
        else {
            TextField k = (TextField) scene.lookup("#groupby");
            String[] keys = k.getText().split(", ");
            if(keys.length==1 && keys[0].isEmpty()){
                keys= new String[]{};
            }
            try {
                transfromDataFrameToTableView(mainDataFrame.groupBy(keys).max());
            }
            catch(RuntimeException e){
                this.printCommand(e.toString());
            }
        }
    }
    private void printCommand(String commandToPrint){
        Scene scene= Main.getPrimaryStage().getScene();
        Label command = (Label) scene.lookup("#commandLine");
        command.setUnderline(true);
        command.setText(commandToPrint);
    }







}
