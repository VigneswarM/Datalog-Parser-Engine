package comp6591.main;

import java.util.Collection;

public class DatalogGlobal_unificaion_Param {
   

private String name;
private Collection<String> Predicates;
private Collection<Integer> index_in_predicte;
private Collection<Integer> predicte_appearance_index;
private int is_fitered ;
public DatalogGlobal_unificaion_Param() {
	is_fitered =0;
}
public int indexofar_inpredicate(int position)
{
	return (int) index_in_predicte.toArray()[position];


}

public int  get_visit_status()
{
return is_fitered;
}

public void  setis_fitered(int P_is_fitered   )
{
	is_fitered=P_is_fitered ;
}

public Collection<Integer>  getpredicte_appearance_index()
{
return predicte_appearance_index;
}

public void  setpredicate_appearance( Collection<Integer> P_predicte_appearance_index    )
{
	predicte_appearance_index=P_predicte_appearance_index ;
}
public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}
public Collection<String> getPredicates() {
return Predicates;
}
public void setPredicates(Collection<String> predicates) {
this.Predicates = predicates;
}

public Collection<Integer>  getindex_in_predicte()
{
return index_in_predicte;
}

public void  setindex_in_predicte( Collection<Integer> P_index_in_predicte    )
{
	index_in_predicte=P_index_in_predicte ;
}

}


