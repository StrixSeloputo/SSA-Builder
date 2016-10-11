package ssa_builder_n_simple_cfg;

import java.util.LinkedHashSet;
import java.util.Set;

public class Var implements Expr, Comparable<Var> {
	public Var() {}
	public Var(String n) {
		name = n;
	}
	public Var(Var var) {
		name = var.name;
		version = var.version;
	}
	public Var copy() {
		return new Var(this);
	}
	@Override
	public String toString() {
		return name+(0>version ? "" : version);
	}
	@Override
	public int compareTo(Var o) {
		return name.compareTo(o.name);
	}
	@Override
	public boolean isPhi() {
		return false;
	}
	@Override
	public Set<Var> vars() {
		Set<Var> res = new LinkedHashSet<Var>();
		res.add(this);
		return res;
	}
	public String name() {
		return name;
	}
	public Var newVersion(int ver) {
		//return new Var(name, ver);
		version = ver;
		return this;
	}
	
//	private Var(String nm, int ver) {
//		name=nm; version=ver;
//	}
	private String name = "";
	private int version = -1;
}
