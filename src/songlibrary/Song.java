/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package songlibrary;
import java.util.Comparator;
/**
 *
 * @author Sam
 */
public class Song {
    //name and artist required, album and year optional
    //if exists, not allowed
    //editable
    //deletable
    public String name, artist, album, year;
    
    public Song(String name, String artist){
        this.name=name;
        this.artist=artist;
        this.album="";
        this.year="";
    }
    public Song(String name, String artist, String album, String year){
        this.name=name;
        this.artist=artist;
        this.album=album;
        this.year=year;
    }
    //For display of observablelist
    @Override
    public String toString(){
        //return (this.name + " " + this.artist + " " + this.album + " " + this.year);
        return this.name;
    }
    //For comparator function
    public String getName(){
        return this.name;
    }
    //For exists function
    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Song)){
            return false;
        }
        Song tmp = (Song)obj;
        if(this.name.equals(tmp.name) && this.artist.equals(tmp.artist)){
            return true;
        }
        return false;
    }
}