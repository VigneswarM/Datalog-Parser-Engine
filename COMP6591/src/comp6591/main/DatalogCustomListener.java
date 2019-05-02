package comp6591.main;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.crypto.KeyGenerator;
import javax.sql.rowset.Predicate;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import comp6591.antlr.DatalogBaseListener;
import comp6591.antlr.DatalogParser;
import comp6591.antlr.DatalogParser.ParameterContext;
import java.util.Set;

import java.util.Map;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import comp6591.antlr.DatalogBaseListener;
import comp6591.antlr.DatalogParser;
import comp6591.antlr.DatalogParser.ParameterContext;
import comp6591.utils.PrintColor;
import java.util.HashSet;
public class DatalogCustomListener extends DatalogBaseListener {

	private static int ruleNb;
	Collection<String> currentBodyParams;
	Collection<String> currentHeadParams;
	private HashMap<Integer, String> paramsLinesNumber;
	private HashMap<String, Object> userConfig;
	private HashMap<Integer, List<DatalogPredicate>> predicatesList;
	private List<DatalogPredicate> currentPredicatesList;
	private Map<Integer, List<DatalogPredicate>> factsList = new HashMap<Integer, List<DatalogPredicate>>();
	List<String> safetyErrorMsgs;
	// PrintColorWriter colorPrint;

	public DatalogCustomListener(HashMap<String, Object> conf) throws UnsupportedEncodingException {
		userConfig = conf;
		ruleNb = 1;
		safetyErrorMsgs = new ArrayList<String>();
		paramsLinesNumber = new HashMap<Integer, String>();
		predicatesList = new HashMap<Integer, List<DatalogPredicate>>();
		// colorPrint = new PrintColorWriter(System.out);
	}

	@Override
	public void enterARule(DatalogParser.ARuleContext ctx) {
		currentPredicatesList = new LinkedList<DatalogPredicate>();
		predicatesList.put(ruleNb, currentPredicatesList);
	}

	@Override
	public void enterFact(DatalogParser.FactContext ctx) {
		currentPredicatesList = new LinkedList<DatalogPredicate>();
		factsList.put(ruleNb, currentPredicatesList);
	}

	@Override
	public void exitARule(DatalogParser.ARuleContext ctx) {
		ruleNb++;
	}

	@Override
	public void exitHeadPredicate(DatalogParser.HeadPredicateContext ctx) {

		// collect all head predicates from all rules
		String firstHeadParam = ctx.ID().toString();

		// substring is used to remove the first comma from the first ID
		currentHeadParams = new ArrayList<String>(Arrays.asList(firstHeadParam));

		// check if we have more than one parameter in predicate
		if (ctx.idList().getText().length() != 0) {
			String otherHeadParameters = ctx.idList().getText().substring(1);
			currentHeadParams.addAll(Arrays.asList(otherHeadParameters.split(",")));
		}
		Token pos = ctx.getStart();
		paramsLinesNumber.put(ruleNb, pos.getLine() + ":" + pos.getCharPositionInLine());

		// set predicate
		DatalogPredicate predicate = new DatalogPredicate();
		predicate.setName(ctx.PREDICATE_NAME().getText());
		predicate.setType(Type.HEAD);
		predicate.setParameters(currentHeadParams);
		predicatesList.get(ruleNb).add(predicate);
		currentHeadParams = new ArrayList<String>();
	}

	@Override
	public void exitPredicate(DatalogParser.PredicateContext ctx) {

		DatalogPredicate predicate = new DatalogPredicate();

		// normal predicate
		if (ctx.PREDICATE_NAME() != null) {
			predicate.setType(Type.BODY);
			predicate.setName(ctx.PREDICATE_NAME().getText());
			// predicate.setParameters(currentBodyParams);
		}
		// built in predicate
		else if (ctx.getChild(0).getClass().getSimpleName().equals("BuiltInPredicateContext")) {
			predicate.setType(Type.BUILTIN);
			String operator = ctx.builtInPredicate().operator().getText();
			predicate.setOperator(operator);
			// predicate.setParameters(currentHeadParams);
		}
		predicate.setParameters(currentBodyParams);
		predicatesList.get(ruleNb).add(predicate);
		// currentHeadParams = new ArrayList<String>();
		currentBodyParams = new ArrayList<String>(); // reset body params because .clear() is not working as it should
	}

	@Override
	public void exitFact(DatalogParser.FactContext ctx) {

		// get all params
		String firstParam = ctx.STRING().getText();
		currentBodyParams = new ArrayList<String>(Arrays.asList(firstParam));
		if (ctx.stringList().getText().length() != 0) {
			String otherFactParams = ctx.stringList().getText().substring(1);
			currentBodyParams.addAll(Arrays.asList(otherFactParams.split(",")));
		}

		// set fact predicate
		DatalogPredicate predicate = new DatalogPredicate();
		predicate.setType(Type.FACT);
		predicate.setName(ctx.PREDICATE_NAME().getText());
		predicate.setParameters(currentBodyParams);
		
		factsList.get(ruleNb).add(predicate);
		currentBodyParams = new ArrayList<String>(); // reset body params because .clear() is not working as it should
		ruleNb++;
		
		}

