package ssa_builder_n_simple_cfg;

public class CondOp extends BinOp {
	public CondOp() {}
	public CondOp(Expr lexpr, Expr rexpr, String symb) {
		lhs = lexpr; rhs = rexpr; symbol = symb;
	}
	@Override
	public String toString() {
		return lhs.toString()+symbol+rhs.toString()+suff;
	}
	
	private static final String suff = "?";
}
