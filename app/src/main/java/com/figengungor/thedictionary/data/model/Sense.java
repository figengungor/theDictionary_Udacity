package com.figengungor.thedictionary.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sense {

@SerializedName("definitions")
@Expose
private List<String> definitions = null;
@SerializedName("examples")
@Expose
private List<Example> examples = null;
@SerializedName("id")
@Expose
private String id;
@SerializedName("subsenses")
@Expose
private List<Subsense> subsenses = null;

public List<String> getDefinitions() {
return definitions;
}

public void setDefinitions(List<String> definitions) {
this.definitions = definitions;
}

public List<Example> getExamples() {
return examples;
}

public void setExamples(List<Example> examples) {
this.examples = examples;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public List<Subsense> getSubsenses() {
return subsenses;
}

public void setSubsenses(List<Subsense> subsenses) {
this.subsenses = subsenses;
}

}