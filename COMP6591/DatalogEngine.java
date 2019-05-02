package comp6591.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import comp6591.main.DatalogExtensions;
import comp6591.main.DatalogPredicate;
import comp6591.main.DatalogPredicate.Type;

public class DatalogEngine {

	private HashMap<Integer, List<DatalogPredicate>> predicatesList;
	private HashMap<Integer, List<DatalogPredicate>> allPredicatesList;
	private int evaluationMethod;
	private static int ruleNb;
	HashMap<Integer, List<DatalogPredicate>> factsList;
	DatalogExtensions extensions;
	int semi_naive_flag = 0;

	public DatalogEngine(HashMap<Integer, List<DatalogPredicate>> predList, int evalMethod) {
		allPredicatesList = predList;
		predicatesList = filterPredicatesWithoutFacts(predList);
		evaluationMethod = evalMethod;
		extensions = new DatalogExtensions();
		ruleNb = 1;
		semi_naive_flag = evalMethod - 1;
	}

	public HashMap<Integer, List<DatalogPredicate>> filterPredicatesWithoutFacts(
			HashMap<Integer, List<DatalogPredicate>> predList) {
		predicatesList = new HashMap<Integer, List<DatalogPredicate>>();
		for (Entry<Integer, List<DatalogPredicate>> predicate : predList.entrySet()) {
			List<DatalogPredicate> predicates = predicate.getValue().stream()
					.filter(p -> !p.getType().equals(Type.FACT)).map(p -> p).collect(Collectors.toList());
			if (predicates.size() > 0) {
				predicatesList.put(predicate.getKey(), predicates);
				ruleNb++;
			}
		}

		// System.out.println("predicatesList " + predicatesList);
		return predicatesList;
	}

