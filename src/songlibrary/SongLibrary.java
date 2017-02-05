/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package songlibrary;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import java.util.Comparator;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Sam
 * TODO
 * details *set editable to false? separate display on the side?
 * pop up dialog
 */
public class SongLibrary extends Application {
    ObservableList<Song> data = FXCollections.observableArrayList();
    boolean addPressed=false;
    boolean editPressed=false;
    
    @Override
    public void start(Stage primaryStage) {
        try{
            Scanner read = new Scanner(new File("C:\\Users\\Sam\\Desktop\\songLibrary.txt"));
            read.useDelimiter("\n");
            while(read.hasNextLine()){
                //System.out.println(read.next()+" ");
                String line = read.nextLine();
                String[] split = line.split(",",-1);
                data.add(new Song(split[0],split[1],split[2],split[3]));
            }
        }
        catch(IOException e){
            System.out.println("Something went wrong reading the file");
        }
        //data.add(new Song("Only One", "Kanye West"));
        //data.add(new Song("Hey Mama", "Kanye West"));
        //Sort by name
        data.sort(Comparator.comparing(Song::getName));
        ListView<Song> list = new ListView<Song>();
        list.setItems(data);
        if(!data.isEmpty()){
            list.getSelectionModel().select(0);
            //displayDetails();
        }
        
        //On selection change
        list.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<Song>() {
            @Override
            public void changed(ObservableValue<? extends Song> ov, 
                Song oldSong, Song newSong) {
                //Display details, also check if all songs are deleted newSong is not null
                if(newSong!=null){
                    System.out.println(newSong.name + " " + newSong.artist);
                }
            }
        });
        
        BorderPane border = new BorderPane();
        GridPane parameters = parameters(list);        
        
        VBox header = new VBox();
        HBox buttons = headers(parameters, list);
        header.getChildren().addAll(buttons,parameters);
        setVisibility(parameters);
        //header.getChildren().remove(parameters);
        border.setTop(header);
        border.setCenter(list);

        //grid.add(buttons,0,0);
        //grid.add(list,1,2);
        //ColumnConstraints col1 = new ColumnConstraints();
        //col1.setPercentWidth(25);
        //grid.getColumnConstraints().addAll(col1);
        //StackPane root = new StackPane();
        //root.getChildren().add(btn);
        //root.getChildren().add(list);
        //root.getChildren().addAll(list,btn);
        
        Scene scene = new Scene(border, 300, 500);
        