	@Override
	public void exitParameter(ParameterContext ctx) {

		// body predicate -- collect alL parameters and compare Later with head
		// predicate to ensure safety
		String paramText = ctx.getText();
		String grandParentContext = ctx.getParent().getParent().getClass().getSimpleName();
		String parentContext = ctx.getParent().getClass().getSimpleName();

		if (grandParentContext.equals("ARuleContext")) { // new rule
			currentBodyParams = new ArrayList<String>();
		}
		currentBodyParams.add(paramText);
	}

	@Override
	public void exitDatalogProgram(DatalogParser.DatalogProgramContext ctx) {

		// DEBUG
		if ((boolean) userConfig.get("debug"))
			System.out.println("predicatesList:\n " + predicatesList + "\n");

		// check safety rules - a clause is safe if every variable in its head occurs in
		// some literal in its body
		System.out.print(">> Checking Safety Rules... ");
		for (Entry<Integer, List<DatalogPredicate>> predicate : predicatesList.entrySet()) {
			checkSafetyRule(predicate);
		}

		// show safety errors if program is not safe
		if (safetyErrorMsgs.size() == 0) {
			// colorPrint.println(PrintColor.GREEN, PrintColor.CHECK_MARK +" A green
			// message");
			// colorPrint.println(PrintColor.RED, PrintColor.ERROR_MARK +" A red message");
			// colorPrint.green(PrintColor.CHECK_MARK +" A green message");
			// colorPrint.red(PrintColor.ERROR_MARK +" A red message");
			System.out.println("No issues " + PrintColor.CHECK_MARK);
			
			if (factsList.size()!= 0 )//if no facts in the program then no inference :)
			{	
				int factlist_size=0 ;
			   // int iteration_counter = 0;
			 Collection<List<DatalogPredicate>> new_facts = new ArrayList<>();
                	//this will be used o know fixed point
                	 factlist_size = factsList.size();

                	//now check if the rule have instances for all body predicates 
                	 
                     for (Entry<Integer, List<DatalogPredicate>> RulesEntry : predicatesList.entrySet())//TODO :Double Check if any duplication elemenated 
					//for each rule in the program
				{ int fire_rule= 1;

          //shouldnt worry about arit ,bring all prediate names frm rule entry
				Collection<String> bodypredOnly = 
						RulesEntry.getValue().stream().
						filter(p->p.getType().equals(Type.BODY))
						.map(p->p.getName()).
						collect(Collectors.toSet());

//take every single bod predicate name and check its availabiliy at facts list
				for (String BodyPred:bodypredOnly )
				{    int instancecounter = 0;
				for ( Entry<Integer, List<DatalogPredicate>>  factPred : factsList.entrySet()   )
				{   
					//do we have any instance of thi pred name in facts list?
					Collection<String> PredicateInstance = 

							factPred.getValue().stream().
							filter(p->p.getName().equals(BodyPred))
							.map(p->p.getParameters())
							.flatMap( p -> p.stream())
							.collect(Collectors.toList()); 

					//if list counter is greater than zero then we have instance
					if(PredicateInstance.size()>0)
					{instancecounter ++;
					break;}//FINDING ONE INSTANCE IS ENOUGH,if ou find it quit :)
					//System.out.println(BodyPred+"   " +PredicateInstance);

				}
				if (instancecounter == 0 )//if no instances after checking all fact list for som predicate then dont fire this rule
					//no need to check the rest of the predicates
				{ fire_rule = 0;
				break;}//if you finishing search and dont find an instances for a predicate ,skip and dont coninue with the rule

				}

				if(fire_rule == 1)
				{
					System.out.println(RulesEntry.getKey()+" will fire now");
					
					Map<Integer, DatalogLocalUnificationList> all_locally_unifiedList = new HashMap<Integer,DatalogLocalUnificationList>();
					int map_key = 0;
					int instance_Predicate_index =-1;
				
					
					/////////////LOCAL UNIFICATION
					
					//ge intnces +set indx for predicates
							for( DatalogPredicate Predicate  :RulesEntry.getValue() )//we guarntee that we have at least on predicate 
						//in a rule body ,so this loop will always work 
					{

						if (Predicate.getType() == Type.BODY) // if a body predicate try to get instances
						{ instance_Predicate_index++;        
						int need_local_unification = 0;
						List< List<Integer>> allIndexes = new ArrayList <>();
						ArrayList<Collection<String>> Locally_unified_instances=new ArrayList<Collection<String>>();
						String Pediate_name=Predicate.getName();
						Collection<String> Predicat_parameters = Predicate.getParameters();
						Collection<String> Variables_to_unify = new ArrayList<String>();
						//check if need local unification or not
						//get instances
						Collection<String> PredicateInstance = new ArrayList<String>();
						if(Predicat_parameters.size() >1 )//it might need local unification
						{
//check varables in predicate if it has any dupication stack it at Variables_to_unify
							for(String Variable_name: Predicat_parameters )
							{ 
								//check frequency of var name in predicat parameters
								//String isexisting_var = 
								long is_already_in_list=0;
								if(!Variables_to_unify.isEmpty())
								{ is_already_in_list= IntStream.range(0, Variables_to_unify.size()).boxed()
								.filter(z -> Variables_to_unify.toArray()[z].equals(Variable_name)).count();}


								if( Collections.frequency(Predicat_parameters, Variable_name)>1  && (is_already_in_list==0)  )
								{
                                          //get all indexes where this variable appears
									allIndexes.add(IntStream.range(0, Predicat_parameters.size()).boxed()
											.filter(x -> Predicat_parameters.toArray()[x].equals(Variable_name))
											.collect(Collectors.toList()));


									//output 
									if(need_local_unification == 0 )
										need_local_unification = 1;
									//System.out.println(indeces_to_unify);
									//should reconsider if Variables_to_unify needed or not?? 
									//add variables to list only if it needs unification 
									Variables_to_unify.add(Variable_name);


								}//end of  if( Collections.frequency(Predicat_parameters, Variable_name)>1   )

							}//end of  for(String Variable_name: Predicat_parameters ) 
							//at this point i have indexes i need to unify
							if (need_local_unification==1 )
							{ 
								//System.out.print("indexes   "+Pediate_name +"  "+allIndexes); 
								for (Entry<Integer, List<DatalogPredicate>> fact : factsList.entrySet())

								{ 
									PredicateInstance= 
											fact.getValue().stream(). 
											filter(p->p.getName().equals(Pediate_name))
											.map(p->p.getParameters())
											.flatMap( p -> p.stream())
											.collect(Collectors.toList());
									int is_locally_unified = 1;
									//if the fact is a predicate instance ,then predicateinstance is not empty
									if(!PredicateInstance.isEmpty())//if the fact is an instance then unify it
									{
										for(List<Integer> indexList :  allIndexes ) 
										{

											String First_value =PredicateInstance.toArray()[indexList.get(0)].toString();
											for(int loop_to_unify = 1;loop_to_unify<indexList.size();loop_to_unify++)
											{      
												if(!PredicateInstance.toArray()[indexList.get(loop_to_unify)].equals(First_value))
													//in case loop finds on not unifiable variable exist the whole processs
												{is_locally_unified =0;
												break;
												} 
											}
										}
										if(is_locally_unified == 1)
										{
											Locally_unified_instances.add( PredicateInstance);
										}
										else
										{
											// System.out.println("not locally unified  "+PredicateInstance);


										}
									}


								}
							
								
								
								
								

								//Map<Integer, DatalogLocalUnificationList> all_locally_unifiedList = new HashMap<Integer,  
								//create DatalogLocalUnificationList object
								DatalogLocalUnificationList Predicate_with_instances = new DatalogLocalUnificationList();
								Predicate_with_instances.setName(Pediate_name);
								Predicate_with_instances.setpredicate_index(instance_Predicate_index);
								Predicate_with_instances.setRuleNB(RulesEntry.getKey());
								Predicate_with_instances.setParameters(Predicate.getParameters());
								Predicate_with_instances.setinstances(Locally_unified_instances);
								all_locally_unifiedList.put(map_key++, Predicate_with_instances);
								// System.out.println("locally unified  "+Locally_unified_instances);
								System.out.print("unification done here  "+instance_Predicate_index+"   " +Pediate_name);


							}//end of need local unification
							else
							{
								 System.out.print("no need to unify no multiple occurance var "+instance_Predicate_index+"   " +Pediate_name);



								for (Entry<Integer, List<DatalogPredicate>> fact1 : factsList.entrySet())

								{ 
									PredicateInstance= 
											fact1.getValue().stream(). 
											filter(p->p.getName().equals(Pediate_name))
											.map(p->p.getParameters())
											.flatMap( p -> p.stream())
											.collect(Collectors.toList());
									if(!PredicateInstance.isEmpty())//if the fact is an instance then unify it
									{
										//if we got an instance add it to array          
										Locally_unified_instances.add( PredicateInstance);  

									}   
								}

								DatalogLocalUnificationList Predicate_with_instances = new DatalogLocalUnificationList();
								Predicate_with_instances.setName(Pediate_name);
								Predicate_with_instances.setRuleNB(RulesEntry.getKey());
								Predicate_with_instances.setParameters(Predicate.getParameters());
								Predicate_with_instances.setpredicate_index(instance_Predicate_index); 
								Predicate_with_instances.setinstances(Locally_unified_instances);

								all_locally_unifiedList.put(map_key++, Predicate_with_instances);

								// System.out.println("all locallu ynnified   "+ all_locally_unifiedList.values());
							}

						}
						else
						{
							 System.out.print("no need to unify param counter is 1 "+instance_Predicate_index +"  "+Pediate_name);





							for (Entry<Integer, List<DatalogPredicate>> fact1 : factsList.entrySet())

							{ 
								PredicateInstance= 
										fact1.getValue().stream(). 
										filter(p->p.getName().equals(Pediate_name))
										.map(p->p.getParameters())
										.flatMap( p -> p.stream())
										.collect(Collectors.toList());
								if(!PredicateInstance.isEmpty())//if the fact is an instance then unify it
								{
									//if we got an instance add it to array          
									Locally_unified_instances.add( PredicateInstance);  

								}   
							}

							DatalogLocalUnificationList Predicate_with_instances = new DatalogLocalUnificationList();
							Predicate_with_instances.setName(Pediate_name);
							Predicate_with_instances.setRuleNB(RulesEntry.getKey());
							Predicate_with_instances.setpredicate_index(instance_Predicate_index);
							Predicate_with_instances.setParameters(Predicate.getParameters());
							Predicate_with_instances.setinstances(Locally_unified_instances);
							all_locally_unifiedList.put(map_key++, Predicate_with_instances);
						}}
					}
                      System.out.println(all_locally_unifiedList);
					//global unification should start here
//					//if rule have one predicate ,we are done-> infer 
//					////////////////////////////////////////////////////
//
//					if (RulesEntry.getValue().size()==2)
//					{
//						//go to rulenb and get head predicate and infer
//						//get head name an inferred values 
//						for(DatalogPredicate HeadPred:RulesEntry.getValue())
//						{if(HeadPred.getType()== Type.HEAD)//get head name and params name
//						{     Collection<Integer> Indecies = new ArrayList<Integer>();
//						// HeadPred.getParameters() map vriable with its value in instance
//						for(String Param: HeadPred.getParameters())//search each variable position
//						{ int found =0;
//						for(DatalogPredicate bodyPred:RulesEntry.getValue())
//						{if(bodyPred.getType()== Type.BODY)//get head name and params name
//						{   int index=0; 
//
//						for(String bdyvar:  bodyPred.getParameters())
//						{
//							if(bdyvar.equals(Param))
//							{found  =1 ;
//							break;}
//							index++;
//						}
//						if (found == 1)//if variable exist in predicate 
//						{    //must add it to indecies filter                 }
//							Indecies.add(index);
//
//						}
//						}  //if(bodyPred.getType
//
//						}//  for(DatalogPredicate bodyPred:
//
//						}// for(String Param: HeadPred
//						// System.out.println("global indexes  "+Indecies); 
//						//here get all instances to infer
//						//loop all instances
//						for (Entry<Integer, DatalogLocalUnificationList> entry : all_locally_unifiedList.entrySet())
//						{  
//							for(    Collection<String> Instance  :   entry.getValue().getinstances())
//							{List<DatalogPredicate> new_tuple=new ArrayList<>() ;
//							DatalogPredicate IDBfact=new DatalogPredicate();
//							IDBfact.setName(HeadPred.getName());
//							IDBfact.setType(Type.IDBFACT);
//							Collection<String> IDBfact_parameters= new ArrayList<>();
//							for(int index :Indecies)
//							{ 
//								IDBfact_parameters.add( Instance.toArray()[index].toString()  );
//							}
//							IDBfact.setParameters(IDBfact_parameters);
//							//System.out.println("NEW tuple here for predicate "+HeadPred.getName()+"  "+IDBfact_parameters );
//							//MUST CHECK NOT TO ADd DUPLICATE FACT HERE
//							new_tuple.add(IDBfact);
//							int exist = 0;
//							for(    Entry<Integer, List<DatalogPredicate>> check_exist:        factsList.entrySet()      )
//							{
//								for(   DatalogPredicate Pred:      check_exist.getValue()  )
//								
//									{
//									//if needs fix but it didnt work properly without this stp
//									//int contained= 0;
//									//if(IDBfact.getParameters().containsAll(Pred.getParameters()))
//									//	contained = 1;
//									if(Pred.getName().equals(IDBfact.getName())  &&   Pred.getType().equals(IDBfact.getType()) && Pred.getParameters().containsAll(IDBfact.getParameters()) )                        
//									{
//									 
//										
//									 exist = 1;
//								      break;	
//								}
//								}
//								
//							}
//								
//							if(exist==0 && ( IDBfact.getParametersCount() !=0     ))
//							{
//							//new_facts.add(new_tuple);	
//							factsList.put(ruleNb++,new_tuple);
//							}
//							}//end of instance
//
//						}//for (Entry<Integer, DatalogLocalUnificationList>
//						} 
//
//
//						}
//					}
//
//					else//if compound rule 
//					{
//
//						//do global unification before instantiation 
//						//need map of form key -> globaluniicationparam
//						Map <Integer,DatalogGlobal_unificaion_Param>  RuleGobalUnificaion=new HashMap<>();
//						int[]  Visit_log= new int[100] ;
//						for(int LoopI=0;LoopI<(RulesEntry.getValue().size()-1);LoopI++)//TODO:remove built in pred from the loop
//						{ //System.out.println(LoopI);
//
//							Visit_log[LoopI]=0;
//						}
//						int VISIT_INDEX = 0;
//						for(DatalogPredicate bodyPred:RulesEntry.getValue())
//						{if(bodyPred.getType()== Type.BODY)//if the predicate is body then search each variable
//						{    
//							for( String  Predparameter: bodyPred.getParameters() )
//							{//X for example
//								Collection<String>   Predicates= new ArrayList<>();
//								Collection<Integer> index_in_predicte = new ArrayList<>();
//								Collection<Integer> predicte_appearance = new ArrayList<>();
//								int variables_added=0;
//								if( RuleGobalUnificaion.entrySet().size()!= 0 )//go ceck if it aready exist
//								{
//									for(Entry<Integer, DatalogGlobal_unificaion_Param> LOOP: RuleGobalUnificaion.entrySet()) 
//									{ if(LOOP.getValue().getName().equals(Predparameter))
//									{variables_added = 1;
//									break;
//									}
//									}
//								} 
//								if( RuleGobalUnificaion.size()== 0 ||  variables_added ==0)//if need to add it to map
//									//should search unvisited predicates only
//								{ int predicate_index = 0;
//
//								for(DatalogPredicate searchindexpred:RulesEntry.getValue())
//								{if(searchindexpred.getType()== Type.BODY &&  Visit_log[predicate_index]==0)//if the predicate is body then search each variable
//								{
//									int j = 0;
//									for(  String  Variable    : searchindexpred.getParameters()   )
//									{
//										if(Variable.equals(Predparameter))
//										{
//											//if an occurrance is found YAY!  
//											//predicate name :
//											//must get the name and declaration a lil bit out fr no reptiion
//											Predicates.add(searchindexpred.getName());
//											index_in_predicte.add( j  );
//											predicte_appearance.add(predicate_index-1);
//											break;
//										}
//										j++;
//									}
//								}
//								predicate_index++;
//								}
//								if(Predicates.size()> 1)
//								{ DatalogGlobal_unificaion_Param ParaObj = new DatalogGlobal_unificaion_Param();
//								ParaObj.setName(Predparameter);
//								ParaObj.setindex_in_predicte(index_in_predicte);
//								ParaObj.setPredicates(Predicates);
//								ParaObj.setpredicate_appearance(predicte_appearance);
//								RuleGobalUnificaion.put(RuleGobalUnificaion.size(), ParaObj) ;
//								}
//								// System.out.println("New var  "+ ParaObj.getName()); 
//								// System.out.println(index_in_predicte); 
//
//
//								}
//								//here should add to index map
//							}
//							Visit_log[VISIT_INDEX]=1;
//							VISIT_INDEX ++;  
//						}
//						}
//
//						// System.out.println("var and indexes map  ");
//						// System.out.println(RuleGobalUnificaion.entrySet());
//						//get max apearance var
//						//to choose nxt flter
//						int max_appearance = -1;
//						String max_param="";
//						for(  Entry<Integer, DatalogGlobal_unificaion_Param> Variale_of_max     :RuleGobalUnificaion.entrySet())
//						{
//							if( Variale_of_max.getValue().get_visit_status()  ==0 )
//							{
//								if(Variale_of_max.getValue().getPredicates().size()>max_appearance )
//								{ max_appearance=Variale_of_max.getValue().getPredicates().size();
//								max_param=Variale_of_max.getValue().getName();
//								}
//							}
//						}
//
//
//
//						// Map<Integer, DatalogLocalUnificationList> Filtered_Instances = new HashMap<Integer,DatalogLocalUnificationList>();
//						do {
//							//System.out.println(all_locally_unifiedList.entrySet()); 
//							//max_param has the filter name
//							//loop the variables list again and get this varible 
//							for(  Entry<Integer, DatalogGlobal_unificaion_Param> Variale_to_unify     :RuleGobalUnificaion.entrySet())
//							{
//								if( Variale_to_unify.getValue().getName().equals(max_param) )//if we are at filtr position
//								{//loop all its predicate poitions
//									int predicate_index =-1;
//									ArrayList<Set <String>> set_all_varaible_values = new ArrayList<>();
//									for( int pred_pod :  Variale_to_unify.getValue().getpredicte_appearance_index()  )
//									{  //search that predicate from instances list
//										// pred_pos is the pos of the predicte am looking for  
//										predicate_index ++;
//										int loop =-1;
//										for (Entry<Integer, DatalogLocalUnificationList> InstanceEntry : all_locally_unifiedList.entrySet())
//										{loop++;
//										//get the pred am looking for 
//
//
//										if(pred_pod == InstanceEntry.getValue().getpredicate_index() )//if i got the predicte am looking for  in te in
//										{      
//											//now i have all instances of the predicate and need to take the vaue at the desirde index
//											// InstanceEntry.getValue().getinstances() 
//											//need this index in all instances
//											Set<String> all_possible_values= new HashSet<>();
//
//											int value_to_map=Variale_to_unify.getValue().indexofar_inpredicate(predicate_index);
//
//											for(   Collection<String> getvalue   : InstanceEntry.getValue().getinstances())
//											{ int index = 0;
//
//
//
//											// InstanceEntry.getValue().getpredicate_index();
//											for(String val_in_instance :getvalue)
//											{
//
//												if( index == value_to_map)
//													all_possible_values.add(val_in_instance) ;
//
//												index++;
//											}
//
//
//											}
//											set_all_varaible_values.add(  all_possible_values);
//
//										}
//
//
//										}
//
//										//     Collection<String> Temp =set_all_varaible_values.stream()
//										//          .distinct()
//										//          .collect(Collectors.toList());
//										//     set_all_varaible_values= Temp;
//
//									}
//									//
//
//									//System.out.println("set of all values for variable  "+Variale_to_unify.getValue().getName() +"  "+  set_all_varaible_values); 
//
//									Variale_to_unify.getValue().setis_fitered(1);//dont visit it again
//									//now must do intersection between the set_all_varaible_values elements
//									Set<String> intersected_values = new HashSet<>();
//									Set<String> initial_set =set_all_varaible_values.get(0);
//									int Counter =0;
//									for (String  val:initial_set)
//									{//search appearance of each element
//										Counter =0;
//										for(  Set<String>  Search_sets   :set_all_varaible_values)
//										{ for(  String search_Word:   Search_sets )
//										{
//											if(search_Word.equals(val))
//												Counter++;
//										}
//
//										}
//										if( Counter  > 1)
//											intersected_values.add(val) ;
//									}
//
//									//System.out.println("intersectin  "+intersected_values);
//									//now filter the instances of the rule 
//									//////////////////////////FILTER
//
//
//
//
//									int filter_predicate_index =-1;
//									ArrayList<Collection<String>> Filtered_instances = new ArrayList<>();
//									for( int pred_pod :  Variale_to_unify.getValue().getpredicte_appearance_index()  )
//									{  //search that predicate from instances list
//										// pred_pos is the pos of the predicte am looking for  
//										filter_predicate_index ++;
//										int i_loop =-1;
//										for (Entry<Integer, DatalogLocalUnificationList> InstanceEntry : all_locally_unifiedList.entrySet())
//										{i_loop++;
//										//get the pred am looking for 
//
//
//										if(pred_pod == InstanceEntry.getValue().getpredicate_index() )//if i got the predicte am looking for  in te in
//										{      
//											//now i have all instances of the predicate and need to take the vaue at the desirde index
//											// InstanceEntry.getValue().getinstances() 
//											//need this index in all instances
//											ArrayList<Collection<String>> instances= new ArrayList<>();
//
//											int value_to_map=Variale_to_unify.getValue().indexofar_inpredicate(filter_predicate_index);
//
//											for(   Collection<String> getvalue   : InstanceEntry.getValue().getinstances())
//											{ int index = 0;
//											// InstanceEntry.getValue().getpredicate_index();
//											for(String val_in_instance :getvalue)
//											{
//
//												if( index == value_to_map)
//												{
//													//value here is in intersection list
//													for( String Serch_word:   intersected_values )
//													{
//														if(   Serch_word.equals(val_in_instance)         )
//														{//WE HAVE AN INSTANCE HERE!!
//															instances.add(getvalue);
//														}
//													}
//
//												}
//												//    add instances list to the map   
//
//												index++;
//											}
//
//
//											}
//											// Filtered_instances.add(  all_possible_values);
//											// Map<Integer, DatalogLocalUnificationList> Filtered_Instances = new HashMap<Integer,DatalogLocalUnificationList>();
//											//        DatalogLocalUnificationList New_instance = new  DatalogLocalUnificationList();
//											//        New_instance.setinstances(instances);
//											//        New_instance.setRuleNB(InstanceEntry.getValue().getRuleNB());
//											//        New_instance.setParameters(InstanceEntry.getValue().getParameters());
//											//                                   New_instance.setName(InstanceEntry.getValue().getName());
//											//                                   Filtered_Instances.put(Filtered_Instances.size(), New_instance) ;
//											//change instances list
//											InstanceEntry.getValue().setinstances(instances);
//										}
//
//
//										}
//
//
//
//										//     Collection<String> Temp =set_all_varaible_values.stream()
//										//          .distinct()
//										//          .collect(Collectors.toList());
//										//     set_all_varaible_values= Temp;
//									}
//									//System.out.println("filtered instances  "+all_locally_unifiedList);
//									//
//
//									//  System.out.println("set of all values for variable  "+Variale_to_unify.getValue().getName() +"  "+  set_all_varaible_values); 
//								}//if is max param
//
//								//now im done with this var. gt next max
//								max_appearance = -1;
//
//
//								for(  Entry<Integer, DatalogGlobal_unificaion_Param> Variale_of_max     :RuleGobalUnificaion.entrySet())
//								{
//									if( Variale_of_max.getValue().get_visit_status()  ==0 )
//									{
//										if(Variale_of_max.getValue().getPredicates().size()>max_appearance )
//										{ max_appearance=Variale_of_max.getValue().getPredicates().size();
//										max_param=Variale_of_max.getValue().getName();
//										}
//									}
//								}
//								//now i have next univisited 
//							}
//						}while(max_appearance != -1);
//
//						//////////////////////////INFER NOW 
//
//
//						for(DatalogPredicate HeadPred:RulesEntry.getValue())//go to head parameter
//						{if(HeadPred.getType()== Type.HEAD)//get head name and params name
//						{     Collection<Integer> Indecies = new ArrayList<Integer>();
//						Collection<Integer> Predicate_index = new ArrayList<Integer>();
//						// HeadPred.getParameters() map vriable with its value in instance
//						int body_predicate_index=0;
//						for(String Param: HeadPred.getParameters())//search each variable position
//						{ int found =0;
//						for(DatalogPredicate bodyPred:RulesEntry.getValue())
//						{if(bodyPred.getType()== Type.BODY)//get head name and params name
//						{   int index=0; 
//
//						for(String bdyvar:  bodyPred.getParameters())
//						{
//							if(bdyvar.equals(Param))
//							{found  =1 ;
//							break;}
//							index++;
//						}
//						if (found == 1)//if variable exist in predicate 
//						{    //must add it to indecies filter                 }
//							Indecies.add(index);
//							Predicate_index.add(body_predicate_index);
//						}
//						}  //if(bodyPred.getType
//						if(found ==1 ) break; 
//						}//  for(DatalogPredicate bodyPred:
//						body_predicate_index++;
//						}
//						//System.out.println("LAAAAAAAAST  "+Indecies);
//						//System.out.println("IN predicates  "+Predicate_index);
//						Collection<String> IDBfact_parameters= new ArrayList<>();
//						for (Entry<Integer, DatalogLocalUnificationList> entry : all_locally_unifiedList.entrySet())
//						{
//							int predicate_loop=0;
//							for(int P_index:Predicate_index)
//							{   int target_inex=entry.getValue().getpredicate_index();
//							if(P_index ==target_inex )//if the predicate is found
//							{//now loop the instances 
//								ArrayList<Integer> Temp= (ArrayList<Integer>) Indecies;
//								for(    Collection<String> instance  : entry.getValue().getinstances())
//								{    int  instance_token_cntr=0;  
//								for(String Token:instance)
//								{if(instance_token_cntr ==Temp.get(predicate_loop) )
//								{
//									IDBfact_parameters.add( Token );
//
//
//								}
//
//								instance_token_cntr++;
//								}
//								///if(  Indecies.toArray()[predicate_loop] ==  instance_token_cntr)
//
//
//								}
//							}
//							predicate_loop++; 
//							}
//						}
//					//	System.out.println("New values for IDB tuple  "+ HeadPred.getName()+" "+IDBfact_parameters); 
//						List<DatalogPredicate> new_tuple=new ArrayList<>() ;
//						DatalogPredicate IDBfact=new DatalogPredicate();
//						IDBfact.setName(HeadPred.getName());
//						IDBfact.setType(Type.IDBFACT);
//						IDBfact.setParameters(IDBfact_parameters);
//						new_tuple.add(IDBfact);
//						int exist = 0;
//						for(    Entry<Integer, List<DatalogPredicate>> check_exist:        factsList.entrySet()      )
//						{
//							for(   DatalogPredicate Pred:      check_exist.getValue()  )
//							
//								{
//								//if needs fix but it didnt work properly without this stp
//								//int contained= 0;
//								//if(IDBfact.getParameters().containsAll(Pred.getParameters()))
//								//	contained = 1;
//								if(Pred.getName().equals(IDBfact.getName())  &&   Pred.getType().equals(IDBfact.getType()) && Pred.getParameters().containsAll(IDBfact.getParameters()) )                        
//								{
//								 
//									
//								 exist = 1;
//							      break;	
//							}
//							}
//							
//						}
//						
//						
//						
//						if(exist==0 && ( IDBfact.getParametersCount() !=0  ))
//						{
//                        //  new_facts.add(new_tuple);
//						factsList.put(ruleNb++,new_tuple);
//						}
//						} 
//
//
//						}
//
//
//
//						/////////////////////////////END INFER
//						///TODO SET FIXED POINT
//
//
//
//
//
//
//
//
//					}//if compound rule (else part
//
//					//////////////////////////////////////////////////////
				}
				}
 //   System.out.println("LOOOOOOP  "+factlist_size);
    System.out.println("NUMBER OF FACTS NOW  "+factsList.size());
    
    for (Map.Entry<Integer, List<DatalogPredicate>> entry : factsList.entrySet()) {
		System.out.println("inside for loop");
	        System.out.println(entry.getKey()+" : "+entry.getValue());
	 }
    
    getTuples();

				////here while not fixed point
				//}while(factlist_size <6  );
         /*         for(List<DatalogPredicate>  New_IDB: new_facts )
                  {
                	  factsList.put(ruleNb++, New_IDB);
                	  
                  }
                  */
    
          //      }while( factsList.size()  !=factlist_size   );
				
          //    System.out.println("FIXED POINT REACHED ");//#"+iteration_counter+1 );
          //      System.out.println("NUMBER OF FACTS NOW  "+factsList.size());
             
          //      System.out.println(factsList.entrySet());
             //    }while(factsList.size() != factlist_size );
			//while
			}//end of if the fact list not empty



			else
			{
				System.out.print("No Facts ... No inferred tuples"); 
			}

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		} else {
			String errors = safetyErrorMsgs.stream().map(i -> i.toString()).collect(Collectors.joining("\n"));
			throw new ParseCancellationException(
					safetyErrorMsgs.size() + " violation(s) " + PrintColor.ERROR_MARK + "\n" + errors);
		}

		// TODO: print to file
		if ((boolean) userConfig.get("printToFile")) {
			Path path = Paths.get((String) userConfig.get("currentFolder") + " DatalogTree.txt");
			try (BufferedWriter writer = Files.newBufferedWriter(path)) {
				writer.write(ctx.toStringTree());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void checkSafetyRule(Entry<Integer, List<DatalogPredicate>> predicate) {
		int RuleNb = predicate.getKey();
		
		// TODO: this part must be optimized
		Collection<String> headParams = predicate.getValue().stream().filter(p -> p.getType().equals(Type.HEAD))
				.map(p -> p.getParameters()).flatMap(p -> p.stream()).collect(Collectors.toSet());

		Collection<String> bodyParams = predicate.getValue().stream().filter(p -> p.getType().equals(Type.BODY))
				.map(p -> p.getParameters()).flatMap(p -> p.stream()).collect(Collectors.toList());

		Collection<String> builtinParams = predicate.getValue().stream()
				.filter(p -> p.getType().equals(Type.BUILTIN)).map(p -> p.getParameters()).flatMap(p -> p.stream())
				.collect(Collectors.toList());

		// SAFETY RULE #1 : builtin predicate case: check if at least one parameter is bound
		boolean isBothBuiltinParamBound = true;
		if (builtinParams.size() > 0) {
			long nbBoundBuiltinParams = builtinParams.stream()
					.filter(p -> headParams.contains(p) || bodyParams.contains(p)).count();
			if (nbBoundBuiltinParams == 0) {
				isBothBuiltinParamBound = false;
				safetyErrorMsgs.add("Line " + paramsLinesNumber.get(RuleNb) + " | Rule #" + RuleNb
						+ " breaks the safety rules (Non-Bound Built-in Paramater(s): " + builtinParams + ")");
			}
		}

		// SAFETY RULE #2 : check if all builtin predicates are part of head & body
		if (isBothBuiltinParamBound) {
			Collection<String> builtinNonEqualityParams = predicate.getValue().stream()
					.filter(p -> p.getType().equals(Type.BUILTIN) && (!p.getOperator().equals(Operator.EQUAL)
							&& !p.getOperator().equals(Operator.NONEQUAL)))
					.map(p -> p.getParameters()).flatMap(p -> p.stream()).collect(Collectors.toList());

			if (!bodyParams.containsAll(builtinNonEqualityParams)
					&& !headParams.containsAll(builtinNonEqualityParams)) {
				Collection<String> missingParams = builtinNonEqualityParams.stream()
						.filter(n -> !headParams.contains(n) && !bodyParams.contains(n))
						.collect(Collectors.toSet());
				safetyErrorMsgs.add("Line " + paramsLinesNumber.get(RuleNb) + " | Rule #" + RuleNb
						+ " breaks the safety rules (Non-Bound Built-in Paramater(s): " + missingParams + ")");
			}
		}

		// SAFETY RULE #3 : check if all head predicates are part of body as well
		Collection<String> bodyParamsIncludingBuiltin = Stream.concat(bodyParams.stream(), builtinParams.stream())
				.collect(Collectors.toSet());
		if (!bodyParamsIncludingBuiltin.containsAll(headParams)) {
			Collection<String> missingParams = headParams.stream().filter(n -> !bodyParams.contains(n))
					.collect(Collectors.toSet());
			safetyErrorMsgs.add("Line " + paramsLinesNumber.get(RuleNb) + " | Rule #" + RuleNb
					+ " breaks the safety rules (Non-Bound Head Paramater(s): " + missingParams + ")");
		}

				//safety rule # 4::::	 EDB facts must be grounded
		
		ArrayList<String> demoforFactRule4 = new ArrayList<>();
		ArrayList<String> demoforfactParametrsRule4 = new ArrayList<>();
		HashMap<String, ArrayList<String>> demo1Rule4 = new HashMap<>();
		for (Entry<Integer, List<DatalogPredicate>> entry : predicatesList.entrySet()) {
			List<DatalogPredicate> value = entry.getValue();
			String key1 = value.get(0).getType().toString();
			String value1 = value.get(0).getParameters().toString();
			if(key1.equals("FACT")){
				demoforFactRule4.add(value1);
				demo1Rule4.put(key1, demoforFactRule4);
			}
		}
		//System.out.println("demo1Rule4: " + demo1Rule4);		
		
		for (Entry<String, ArrayList<String>> entry : demo1Rule4.entrySet()) {
			String str = entry.getValue().toString();			
			demoforfactParametrsRule4.add(str);
		}	
		//System.out.println("demoforfactParametrsRule4 : " + demoforfactParametrsRule4.toString());
		String str = demoforfactParametrsRule4.toString();
		//System.out.println("string value : " +str);
		char ch;
		//System.out.println("size : " + str.length());
		for(int i = 0; i<str.length();i++)
		{
			//System.out.println(i);
			ch =str.charAt(i);
			//System.out.println("character :" +ch);
			if(ch>=65 && ch<=90)
			{
				System.out.println("Illegal Character Found is :" + ch);
				System.out.println("=========");
				System.out.println("EDB predicates should be grounded");
				System.exit(0);
			}
		}
		
				//safety rule # 5::::	 EDB facts can only appear in body clauses
		
		
		ArrayList<String> demoforhead = new ArrayList<>();
		ArrayList<String> demoforfact = new ArrayList<>();
		HashMap<String, ArrayList<String>> demo1 = new HashMap<>();
		for (Entry<Integer, List<DatalogPredicate>> entry : predicatesList.entrySet()) {
			Integer key = entry.getKey();
			List<DatalogPredicate> value = entry.getValue();
		String key1 = value.get(0).getType().toString();
		String value1 = value.get(0).getName().toString();
		if(key1.equals("HEAD")){
			demoforhead.add(value1);
			demo1.put(key1, demoforhead);
		}else{
			demoforfact.add(value1);
			demo1.put(key1, demoforfact);
		}
		}

		int size1 = demo1.get("HEAD").size();
//		System.out.println(size1);
//		System.out.println(demo1);
//		System.out.println(demo1.get("FACT").size());
		int size2 = demo1.get("FACT").size();
		int iterator =0;
		String name = null;
		if(size1 > size2){
			iterator = size1;
			name = "HEAD";
		}else{
			iterator = size2;
			name = "FACT";
		}
		if(name.equals("HEAD")){
			for(int i1 =0; i1< iterator ; i1++){
				if(demo1.get("FACT").contains(demo1.get("HEAD").get(i1))){
					System.out.println("Fact predicate can't be present under HEAD ");
					System.exit(0);
				}else{
					continue;
				}
			}
		}else{
			for(int i1 =0; i1< iterator ; i1++){
				if(demo1.get("HEAD").contains(demo1.get("FACT").get(i1))){
					System.out.println("Fact predicate can't be present under HEAD ");
					System.exit(0);
				}else{
					continue;
				}
			}
		}	

	}

	public  Map<Integer, List<DatalogPredicate>> getTuples() {
		// TODO Auto-generated method stub
		
		int check =0;
		if (check==0)
		{
			
		}
		
		if(factsList.isEmpty())
		{
			System.out.println("****Hash Map empty****");
			
			
		}
		
		else
		{
		//System.out.println("inside function Hashmap not empty");
		
		/* for (Map.Entry<Integer, List<DatalogPredicate>> entry : factsList.entrySet()) {
				System.out.println("inside for loop");
			        System.out.println(entry.getKey()+" : "+entry.getValue());
			 }*/

		}
		return factsList;
	}

}
