package com.figengungor.thedictionary.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Entry {

@SerializedName("homographNumber")
@Expose
private String homographNumber;
@SerializedName("senses")
@Expose
private List<Sense> senses = null;

public String getHomographNumber() {
return homographNumber;
}

public void setHomographNumber(String homographNumber) {
this.homographNumber = homographNumber;
}

public List<Sense> getSenses() {
return senses;
}

public void setSenses(List<Sense> senses) {
this.senses = senses;
}

}