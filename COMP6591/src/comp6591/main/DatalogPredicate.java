package comp6591.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

enum Type {HEAD, BODY, BUILTIN, FACT,IDBFACT};
enum Operator {EQUAL, NONEQUAL, GREATER, GREATERSTRICT, LESS, LESSSTRICT};

public class DatalogPredicate {
    private static final AtomicInteger count = new AtomicInteger(0); // used for multithread safety
	private final int predicateId;
	public String name;
	private Type type;
	private Operator operator;
	private Collection<String> parameters;
	private HashMap<String, Operator> operators;
	
	public DatalogPredicate() {
		predicateId = count.incrementAndGet(); 
		_initOperators();
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operators.get(operator);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
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
	@Override
	public String toString() {
		return "DatalogPredicate [predicateId=" + predicateId + ", name=" + name + ", type=" + type + ", operator="
				+ operator + ", parameters=" + parameters + "]\n";
	}
	private void _initOperators() {
		operators = new HashMap<String, Operator>();
		operators.put("=", Operator.EQUAL);
		operators.put("<>", Operator.NONEQUAL);
		operators.put(">", Operator.GREATER);
		operators.put(">=", Operator.GREATERSTRICT);
		operators.put("<", Operator.LESS);
		operators.put("<=", Operator.LESSSTRICT);
	}
	
	
}