        primaryStage.setTitle("Song Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void stop(){
        try{
            PrintWriter writer = new PrintWriter("C:\\Users\\Sam\\Desktop\\songLibrary.txt", "UTF-8");
            for(int i=0; i<data.size(); i++){
                Song tmp = data.get(i);
                writer.println(tmp.name+","+tmp.artist+","+tmp.album+","+tmp.year);
            }
            writer.close();
        }
        catch (IOException e){
            System.out.println("Something went wrong");
        }
        
    }
    void setVisibility(GridPane parameters){
        if(parameters.isVisible() && parameters.isManaged()){
            parameters.setVisible(false);
            parameters.setManaged(false);
        }
        else{
            parameters.setVisible(true);
            parameters.setManaged(true);
        }
    }
    GridPane parameters(ListView<Song> list){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        
        Label songLabel = new Label("Song Name: ");
        TextField songName = new TextField();
        Label artistLabel = new Label("Artist Name: ");
        TextField artistName = new TextField();
        Label albumLabel = new Label("Album Name: ");
        TextField albumName = new TextField();
        Label yearLabel = new Label("Year Name: ");
        TextField yearName = new TextField();
        Button addSong = new Button();
        addSong.setText("Submit");
        addSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = songName.getText();
                String artist = artistName.getText();
                String album = albumName.getText();
                String year = yearName.getText();
                if(name.isEmpty() || artist.isEmpty()){
                    //Empty 
                }
                else{
                    Song tmp = new Song(name,artist,album,year);
                    if(data.contains(tmp)){
                        //pop out instead
                        System.out.println("Exists");
                    }
                    else{
                        list.getItems().add(list.getItems().size(), tmp);
                        list.getSelectionModel().select(list.getItems().size()-1);
                    }
                    setVisibility(grid);
                    data.sort(Comparator.comparing(Song::getName));
                    addPressed=false;
                }
            }
        });
        Button editSong = new Button();
        editSong.setText("Edit");
        editSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = songName.getText();
                String artist = artistName.getText();
                String album = albumName.getText();
                String year = yearName.getText();
                if(name.isEmpty() || artist.isEmpty()){
                    //Empty cannot continue
                }
                else{
                    Song tmp = new Song(name,artist,album,year);
                    if(data.contains(tmp)){
                        //pop out instead
                        System.out.println("Exists");
                    }
                    else{
                        data.remove(list.getSelectionModel().getSelectedIndex());
                        list.getItems().add(list.getItems().size(), tmp);
                        list.getSelectionModel().select(list.getItems().size()-1);
                    }
                    setVisibility(grid);
                    data.sort(Comparator.comparing(Song::getName));
                    editPressed=false;
                }
            }
        });
        
        //Add listener
        grid.add(songLabel, 0,1);
        grid.add(songName, 1,1);
        grid.add(artistLabel, 0,2);
        grid.add(artistName, 1,2);
        grid.add(albumLabel, 0,3);
        grid.add(albumName, 1,3);
        grid.add(yearLabel, 0,4);
        grid.add(yearName, 1,4);
        grid.add(addSong,0,5);
        grid.add(editSong,1,5);
        return grid;
    }
    HBox headers(GridPane parameters, ListView<Song> list){
        TextField songName = (TextField)parameters.getChildren().get(1);
        TextField artistName =(TextField)parameters.getChildren().get(3);
        TextField albumName =(TextField)parameters.getChildren().get(5);
        TextField yearName =(TextField)parameters.getChildren().get(7);
        Button editSubmit = (Button)parameters.getChildren().get(9);
        Button addSubmit = (Button)parameters.getChildren().get(8);
        Button add = new Button();
        add.setText("Add Song");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!addPressed && editPressed){
                    addPressed=true;
                    editPressed=false;
                }
                else if(!addPressed && !editPressed){
                    addPressed=true;
                    setVisibility(parameters);
                }
                else if(addPressed){
                    addPressed=false;
                    setVisibility(parameters);
                }
                addSubmit.setVisible(true);
                addSubmit.setManaged(true);
                editSubmit.setVisible(false);
                editSubmit.setManaged(false);
                songName.setText("");
                artistName.setText("");
                albumName.setText("");
                yearName.setText("");
                /*
                Song newSong = new Song("Gold Digger", "Kanye West");
                //Check if song exists
                if(data.contains(newSong)){
                    //popup instead
                    System.out.println("Exists");
                }
                else{
                    list.getItems().add(list.getItems().size(), newSong);
                    list.getSelectionModel().select(list.getItems().size()-1);
                }
                */          
                //list.scrollTo(list.getItems().size()-1);
                //list.edit(list.getItems().size()-1);
            }
        });
        Button edit = new Button();
        edit.setText("Edit Song");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(addPressed && !editPressed){
                    editPressed=true;
                    addPressed=false;
                }
                else if(!addPressed && !editPressed){
                    editPressed=true;
                    setVisibility(parameters);
                }
                else if(editPressed){
                    editPressed=false;
                    setVisibility(parameters);
                }
                addSubmit.setVisible(false);
                addSubmit.setManaged(false);
                editSubmit.setVisible(true);
                editSubmit.setManaged(true);
                //list.edit(list.getSelectionModel().getSelectedIndex());
                /*
                0 1
                2 3
                4 5
                6 7
                8 9
                */
                if(!data.isEmpty()){
                    Song newSong = list.getItems().get(list.getSelectionModel().getSelectedIndex());
                    songName.setText(newSong.name);
                    artistName.setText(newSong.artist);
                    albumName.setText(newSong.album);
                    yearName.setText(newSong.year);
                }
                //setVisibility(parameters);
                for(int i=0; i<data.size(); i++){
                    Song tmp = data.get(i);
                    System.out.println(tmp.name+","+tmp.artist+","+tmp.album+","+tmp.year);
                }
            }
        });
        
        Button delete = new Button();
        delete.setText("Delete Song");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!data.isEmpty()){
                    int pos=list.getSelectionModel().getSelectedIndex();
                    data.remove(pos);
                    if(pos==list.getItems().size()-1){
                        list.getSelectionModel().select(list.getItems().size()-1);
                    }
                    else{
                        list.getSelectionModel().select(pos);
                    }
                    //list.getSelectionModel().select(list.getItems().size()-1);
                }
            }
        });
        HBox buttons = new HBox();
        buttons.setPadding(new Insets(15,12,15,12));
        buttons.setSpacing(10);
        buttons.getChildren().addAll(add, edit, delete);
        return buttons;
    }
}