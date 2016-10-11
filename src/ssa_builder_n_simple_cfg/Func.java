package ssa_builder_n_simple_cfg;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Func implements Expr {
	public Func() {}
	public Func(String n) {
		name = n;
	}
	public Func(Func f) {
		name = new String(f.name);
		f.args.forEach(arg -> args.add(arg.copy()));
	}
	
	public Func copy() {
		return new Func(this);
	}
	@Override
	public String toString() {
		String argStr = "";
		for (int i = 0; i < args.size()-1; i++) 
			argStr += args.get(i).toString()+",";
		argStr += args.get(args.size()-1);
		return name+"("+argStr+")";
	}
	@Override
	public boolean isPhi() {
		return false;
	}
	@Override
	public Set<Var> vars() {
		Set<Var> res = new LinkedHashSet<Var>();
		args.forEach(arg -> res.addAll(arg.vars()));
		return res;
	}
	
	public Func addArg(Expr arg) {
		args.add(arg);
		return this;
	}

	protected String name = "";
	private List<Expr> args = new ArrayList<Expr>();
}
