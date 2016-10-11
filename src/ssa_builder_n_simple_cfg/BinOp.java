package ssa_builder_n_simple_cfg;

import java.util.LinkedHashSet;
import java.util.Set;

public class BinOp implements Expr {
	public BinOp() {}
	public BinOp(Expr lexpr, Expr rexpr, String symb) {
		lhs = lexpr; rhs = rexpr; symbol = symb;
	}
	public BinOp(BinOp bo) {
		lhs = bo.lhs.copy(); rhs = bo.rhs.copy(); symbol = new String(bo.symbol);
	}
	
	public BinOp copy() {
		return new BinOp(this);
	}
	@Override
	public String toString() {
		return lhs.toString()+symbol+rhs.toString();
	}
	@Override
	public boolean isPhi() {
		return false;
	}
	@Override
	public Set<Var> vars() {
		Set<Var> res = new LinkedHashSet<Var>();
		res.addAll(lhs.vars());
		res.addAll(rhs.vars());
		return res;
	}

	protected String symbol;
	protected Expr lhs;
	protected Expr rhs;
}
