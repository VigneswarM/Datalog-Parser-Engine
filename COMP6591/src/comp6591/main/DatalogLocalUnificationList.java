package comp6591.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class DatalogLocalUnificationList {
   
private Integer RuleNB; 
private String name;
private Collection<String> parameters;
private ArrayList<Collection<String>> Locally_unified_instances;
private int predicate_index;
public DatalogLocalUnificationList() {
}
public Integer getRuleNB() {
return RuleNB;
}
public void setpredicate_index(int P_predicate_index) {
this.predicate_index = P_predicate_index;
}

public int getpredicate_index() {
return predicate_index;
}
public void setRuleNB(Integer P_RuleNB) {
this.RuleNB = P_RuleNB;
}


public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}
public Collection<String> getParameters() {
return parameters;
}
public void setParameters(Collection<String> parameters) {
this.parameters = parameters;
}
public int getParametersCount() {
return parameters.size();
}
public  ArrayList<Collection<String>>  getinstances()
{
return Locally_unified_instances;
}

public void  setinstances(ArrayList<Collection<String>> InstancesList )
{
Locally_unified_instances=InstancesList ;
}

}


