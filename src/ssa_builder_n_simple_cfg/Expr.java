package ssa_builder_n_simple_cfg;

import java.util.Set;

public interface Expr {
	public Set<Var> vars();
	public boolean isPhi();
	public Expr copy();
}