	public void evaluate() {

		// get only facts in EDB
		factsList = new HashMap<Integer, List<DatalogPredicate>>();
		for (Entry<Integer, List<DatalogPredicate>> predicate : allPredicatesList.entrySet()) {
			List<DatalogPredicate> predicates = predicate.getValue().stream().filter(p -> p.getType().equals(Type.FACT))
					.map(p -> predicate.getValue()).flatMap(p -> p.stream()).collect(Collectors.toList());
			if (predicates.size() > 0)
				factsList.put(predicate.getKey(), predicates);
			ruleNb++;

		}

		// System.out.println("factsList " + factsList);

		if (factsList.size() != 0)// if no facts in the program then no inference :)
		{

			HashMap<Integer, Collection<ArrayList<String>>> Semi_naive_Bookkeeping = new HashMap<>();

			int factlist_size = 0;
			do {
				factlist_size = factsList.size();
				// int iteration_counter = 0;
				Collection<List<DatalogPredicate>> new_facts = new ArrayList<>();
				// this will be used o know fixed point
				// factlist_size = factsList.size();

				// now check if the rule have instances for all body predicates

				for (Entry<Integer, List<DatalogPredicate>> RulesEntry : predicatesList.entrySet())// TODO :Double Check
																									// if any
																									// duplication
																									// elemenated
				// for each rule in the program
				{
					int fire_rule = 1;

					// shouldnt worry about arit ,bring all prediate names frm rule entry
					Collection<String> bodypredOnly = RulesEntry.getValue().stream()
							.filter(p -> p.getType().equals(Type.BODY)).map(p -> p.getName())
							.collect(Collectors.toSet());

					// take every single bod predicate name and check its availabiliy at facts list
					for (String BodyPred : bodypredOnly) {
						int instancecounter = 0;
						for (Entry<Integer, List<DatalogPredicate>> factPred : factsList.entrySet()) {
							// do we have any instance of thi pred name in facts list?
							Collection<String> PredicateInstance =

									factPred.getValue().stream().filter(p -> p.getName().equals(BodyPred))
											.map(p -> p.getParameters()).flatMap(p -> p.stream())
											.collect(Collectors.toList());

							// if list counter is greater than zero then we have instance
							if (PredicateInstance.size() > 0) {
								instancecounter++;
								break;
							} // FINDING ONE INSTANCE IS ENOUGH,if ou find it quit :)
							// System.out.println(BodyPred+" " +PredicateInstance);

						}
						if (instancecounter == 0)// if no instances after checking all fact list for som predicate then
													// dont fire this rule
						// no need to check the rest of the predicates
						{
							fire_rule = 0;
							break;
						} // if you finishing search and dont find an instances for a predicate ,skip and
							// dont coninue with the rule

					}

					if (fire_rule == 1) {
						// System.out.println(RulesEntry.getKey()+" will fire now");
						Map<Integer, DatalogLocalUnificationList> all_locally_unifiedList = new HashMap<Integer, DatalogLocalUnificationList>();
						int map_key = 0;
						int instance_Predicate_index = -1;
						///////////// LOCAL UNIFICATION
						// ge intnces +set indx for predicates
						// if an predicate has no instances ,skip inferene,defined here cz the whol rule
						///////////// will no need to proceed
						int need_global_unification = 1;
						for (DatalogPredicate Predicate : RulesEntry.getValue())// we guarntee that we have at least on
																				// predicate
						// in a rule body ,so this loop will always work
						{
							if (need_global_unification == 1) {
								if (Predicate.getType() == Type.BODY) // if a body predicate try to get instances
								{
									instance_Predicate_index++;
									int need_local_unification = 0;
									List<List<Integer>> allIndexes = new ArrayList<>();
									ArrayList<Collection<String>> Locally_unified_instances = new ArrayList<Collection<String>>();
									String Pediate_name = Predicate.getName();
									Collection<String> Predicat_parameters = Predicate.getParameters();
									Collection<String> Variables_to_unify = new ArrayList<String>();
									// check if need local unification or not
									// get instances
									Collection<String> PredicateInstance = new ArrayList<String>();
									if (Predicat_parameters.size() > 1)// it might need local unification
									{
										// check varables in predicate if it has any dupication stack it at
										// Variables_to_unify
										for (String Variable_name : Predicat_parameters) {
											// check frequency of var name in predicat parameters
											// String isexisting_var =
											long is_already_in_list = 0;
											if (!Variables_to_unify.isEmpty()) {
												is_already_in_list = IntStream.range(0, Variables_to_unify.size())
														.boxed().filter(z -> Variables_to_unify.toArray()[z]
																.equals(Variable_name))
														.count();
											}

											if (Collections.frequency(Predicat_parameters, Variable_name) > 1
													&& (is_already_in_list == 0)) {
												// get all indexes where this variable appears
												allIndexes
														.add(IntStream.range(0, Predicat_parameters.size()).boxed()
																.filter(x -> Predicat_parameters.toArray()[x]
																		.equals(Variable_name))
																.collect(Collectors.toList()));

												// output
												if (need_local_unification == 0)
													need_local_unification = 1;
												// System.out.println(indeces_to_unify);
												// should reconsider if Variables_to_unify needed or not??
												// add variables to list only if it needs unification
												Variables_to_unify.add(Variable_name);

											} // end of if( Collections.frequency(Predicat_parameters, Variable_name)>1
												// )

										} // end of for(String Variable_name: Predicat_parameters )
											// at this point i have indexes i need to unify
										if (need_local_unification == 1) {
											// System.out.print("indexes "+Pediate_name +" "+allIndexes);
											for (Entry<Integer, List<DatalogPredicate>> fact : factsList.entrySet())

											{
												PredicateInstance = fact.getValue().stream()
														.filter(p -> p.getName().equals(Pediate_name))
														.map(p -> p.getParameters()).flatMap(p -> p.stream())
														.collect(Collectors.toList());
												int is_locally_unified = 1;
												// if the fact is a predicate instance ,then predicateinstance is not
												// empty
												if (!PredicateInstance.isEmpty())// if the fact is an instance then
																					// unify it
												{
													for (List<Integer> indexList : allIndexes) {

														String First_value = PredicateInstance.toArray()[indexList
																.get(0)].toString();
														for (int loop_to_unify = 1; loop_to_unify < indexList
																.size(); loop_to_unify++) {
															if (!PredicateInstance.toArray()[indexList
																	.get(loop_to_unify)].equals(First_value))
															// in case loop finds on not unifiable variable exist the
															// whole processs
															{
																is_locally_unified = 0;
																break;
															}
														}
													}
													if (is_locally_unified == 1) {
														Locally_unified_instances.add(PredicateInstance);
													} else {
														// System.out.println("not locally unified "+PredicateInstance);

													}
												}

											}

											// Map<Integer, DatalogLocalUnificationList> all_locally_unifiedList = new
											// HashMap<Integer,
											// create DatalogLocalUnificationList object
											if (Locally_unified_instances.size() == 0)
												need_global_unification = 0;
											else {
												DatalogLocalUnificationList Predicate_with_instances = new DatalogLocalUnificationList();
												Predicate_with_instances.setName(Pediate_name);
												Predicate_with_instances.setpredicate_index(instance_Predicate_index);
												Predicate_with_instances.setRuleNB(RulesEntry.getKey());
												Predicate_with_instances.setParameters(Predicate.getParameters());
												Predicate_with_instances.setinstances(Locally_unified_instances);
												all_locally_unifiedList.put(map_key++, Predicate_with_instances);
												// System.out.println("locally unified "+Locally_unified_instances);
												// System.out.print("unification done here "+instance_Predicate_index+"
												// " +Pediate_name);

											}
										} // end of need local unification
										else {
											// System.out.print("no need to unify no multiple occurance var
											// "+instance_Predicate_index+" " +Pediate_name);

											for (Entry<Integer, List<DatalogPredicate>> fact1 : factsList.entrySet())

											{
												PredicateInstance = fact1.getValue().stream()
														.filter(p -> p.getName().equals(Pediate_name))
														.map(p -> p.getParameters()).flatMap(p -> p.stream())
														.collect(Collectors.toList());
												if (!PredicateInstance.isEmpty())// if the fact is an instance then
																					// unify it
												{
													// if we got an instance add it to array
													Locally_unified_instances.add(PredicateInstance);

												}
											}

											DatalogLocalUnificationList Predicate_with_instances = new DatalogLocalUnificationList();
											Predicate_with_instances.setName(Pediate_name);
											Predicate_with_instances.setRuleNB(RulesEntry.getKey());
											Predicate_with_instances.setParameters(Predicate.getParameters());
											Predicate_with_instances.setpredicate_index(instance_Predicate_index);
											Predicate_with_instances.setinstances(Locally_unified_instances);

											all_locally_unifiedList.put(map_key++, Predicate_with_instances);

											// System.out.println("all locallu ynnified "+
											// all_locally_unifiedList.values());
										}

									} else {
										// System.out.print("no need to unify param counter is 1
										// "+instance_Predicate_index +" "+Pediate_name);
										for (Entry<Integer, List<DatalogPredicate>> fact1 : factsList.entrySet())

										{
											PredicateInstance = fact1.getValue().stream()
													.filter(p -> p.getName().equals(Pediate_name))
													.map(p -> p.getParameters()).flatMap(p -> p.stream())
													.collect(Collectors.toList());
											if (!PredicateInstance.isEmpty())// if the fact is an instance then unify it
											{
												// if we got an instance add it to array
												Locally_unified_instances.add(PredicateInstance);

											}
										}

										DatalogLocalUnificationList Predicate_with_instances = new DatalogLocalUnificationList();
										Predicate_with_instances.setName(Pediate_name);
										Predicate_with_instances.setRuleNB(RulesEntry.getKey());
										Predicate_with_instances.setpredicate_index(instance_Predicate_index);
										Predicate_with_instances.setParameters(Predicate.getParameters());
										Predicate_with_instances.setinstances(Locally_unified_instances);
										all_locally_unifiedList.put(map_key++, Predicate_with_instances);
									}
								}
							}
						}
						// System.out.println(all_locally_unifiedList.entrySet());
						// global unification should start here
						// //if rule have one predicate ,we are done-> infer
						// ////////////////////////////////////////////////////
						//
						if (need_global_unification == 1) {
							int bod_cntr = 0;
							for (DatalogPredicate Check_rule_content : RulesEntry.getValue()) {
								if (Check_rule_content.getType() == Type.BODY)
									bod_cntr++;

							}

							if (bod_cntr == 1) {
								Collection<ArrayList<String>> simple_rule_thetas = new ArrayList<>();

								// go to rulenb and get head predicate and infer
								// get head name an inferred values
								// here i get the variale names fom head and check the first appearance index
								for (DatalogPredicate HeadPred : RulesEntry.getValue()) {
									if (HeadPred.getType() == Type.HEAD)// get head name and params name
									{
										Collection<Integer> Indecies = new ArrayList<Integer>();
										// HeadPred.getParameters() map vriable with its value in instance
										for (String Param : HeadPred.getParameters())// search each variable position
										{
											int found = 0;
											for (DatalogPredicate bodyPred : RulesEntry.getValue()) {
												if (bodyPred.getType() == Type.BODY)// get head name and params name
												{
													int index = 0;

													for (String bdyvar : bodyPred.getParameters()) {
														if (bdyvar.equals(Param)) {
															found = 1;
															break;
														}
														index++;
													}
													if (found == 1)// if variable exist in predicate
													{ // must add it to indecies filter }
														Indecies.add(index);

													}
												} // if(bodyPred.getType

											} // for(DatalogPredicate bodyPred:

										} // for(String Param: HeadPred
											// System.out.println("global indexes "+Indecies);
											// here get all instances to infer
											// loop all instances

										for (Entry<Integer, DatalogLocalUnificationList> entry : all_locally_unifiedList
												.entrySet()) {
											for (Collection<String> Instance : entry.getValue().getinstances()) {
												int found_in_archive = 0;
												// this is my theta so must stack it to sem naive bookkeeping
												if (semi_naive_flag == 1) {
													// check if instanc exists in the bookking
													// if yes skip it
													// if not exist
													try {

														for (ArrayList<String> old_thetas : Semi_naive_Bookkeeping
																.get(RulesEntry.getKey())) {
															int match_cntr = 0;
															for (int i = 0; i < old_thetas.size(); i++) {
																if (old_thetas.get(i).equals(Instance.toArray()[i]))
																	match_cntr++;
															}
															if (match_cntr == old_thetas.size()) {
																found_in_archive = 1;
																break;
															}
															if (found_in_archive == 1)
																break;
														}
														if (found_in_archive == 0)// brand new theta
														{
															ArrayList<String> temp = (ArrayList<String>) Instance;
															simple_rule_thetas.add(temp);
														}
													}

													catch (NullPointerException o) {
														found_in_archive = 0;
														ArrayList<String> temp = (ArrayList<String>) Instance;
														simple_rule_thetas.add(temp);

													}

													if (found_in_archive == 0)// now infer
													{

														List<DatalogPredicate> new_tuple = new ArrayList<>();
														DatalogPredicate IDBfact = new DatalogPredicate();
														IDBfact.setName(HeadPred.getName());
														IDBfact.setType(Type.IDBFACT);
														Collection<String> IDBfact_parameters = new ArrayList<>();
														for (int index : Indecies) {
															IDBfact_parameters
																	.add(Instance.toArray()[index].toString());
														}
														IDBfact.setParameters(IDBfact_parameters);
														// System.out.println("NEW tuple here for predicate
														// "+HeadPred.getName()+" "+IDBfact_parameters );
														// MUST CHECK NOT TO ADd DUPLICATE FACT HERE
														new_tuple.add(IDBfact);
														// check if new tuple is already exist
														int exist = 0;

														for (Entry<Integer, List<DatalogPredicate>> check_exist : factsList
																.entrySet()) {
															for (DatalogPredicate Pred : check_exist.getValue()) {
																// if needs fix but it didnt work properly without this
																// stp
																// int contained= 0;
																// if(IDBfact.getParameters().containsAll(Pred.getParameters()))
																// contained = 1;
																if (Pred.getName().equals(IDBfact.getName())) {
																	int index = 0;
																	int matched_counter = 0;
																	for (String Param_in_fact : Pred.getParameters()) {
																		if (IDBfact.getParameters().toArray()[index]
																				.equals(Param_in_fact)) {
																			matched_counter++;
																		}

																		index++;

																	}

																	if (matched_counter == IDBfact.getParameters()
																			.size()) {
																		exist = 1;
																	}

																	if (exist == 1)
																		break;
																}
															}
														}
														if (exist == 0 && (IDBfact.getParametersCount() != 0)) {
															// TO REPLACE THE ADD FACT WITH THIS

															// kamal - extensions part
															if (extensions.doesPredicateSatisfyBuiltin(RulesEntry,
																	new_tuple.get(0)))
																new_facts.add(new_tuple);
															// factsList.put(ruleNb++,new_tuple);
														}
													}

												} else// if naive
												{
													List<DatalogPredicate> new_tuple = new ArrayList<>();
													DatalogPredicate IDBfact = new DatalogPredicate();
													IDBfact.setName(HeadPred.getName());
													IDBfact.setType(Type.IDBFACT);
													Collection<String> IDBfact_parameters = new ArrayList<>();
													for (int index : Indecies) {
														IDBfact_parameters.add(Instance.toArray()[index].toString());
													}
													IDBfact.setParameters(IDBfact_parameters);
													// System.out.println("NEW tuple here for predicate
													// "+HeadPred.getName()+" "+IDBfact_parameters );
													// MUST CHECK NOT TO ADd DUPLICATE FACT HERE
													new_tuple.add(IDBfact);
													// check if new tuple is already exist
													int exist = 0;

													for (Entry<Integer, List<DatalogPredicate>> check_exist : factsList
															.entrySet()) {
														for (DatalogPredicate Pred : check_exist.getValue()) {
															// if needs fix but it didnt work properly without this stp
															// int contained= 0;
															// if(IDBfact.getParameters().containsAll(Pred.getParameters()))
															// contained = 1;
															if (Pred.getName().equals(IDBfact.getName())) {
																int index = 0;
																int matched_counter = 0;
																for (String Param_in_fact : Pred.getParameters()) {
																	if (IDBfact.getParameters().toArray()[index]
																			.equals(Param_in_fact)) {
																		matched_counter++;
																	}

																	index++;

																}

																if (matched_counter == IDBfact.getParameters().size()) {
																	exist = 1;
																}

																if (exist == 1)
																	break;
															}
														}
													}
													if (exist == 0 && (IDBfact.getParametersCount() != 0)) {
														// TO REPLACE THE ADD FACT WITH THIS

														// kamal - extensions part
														if (extensions.doesPredicateSatisfyBuiltin(RulesEntry,
																new_tuple.get(0)))
															new_facts.add(new_tuple);
														// factsList.put(ruleNb++,new_tuple);
													}
												} // end of instance

											} // for (Entry<Integer, DatalogLocalUnificationList>
										}

									}
								}
								//
								if (semi_naive_flag == 1 && !simple_rule_thetas.isEmpty())
									Semi_naive_Bookkeeping.put(RulesEntry.getKey(), simple_rule_thetas);
							}

							else// if compound rule
							{

								// do global unification before instantiation
								// need map of form key -> globaluniicationparam
								Map<Integer, DatalogGlobalUnificationParam> RuleGobalUnificaion = new HashMap<>();
								int[] Visit_log = new int[100];
								for (int LoopI = 0; LoopI < (RulesEntry.getValue().size() - 1); LoopI++)// TODO:remove
																										// built in pred
																										// from the loop
								{ // System.out.println(LoopI);

									Visit_log[LoopI] = 0;
								}
								int VISIT_INDEX = 0;
								for (DatalogPredicate bodyPred : RulesEntry.getValue()) {
									if (bodyPred.getType() == Type.BODY)// if the predicate is body then search each
																		// variable
									{
										for (String Predparameter : bodyPred.getParameters()) {// X for example
											Collection<String> Predicates = new ArrayList<>();
											Collection<Integer> index_in_predicte = new ArrayList<>();
											Collection<Integer> predicte_appearance = new ArrayList<>();
											int variables_added = 0;
											if (RuleGobalUnificaion.entrySet().size() != 0)// go ceck if it aready exist
											{
												for (Entry<Integer, DatalogGlobalUnificationParam> LOOP : RuleGobalUnificaion
														.entrySet()) {
													if (LOOP.getValue().getName().equals(Predparameter)) {
														variables_added = 1;
														break;
													}
												}
											}
											if (RuleGobalUnificaion.size() == 0 || variables_added == 0)// if need to
																										// add it to map
											// should search unvisited predicates only
											{
												int predicate_index = 0;

												for (DatalogPredicate searchindexpred : RulesEntry.getValue()) {
													if (searchindexpred.getType() == Type.BODY
															&& Visit_log[predicate_index] == 0)// if the predicate is
																								// body then search each
																								// variable
													{
														int j = 0;
														for (String Variable : searchindexpred.getParameters()) {
															if (Variable.equals(Predparameter)) {
																// if an occurrance is found YAY!
																// predicate name :
																// must get the name and declaration a lil bit out fr no
																// reptiion
																Predicates.add(searchindexpred.getName());
																index_in_predicte.add(j);
																predicte_appearance.add(predicate_index - 1);
																break;
															}
															j++;
														}
													}
													predicate_index++;
												}
												if (Predicates.size() > 1) {
													DatalogGlobalUnificationParam ParaObj = new DatalogGlobalUnificationParam();
													ParaObj.setName(Predparameter);
													ParaObj.setindex_in_predicte(index_in_predicte);
													ParaObj.setPredicates(Predicates);
													ParaObj.setpredicate_appearance(predicte_appearance);
													RuleGobalUnificaion.put(RuleGobalUnificaion.size(), ParaObj);
												}
												// System.out.println("New var "+ ParaObj.getName());
												// System.out.println(index_in_predicte);

											}
											// here should add to index map
										}
										Visit_log[VISIT_INDEX] = 1;
										VISIT_INDEX++;
									}
								}
								// System.out.print(RuleGobalUnificaion);
								/////////////////////////////// FINAL TEST
								/// what if nooneed to glbal unify?
								// then RuleGobalUnificaion should be empty
								if (RuleGobalUnificaion.size() != 0) {
									int max_appearance = -1;
									String max_param = "";
									for (Entry<Integer, DatalogGlobalUnificationParam> Variale_of_max : RuleGobalUnificaion
											.entrySet()) {
										if (Variale_of_max.getValue().get_visit_status() == 0) {
											if (Variale_of_max.getValue().getPredicates().size() > max_appearance) {
												max_appearance = Variale_of_max.getValue().getPredicates().size();
												max_param = Variale_of_max.getValue().getName();
											}
										}
									}

									///////////////////////////
									Map<Integer, DatalogLocalUnificationList> Filtered_Instances = new HashMap<Integer, DatalogLocalUnificationList>();
									do {
										// System.out.println(all_locally_unifiedList.entrySet());
										// max_param has the filter name
										// loop the variables list again and get this varible
										for (Entry<Integer, DatalogGlobalUnificationParam> Variale_to_unify : RuleGobalUnificaion
												.entrySet()) {
											if (Variale_to_unify.getValue().getName().equals(max_param))// if we are at
																										// filtr
																										// position
											{// loop all its predicate poitions
												int predicate_index = -1;
												ArrayList<Set<String>> set_all_varaible_values = new ArrayList<>();
												for (int pred_pod : Variale_to_unify.getValue()
														.getpredicte_appearance_index()) { // search that predicate from
																							// instances list
																							// pred_pos is the pos of
																							// the predicte am looking
																							// for
													predicate_index++;
													int loop = -1;
													for (Entry<Integer, DatalogLocalUnificationList> InstanceEntry : all_locally_unifiedList
															.entrySet()) {
														loop++;
														// get the pred am looking for

														if (pred_pod == InstanceEntry.getValue().getpredicate_index())// if
																														// i
																														// got
																														// the
																														// predicte
																														// am
																														// looking
																														// for
																														// in
																														// te
																														// in
														{
															// now i have all instances of the predicate and need to
															// take the vaue at the desirde index
															// InstanceEntry.getValue().getinstances()
															// need this index in all instances
															Set<String> all_possible_values = new HashSet<>();

															int value_to_map = Variale_to_unify.getValue()
																	.indexofar_inpredicate(predicate_index);

															for (Collection<String> getvalue : InstanceEntry.getValue()
																	.getinstances()) {
																int index = 0;

																// InstanceEntry.getValue().getpredicate_index();
																for (String val_in_instance : getvalue) {

																	if (index == value_to_map)
																		all_possible_values.add(val_in_instance);

																	index++;
																}

															}
															set_all_varaible_values.add(all_possible_values);

														}

													}

													// Collection<String> Temp =set_all_varaible_values.stream()
													// .distinct()
													// .collect(Collectors.toList());
													// set_all_varaible_values= Temp;

												}
												//

												// System.out.println("set of all values for variable
												// "+Variale_to_unify.getValue().getName() +" "+
												// set_all_varaible_values);

												Variale_to_unify.getValue().setis_fitered(1);// dont visit it again
												// now must do intersection between the set_all_varaible_values elements
												Set<String> intersected_values = new HashSet<>();
												Set<String> initial_set = set_all_varaible_values.get(0);
												int Counter = 0;
												for (String val : initial_set) {// search appearance of each element
													Counter = 0;
													for (Set<String> Search_sets : set_all_varaible_values) {
														for (String search_Word : Search_sets) {
															if (search_Word.equals(val))
																Counter++;
														}

													}
													if (Counter > 1)
														intersected_values.add(val);
												}

												// System.out.println("intersectin "+intersected_values);
												// now filter the instances of the rule
												////////////////////////// FILTER

												int filter_predicate_index = -1;
												ArrayList<Collection<String>> Filtered_instances = new ArrayList<>();
												for (int pred_pod : Variale_to_unify.getValue()
														.getpredicte_appearance_index()) { // search that predicate from
																							// instances list
																							// pred_pos is the pos of
																							// the predicte am looking
																							// for
													filter_predicate_index++;
													int i_loop = -1;
													for (Entry<Integer, DatalogLocalUnificationList> InstanceEntry : all_locally_unifiedList
															.entrySet()) {
														i_loop++;
														// get the pred am looking for

														if (pred_pod == InstanceEntry.getValue().getpredicate_index())// if
																														// i
																														// got
																														// the
																														// predicte
																														// am
																														// looking
																														// for
																														// in
																														// te
																														// in
														{
															// now i have all instances of the predicate and need to
															// take the vaue at the desirde index
															// InstanceEntry.getValue().getinstances()
															// need this index in all instances
															ArrayList<Collection<String>> instances = new ArrayList<>();

															int value_to_map = Variale_to_unify.getValue()
																	.indexofar_inpredicate(filter_predicate_index);

															for (Collection<String> getvalue : InstanceEntry.getValue()
																	.getinstances()) {
																int index = 0;
																// InstanceEntry.getValue().getpredicate_index();
																for (String val_in_instance : getvalue) {

																	if (index == value_to_map) {
																		// value here is in intersection list
																		for (String Serch_word : intersected_values) {
																			if (Serch_word.equals(val_in_instance)) {// WE
																														// HAVE
																														// AN
																														// INSTANCE
																														// HERE!!
																				instances.add(getvalue);
																			}
																		}

																	}
																	// add instances list to the map

																	index++;
																}

															}
															// Filtered_instances.add( all_possible_values);
															// Map<Integer, DatalogLocalUnificationList>
															// Filtered_Instances = new
															// HashMap<Integer,DatalogLocalUnificationList>();
															// DatalogLocalUnificationList New_instance = new
															// DatalogLocalUnificationList();
															// New_instance.setinstances(instances);
															// New_instance.setRuleNB(InstanceEntry.getValue().getRuleNB());
															// New_instance.setParameters(InstanceEntry.getValue().getParameters());
															// New_instance.setName(InstanceEntry.getValue().getName());
															// Filtered_Instances.put(Filtered_Instances.size(),
															// New_instance) ;
															// change instances list
															InstanceEntry.getValue().setinstances(instances);
														}

													}

													// Collection<String> Temp =set_all_varaible_values.stream()
													// .distinct()
													// .collect(Collectors.toList());
													// set_all_varaible_values= Temp;
												}
												// System.out.println("filtered instances "+all_locally_unifiedList);
												//

												// System.out.println("set of all values for variable
												// "+Variale_to_unify.getValue().getName() +" "+
												// set_all_varaible_values);
											} // if is max param

											// now im done with this var. gt next max
											max_appearance = -1;

											for (Entry<Integer, DatalogGlobalUnificationParam> Variale_of_max : RuleGobalUnificaion
													.entrySet()) {
												if (Variale_of_max.getValue().get_visit_status() == 0) {
													if (Variale_of_max.getValue().getPredicates()
															.size() > max_appearance) {
														max_appearance = Variale_of_max.getValue().getPredicates()
																.size();
														max_param = Variale_of_max.getValue().getName();
													}
												}
											}
											// now i have next univisited
										}
									} while (max_appearance != -1);

									////////////////////////// INFER NOW

								}

								// now infer
								ArrayList<String> Variable_in_Rule = new ArrayList<>();
								for (Entry<Integer, DatalogLocalUnificationList> all_rule_predicates : all_locally_unifiedList
										.entrySet()) {
									for (String Parameter : all_rule_predicates.getValue().getParameters()) {
										if (Variable_in_Rule.size() != 0) {
											if (!Variable_in_Rule.contains(Parameter)) {
												Variable_in_Rule.add(Parameter);
											}
										} else {
											Variable_in_Rule.add(Parameter);
										}

									}

								}
								// System.out.println(Variable_in_Rule);
								// now i have all variable names in the list Variable in rule

								Collection<ArrayList<String>> all_theta = new ArrayList<>();

								for (Entry<Integer, DatalogLocalUnificationList> first_predicate : all_locally_unifiedList
										.entrySet()) {
									int instance_loop = 0;
									for (Collection<String> Instance : first_predicate.getValue().getinstances()) {// for
																													// each
																													// instance
																													// in
																													// the
																													// first
																													// predicit
										Collection<ArrayList<String>> instance_theta = new ArrayList<>();
										ArrayList<String> intial_assumption = new ArrayList<>();
										int index_to_put_value = 0;
										for (String Var_name : Variable_in_Rule) {
											if (first_predicate.getValue().getParameters().contains(Var_name))// if
																												// variable
																												// occurs
																												// in
																												// predicate
																												// assign
											{
												intial_assumption.add(index_to_put_value, first_predicate.getValue()
														.get_first_occurrance_in_instance(instance_loop, Var_name));

											}

											index_to_put_value++;

										}
										// now i have the substitution assumptions from the first predicate

										for (Entry<Integer, DatalogLocalUnificationList> other_predicates : all_locally_unifiedList
												.entrySet()) {
											if (first_predicate.getValue().getpredicate_index() != other_predicates
													.getValue().getpredicate_index()) {
												Collection<ArrayList<String>> predicate_assumptions = new ArrayList<>();

												// no start stacking compatible indexes in order to infer at the end
												int other_instance_counter = 0;
												for (Collection<String> instances_of_other_predicit : other_predicates
														.getValue().getinstances()) {// now try to map every instance in
																						// every other predicit with the
																						// instance of the first
																						// predicit
													if (instance_theta.isEmpty()) {
														ArrayList<String> New_tuple_assumption = new ArrayList<>(
																intial_assumption); // for each instance store the
																					// promising theta her
														// initialize it with instance initial assumption

														int new_inference_fails = 0;
														int index_of_other_predicate = 0;// index of variable like XYZ
														for (String other_pred_Var_name : Variable_in_Rule) {
															if (other_predicates.getValue().getParameters()
																	.contains(other_pred_Var_name))// if variable occurs
																									// in predicate
																									// assign
															{
																// check if am changing any assumption then quit
																try {
																	New_tuple_assumption.get(index_of_other_predicate);
																	if (!New_tuple_assumption
																			.get(index_of_other_predicate)
																			.equals(other_predicates.getValue()
																					.get_first_occurrance_in_instance(
																							other_instance_counter,
																							other_pred_Var_name))) {
																		new_inference_fails = 1;
																		break;
																	}
																} catch (IndexOutOfBoundsException e) {
																	New_tuple_assumption.add(index_of_other_predicate,
																			other_predicates.getValue()
																					.get_first_occurrance_in_instance(
																							other_instance_counter,
																							other_pred_Var_name));
																}

															}
															index_of_other_predicate++;
															if (new_inference_fails == 1)
																break;

														}

														// other_instance_counter++;
														if (new_inference_fails == 0) {// if the substitution is still
																						// correct
															if (!predicate_assumptions.contains(New_tuple_assumption))
																predicate_assumptions.add(New_tuple_assumption);

														}

													} // if theta is empty
													else// if i have successful assumption just continue with it
													{
														for (Collection<String> Assumption : instance_theta) {

															ArrayList<String> New_tuple_assumption = new ArrayList<>(
																	Assumption); // for each instance store the
																					// promising theta her
															// initialize it with instance initial assumption

															int new_inference_fails = 0;
															int index_of_other_predicate = 0;// index of variable like
																								// XYZ
															for (String other_pred_Var_name : Variable_in_Rule) {
																if (other_predicates.getValue().getParameters()
																		.contains(other_pred_Var_name))// if variable
																										// occurs in
																										// predicate
																										// assign
																{
																	// check if am changing any assumption then quit
																	try {
																		New_tuple_assumption
																				.get(index_of_other_predicate);
																		if (!New_tuple_assumption
																				.get(index_of_other_predicate)
																				.equals(other_predicates.getValue()
																						.get_first_occurrance_in_instance(
																								other_instance_counter,
																								other_pred_Var_name))) {
																			new_inference_fails = 1;
																			break;
																		}
																	} catch (IndexOutOfBoundsException e) {
																		New_tuple_assumption.add(
																				index_of_other_predicate,
																				other_predicates.getValue()
																						.get_first_occurrance_in_instance(
																								other_instance_counter,
																								other_pred_Var_name));
																	}

																}
																index_of_other_predicate++;
																if (new_inference_fails == 1)
																	break;

															}

															if (new_inference_fails == 0) {// if the substitution is
																							// still correct
																if (!predicate_assumptions
																		.contains(New_tuple_assumption))
																	predicate_assumptions.add(New_tuple_assumption);

															}

														}

													}

													other_instance_counter++;
												}
												instance_theta.clear();
												instance_theta.addAll(predicate_assumptions);

											}

										}
										// when explore this instance with all possibilities of all other predicates
										if (!instance_theta.isEmpty()) {
											// if(semi_naive_flag==0)
											if (semi_naive_flag == 0)
												all_theta.addAll(instance_theta);
											else {
												// if semi naive
												for (ArrayList<String> instance : instance_theta) {// check do we have
																									// this theta
																									// already in
																									// bookkeeping?
																									//////////////////

													int found_in_archive = 0;
													// this is my theta so must stack it to sem naive bookkeeping

													// check if instanc exists in the bookking
													// if yes skip it
													// if not exist
													try {

														for (ArrayList<String> old_thetas : Semi_naive_Bookkeeping
																.get(RulesEntry.getKey())) {
															int match_cntr = 0;
															for (int i = 0; i < old_thetas.size(); i++) {
																if (old_thetas.get(i).equals(instance.toArray()[i]))
																	match_cntr++;
															}
															if (match_cntr == old_thetas.size()) {
																found_in_archive = 1;
																break;
															}
															if (found_in_archive == 1)
																break;
														}
														if (found_in_archive == 0)// brand new theta
														{
															ArrayList<String> temp = (ArrayList<String>) instance;
															all_theta.add(temp);
														}
													}

													catch (NullPointerException o) {
														found_in_archive = 0;
														ArrayList<String> temp = (ArrayList<String>) instance;
														all_theta.add(temp);

													}

													///////////////
												}

											}
											// else
											// {
											// for(ArrayList<String> one_theta_instance:instance_theta)
											// {//check if instance is already in the semi naive stack
											// try
											// {
											//
											// }
											// catch(IndexOutOfBoundsException)
											//
											// }
											//
											// }
										}
										instance_loop++;
									}

									// do this for first predicit only
									break;

								}
								// System.out.println(all_theta);

								// here i have all_theta ready for this rule
								if (semi_naive_flag == 1) {
									try {// adding a new theta list to a sertain rule number
										Semi_naive_Bookkeeping.get(RulesEntry.getKey());
										// if no error then we have this rule number already just add the theta list to
										if (!all_theta.isEmpty())
											Semi_naive_Bookkeeping.get(RulesEntry.getKey()).addAll(all_theta);

									}

									catch (NullPointerException o) {
										if (!all_theta.isEmpty())
											Semi_naive_Bookkeeping.put(RulesEntry.getKey(), all_theta);
									}

								}
								// INFER
								// System.out.println("this is the values in all book keeping after all is done
								// ");
//								System.out.println(Semi_naive_Bookkeeping);
								DatalogPredicate Head_predicate = new DatalogPredicate();
								Collection<ArrayList<String>> Inferred_vectors = new ArrayList<>();
								for (DatalogPredicate HeadPred : RulesEntry.getValue())// go to head parameter
								{
									if (HeadPred.getType() == Type.HEAD)// get head name and params name
									{
										Head_predicate = HeadPred;
										for (String head_param : HeadPred.getParameters()) {
											for (int loop_rule_variable = 0; loop_rule_variable < Variable_in_Rule
													.size(); loop_rule_variable++) {
												if (head_param.equals(Variable_in_Rule.get(loop_rule_variable))) {
													ArrayList<String> Var_values = new ArrayList<>();
													for (ArrayList<String> theta : all_theta) {
														// go get all values at the loop_rule_variable index from every
														// theta and append it to inferred verctors
														Var_values.add(theta.get(loop_rule_variable));

													}
													// here must append to inferred vectors
													Inferred_vectors.add(Var_values);
												}

											}

										}
									}

								}

								// System.out.println("New vectors "+Inferred_vectors);

								for (ArrayList<String> fact_loop : Inferred_vectors)// this is just to get the size for
																					// iteration must be replaced
								{
									for (int loop_cntr = 0; loop_cntr < fact_loop.size(); loop_cntr++) {
										DatalogPredicate New_IDB = new DatalogPredicate();
										Collection<String> Parameters = new ArrayList<>();
										for (ArrayList<String> new_fact : Inferred_vectors) {
											Parameters.add(new_fact.get(loop_cntr));

										}
										New_IDB.setName(Head_predicate.getName());
										New_IDB.setType(Type.IDBFACT);
										New_IDB.setParameters(Parameters);
										// the new tuple is ready add it to new_facts_list
										List<DatalogPredicate> New_tuple = new ArrayList<>();
										New_tuple.add(New_IDB);
										// must check no redunduncy dont add facts we already have!

										int exist = 0;
										for (Entry<Integer, List<DatalogPredicate>> check_exist : factsList
												.entrySet()) {
											for (DatalogPredicate Pred : check_exist.getValue()) {
												// if needs fix but it didnt work properly without this stp
												// int contained= 0;
												// if(IDBfact.getParameters().containsAll(Pred.getParameters()))
												// contained = 1;
												if (Pred.getName().equals(New_IDB.getName())) {
													int index = 0;
													int matched_counter = 0;
													for (String Param_in_fact : Pred.getParameters()) {
														if (New_IDB.getParameters().toArray()[index]
																.equals(Param_in_fact)) {
															matched_counter++;
														}

														index++;

													}

													if (matched_counter == New_IDB.getParameters().size()) {
														exist = 1;
													}

													if (exist == 1)
														break;
												}
											}
										}
										if (exist == 0 && (New_IDB.getParametersCount() != 0)) {
											// TO REPLACE THE ADD FACT WITH THIS
											// kamal - extensions part
											if (extensions.doesPredicateSatisfyBuiltin(RulesEntry, New_tuple.get(0)))
												new_facts.add(New_tuple);
											// factsList.put(ruleNb++,new_tuple);
										}

										// new_facts.add(New_tuple);
									}

									break;
								}
							}

							// for(DatalogPredicate HeadPred:RulesEntry.getValue())//go to head parameter
							// {if(HeadPred.getType()== Type.HEAD)//get head name and params name
							// { Collection<Integer> Indecies = new ArrayList<Integer>();
							// Collection<Integer> Predicate_index = new ArrayList<Integer>();
							// // HeadPred.getParameters() map vriable with its value in instance
							// int body_predicate_index=0;
							// for(String Param: HeadPred.getParameters())//search each variable position
							// { int found =0;
							// for(DatalogPredicate bodyPred:RulesEntry.getValue())
							// {if(bodyPred.getType()== Type.BODY)//get head name and params name
							// { int index=0;
							//
							// for(String bdyvar: bodyPred.getParameters())
							// {
							// if(bdyvar.equals(Param))
							// {found =1 ;
							// break;}
							// index++;
							// }
							// if (found == 1)//if variable exist in predicate
							// { //must add it to indecies filter }
							// Indecies.add(index);
							// Predicate_index.add(body_predicate_index);
							// }
							// } //if(bodyPred.getType
							// if(found ==1 ) break;
							// }// for(DatalogPredicate bodyPred:
							// body_predicate_index++;
							// }
							// //System.out.println("LAAAAAAAAST "+Indecies);
							// //System.out.println("IN predicates "+Predicate_index);
							// Collection<String> IDBfact_parameters= new ArrayList<>();
							// for (Entry<Integer, DatalogLocalUnificationList> entry :
							// all_locally_unifiedList.entrySet())
							// {
							// int predicate_loop=0;
							// for(int P_index:Predicate_index)
							// { int target_inex=entry.getValue().getpredicate_index();
							// if(P_index ==target_inex )//if the predicate is found
							// {//now loop the instances
							// ArrayList<Integer> Temp= (ArrayList<Integer>) Indecies;
							// for( Collection<String> instance : entry.getValue().getinstances())
							// { int instance_token_cntr=0;
							// for(String Token:instance)
							// {if(instance_token_cntr ==Temp.get(predicate_loop) )
							// {
							// IDBfact_parameters.add( Token );
							// break;
							//
							// }
							//
							// instance_token_cntr++;
							// }
							// ///if( Indecies.toArray()[predicate_loop] == instance_token_cntr)
							//
							//
							// }
							// }
							// predicate_loop++;
							// }
							// }
							// // System.out.println("New values for IDB tuple "+ HeadPred.getName()+"
							// "+IDBfact_parameters);
							// List<DatalogPredicate> new_tuple=new ArrayList<>() ;
							// DatalogPredicate IDBfact=new DatalogPredicate();
							// IDBfact.setName(HeadPred.getName());
							// IDBfact.setType(Type.IDBFACT);
							// IDBfact.setParameters(IDBfact_parameters);
							// new_tuple.add(IDBfact);
							// int exist = 0;
							// for( Entry<Integer, List<DatalogPredicate>> check_exist: factsList.entrySet()
							// )
							// {
							// for( DatalogPredicate Pred: check_exist.getValue() )
							// {
							// //if needs fix but it didnt work properly without this stp
							// //int contained= 0;
							// //if(IDBfact.getParameters().containsAll(Pred.getParameters()))
							// // contained = 1;
							// if(Pred.getName().equals(IDBfact.getName()) &&
							// Pred.getType().equals(IDBfact.getType()) &&
							// Pred.getParameters().containsAll(IDBfact.getParameters()) )
							// {
							//
							// exist = 1;
							// break;
							// }
							// }
							// }
							// if(exist==0 && ( IDBfact.getParametersCount() !=0 ))
							// {
							// new_facts.add(new_tuple);
							// factsList.put(ruleNb++,new_tuple);
							// }
							// }

							// }

							///////////////////////////// END INFER
							/// TODO SET FIXED POINT

						}

						else// if dont need global nification
						{

							// System.out.println("here some predicate in this rule has zero instances ,,no
							// need to proceed to inference");

						}
						// System.out.println("var and indexes map ");
						// System.out.println(RuleGobalUnificaion.entrySet());
						// get max apearance var
						// to choose nxt flter
						//
						//
						//
						//
						//
						// }//if compound rule (else part
						//
						// //////////////////////////////////////////////////////
					}
				}
				// System.out.println("LOOOOOOP "+factlist_size);
				// System.out.println("NUMBER OF FACTS NOW "+factsList.size());

				// System.out.println("NUMBER OF new FACTS NOW "+new_facts.size());
				// System.out.println(Semi_naive_Bookkeeping);
				// now add new facts to factslist
				for (List<DatalogPredicate> extra_fact : new_facts)
				{   int duplicate = 0;
					for (Entry<Integer, List<DatalogPredicate>> existing_facts : factsList.entrySet())
					{
						DatalogPredicate existing_fact_object= existing_facts.getValue().get(0);
						if(extra_fact.get(0).getName().equals(existing_fact_object.getName()))//if we have an existing tuple for the same predicate then check if parameters are equal
						{  int matches_counter =0;
						    int index=0;
						for( String Existing_parameter:existing_fact_object.getParameters()     )
							{
							if (extra_fact.get(0).getParameters().toArray()[index]
									.equals(Existing_parameter))
							{
								matches_counter++;
							}	  
							index++;	
							}
						if(matches_counter == existing_fact_object.getParameters().size())
						{
							duplicate = 1;
							break;
						}
							
						}
						
						
					}
					if(duplicate == 0)
					{
					factsList.put(ruleNb++, extra_fact);
					}
				}

				// System.out.println(factsList);
				// factlist_size++;
				// if(factlist_size==2)
				// System.out.println("hi");
			} while (factlist_size != factsList.size());

			/*
			 * for(List<DatalogPredicate> New_IDB: new_facts ) { factsList.put(ruleNb++,
			 * New_IDB);
			 * 
			 * }
			 */

			// }while( factsList.size() !=factlist_size );
			// System.out.println("FIXED POINT REACHED ");//#"+iteration_counter+1 );
			// System.out.println("NUMBER OF FACTS NOW "+factsList.size());

			// System.out.println(factsList.entrySet());
			// }while(factsList.size() != factlist_size );
			// while
		} // end of if the fact list not empty

		else {
			System.out.print("No Facts ... No inferred tuples");
		}
	}

	void reset_fact_list() {

		for (Entry<Integer, List<DatalogPredicate>> fact : factsList.entrySet()) {
			if (fact.getValue().get(0).getType().equals(Type.IDBFACT))
				fact.getValue().get(0).Set_new_tuple(0);

		}
	}

	public HashMap<Integer, List<DatalogPredicate>> getTuples() {
		System.out.println("\nEDB + IDB Facts");
		return factsList;
	}
}
