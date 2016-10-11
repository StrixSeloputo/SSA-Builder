package ssa_builder_n_simple_cfg;

import java.util.LinkedHashSet;
import java.util.Set;

public class Const implements Expr {
	public Const() {}
	public Const(double val) {
		value = val;
	}
	public Const(Const c) {
		value = c.value;
	}
	
	public Const copy() {
		return new Const(this);
	}
	@Override 
	public String toString() {
		return ""+value;
	}
	@Override
	public Set<Var> vars() {
		return new LinkedHashSet<Var>();
	}
	@Override
	public boolean isPhi() {
		return false;
	}

	private double value = 0;
}
