package ssa_builder_n_simple_cfg;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Vertex {
	public Vertex() {
		uid = count++;
	}
		
	@Override
	public String toString() {
		String res = "["+uid+"]:\t";
		for (Vertex v : succ)
			res += v.uid+"\t";
		res += "\n";
		for (Statement stmt : stmts) 
			res += stmt+"\n";
		return res + "=============================\n";
	}
	
	public static void newGraph() {
		count = 0;
		orderCount = 0;
	}
	public Vertex copy() {
		Vertex res = new Vertex();
		res.uid = uid;
		stmts.forEach(stmt -> res.stmts.add(stmt.copy()));
		//res.stmts.add()
		return res;
	}
	public Vertex join(Vertex v) {
		succ.add(v);
		return this;
	}
	public Vertex addStmt (Statement stmt) {
		stmts.add(stmt);
		return this;
	}
	public Vertex addPhiFunc(Var p) {
		PhiFunc pf = new PhiFunc(p, pred.size());
		stmts.add(0, new Statement(p, pf));
		return this;
	}
	
	public List<Statement> stmts() {
		return stmts;
	}
	public List<Var> vars() {
		List<Var> res = new ArrayList<Var>();
		stmts.forEach(stmt -> res.addAll(stmt.vars()));
		succ.forEach(v -> res.addAll(v.vars()));
		return res;
	}
	public List<Var> varDef() {
		List<Var> res = new ArrayList<Var>();
		stmts.forEach(stmt -> {
			if (stmt.lhs()!=null) res.add(stmt.lhs());
		});
		return res;
	}
	public List<Statement> phis() {
		List<Statement> r = new ArrayList<Statement>();
		stmts.forEach(s -> {
			if (s.isPhi())
				r.add(s);
		});		
		return r;
	}
	
	public int uid() {
		return uid;
	}
	public Vertex idom() {
//		if (idom.equals(this))
//			return Null;
		return idom;
	}
	public Set<Vertex> pred() {
		return pred;
	}
	public Set<Vertex> succ() {
		return succ;
	}
	public Set<Vertex> children() {
		return children;
	}
	public int whichPred(Vertex v) {
		int i = 0;
		for (Vertex w : pred) {
			if (v == w)
				return i;
			i++;
		}
		return -1;
	}
	public Vertex findIsemidom() {
		Vertex res = findIsemidom(this, order);
		isemidom = (res.order < order) ? res : Null;
		return isemidom;
	}
	public Vertex findIdom() {

//		System.out.println("\t["+uid()+"]:\t"+isemidom().uid());
		idom = findIdom(this, isemidom, isemidom);
//		System.out.println("\t["+uid()+"]:\t"+idom().uid()+"\t"+isemidom().uid());
		if (idom.uid==uid)
			idom = Null;
//		System.out.println("\t["+uid()+"]:\t"+idom().uid()+"\t"+isemidom().uid());
		return idom;
	}	
	
	public Set<Vertex> df() {
		return df;
	}
	public boolean isMarked() {
		return order > 0;
	}
	public int mark() {
		order = orderCount++;
		return order;
	}
	public Vertex isemidom() {
		return isemidom;
	}
	
	private Vertex findIdom(Vertex v, Vertex res, Vertex goal) {
		if (v.order > 0 && v!=goal) {
			if (v.order < res.order)
				res = v;
			for (Vertex w : v.pred) {
				Vertex wres = findIdom(w, res, goal);
				res = (wres==null || wres.order>=res.order) ? res : wres; 
			}
		}
		if (v.order<=0 && v!=goal)
			return Null;
		return res;
	}
	private static Vertex findIsemidom(Vertex v, int k) {
		Set <Vertex> rset = new LinkedHashSet<Vertex>();
		v.pred.forEach(w -> {
			if (w.order > k)
				rset.add(findIsemidom(w, k));
			else
				rset.add(w);
		});
		Vertex res = v;
		for (Vertex w : rset) {
			if (w.order < res.order)
				res = w;
		}
		return res;
	}
	
	public static Vertex initNull() {
		Null = new Vertex();
		count = 0;
		Null.uid = -1;
		Null.order = -1;
		Null.idom = Null;
		Null.isemidom = Null;
		return Null;
	}
	public static Vertex Null = null;
	private int order = -1;
	private static int count = -1;
	private static int orderCount = 0; 
	private int uid = -1;
	private Vertex idom = null;
	private Vertex isemidom = null;
	private Set<Vertex> succ = new LinkedHashSet<Vertex>();
	private Set<Vertex> pred = new LinkedHashSet<Vertex>();
	private Set<Vertex> children = new LinkedHashSet<Vertex>();
	private Set<Vertex> df = new LinkedHashSet<Vertex>();
	private List<Statement> stmts = new ArrayList<Statement>();
}
