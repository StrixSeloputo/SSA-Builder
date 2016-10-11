package ssa_builder_n_simple_cfg;

import java.util.ArrayList;
import java.util.List;

public class PhiFunc extends Func {
	public PhiFunc() {}
	public PhiFunc(Var p, int k) {
		var = p;
		for (int i = 0; i < k; i++) 
			params.add(new Var(p));	// p-copy in args;
	}
	public PhiFunc(PhiFunc pf) {
		pf.params.forEach(arg -> params.add(arg.copy()));
	}
	@Override
	public String toString() {
//		System.out.print(name+"_("+var+"*"+params.size()+"): ");
//		params.forEach(x -> System.out.print(x+"\t"));
//		System.out.println();
//		String argStr = "";
//		for (int i = 0; i < params.size()-1; i++) 
//			argStr += params.get(i).toString()+",";
//		argStr += params.get(params.size()-1);
//		return name+"("+argStr+")";
		return name+"_("+params+")";
	}
	@Override
	public boolean isPhi() {
		return true;
	}
//	@Override
	public List<Var> args() {
		return params;
	}
	public Var getPhiFuncVar() {
		return var;
	}
	protected final String name = "phi";
	public Var var = null;
	private List<Var> params = new ArrayList<Var>();
}
