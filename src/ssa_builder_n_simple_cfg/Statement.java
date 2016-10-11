package ssa_builder_n_simple_cfg;

import java.util.LinkedHashSet;
import java.util.Set;

public class Statement {
	public Statement() {}
	public Statement(Var var, Expr expr) {
		lhs = var; rhs = expr;
	}
	@Override
	public String toString() {
		return lhs+" <- "+rhs;
	}
	
	public Statement copy() {
		return new Statement(lhs==null ? null : new Var(lhs), rhs.copy());
	}
	public Expr rhs() {
		return rhs;
	}
	public Var lhs() {
		return lhs;
	}
	public Set<Var> vars() {
		Set<Var> res = new LinkedHashSet<Var>();
		if (lhs!=null)
			res.add(lhs);
		Set<Var> rhsVars = rhs.vars();
		res.addAll(rhsVars);
		return res;
	}
	public boolean isPhi() {
		return rhs.isPhi();
	}
	
	public Statement setLhs(Var l) {
		lhs = l;
		return this;
	}
	public Statement setRhs(Expr expr) {
		rhs = expr;
		return this;
	}
	
	private Var lhs;
	private Expr rhs;
}
