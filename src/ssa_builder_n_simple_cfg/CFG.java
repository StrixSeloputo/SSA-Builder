package ssa_builder_n_simple_cfg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CFG {
	public CFG() { Vertex.newGraph(); }
	@Override
	public String toString() {
		if (preorder.isEmpty() && entry != null)
			DFS(entry);
		String res = "=============CFG=============\n";
		for (Vertex v : preorder)
			res += ""+v;
		return res;
	}
	public CFG copy() {
		Vertex e = copyGraph(entry);
		CFG clone = new CFG();
		clone.entry = e;
		clone.initFull();
		return clone;
	}
	private Vertex copyGraph(Vertex v) {
		Vertex v1 = v.copy();
		v.succ().forEach(w -> {
			v1.succ().add(copyGraph(w));
		});
		return v1;
	}
	public Vertex entry() {
		return entry;
	}
	public void initFull() {
		System.out.println("Starting CFG features initialization");
		
		System.out.print("[DFS]: Starting DFS");
		DFS(entry);
		System.out.println("DFS done");
		System.out.print("[preorder]:\t");
		preorder.forEach(v -> System.out.print(v.uid()+"  "));
		System.out.println();
		System.out.print("[postorder]:\t");
		postorder.forEach(v -> System.out.print(v.uid()+"  "));
		System.out.println();
		
		System.out.print("[varDef]: Starting varDef initialization");
		initVarDef();
		System.out.println("varDef initialization done");
		System.out.println("[varDef]:");
		varDef.forEach((k,v) -> {
			System.out.print("\t["+k+"]:\t");
			v.forEach(vert -> System.out.print(vert.uid()+"  "));
			System.out.println();
		});
		
		System.out.println("[Succ]:");
		preorder.forEach(v -> {
			System.out.print("\t["+v.uid()+"]:\t");
			v.succ().forEach(vert -> System.out.print(vert.uid()+"  "));
			System.out.println();
		});
		System.out.println("[Pred]:");
		preorder.forEach(v -> {
			System.out.print("\t["+v.uid()+"]:\t");
			v.pred().forEach(vert -> System.out.print(vert.uid()+"  "));
			System.out.println();
		});

		System.out.print("[Doms]: Starting dominatotrs initialization");
		initDoms();
		System.out.println("Dominators initialization done");
		System.out.println("[Doms]:\t     (idom) (isemidom)");
		preorder.forEach(v -> {
			System.out.println("\t["+v.uid()+"]:\t"+v.idom().uid()+"\t"+v.isemidom().uid());
		});
		
		System.out.print("[DF]: Starting DF initialization");
		initDF();
		System.out.println("DF initialization done");
		System.out.println("[DF]:");
		preorder.forEach(v -> {
			System.out.print("\t["+v.uid()+"]:\t");
			v.df().forEach(vert -> System.out.print(vert.uid()+"  "));
			System.out.println();
		});
		
		System.out.print("[Children]: Starting Children initialization");
		initChildren();
		System.out.println("Children initialization done");
		System.out.println("[Children]:");
		preorder.forEach(v -> {
			System.out.print("\t["+v.uid()+"]:\t");
			v.children().forEach(vert -> System.out.print(vert.uid()+"  "));
			System.out.println();
		});
		
		System.out.println("CFG features initialization done");
	}
	
	private void initDF() {
        postorder.forEach(x -> {
			System.out.print(".");
            x.succ().forEach(y -> {
				System.out.print(".");
                if (!y.idom().equals(x))
                	x.df().add(y);
        		System.out.print(".");
            });
            x.children().forEach(z -> 
            	z.df().forEach(y -> {
            		System.out.print(".");
            		if (!y.idom().equals(x))
            			x.df().add(y); 
            		System.out.print(".");
            	})
            );
			System.out.print(".");
        });
    }
	private Set<Vertex> getDF_Set(Set<Vertex> S) {
        Set<Vertex> res = new HashSet<Vertex>();
        S.forEach(k -> k.df().forEach(res::add));
        return res;
	}
	public Set<Vertex> getDFP_Set(Set<Vertex> S) {
        Set<Vertex> res = new LinkedHashSet<Vertex>();
        boolean change = true;
        Set<Vertex> DFP = getDF_Set(S);
        do {
            Set<Vertex> tmpSet = new HashSet<Vertex>(S);
            tmpSet.addAll(DFP);
            DFP = getDF_Set(tmpSet);
            if (change = DFP.size()!=res.size())
                res = DFP;
        } while(change);
        return res;
	}
	private void initVarDef() {
		preorder.forEach(v -> {
			System.out.print(".");
			v.varDef().forEach(p -> {
				System.out.print(".");
				Set<Vertex> val = varDef.get(p);
				if (val == null)
					val = new LinkedHashSet<Vertex>();
				val.add(v);
				varDef.put(p,  val);
				System.out.print(".");
			});
			System.out.print(".");
		});
	}
	private void DFS(Vertex v) {
		System.out.print(".");
		if (!v.isMarked()) {
			preorder.add(v);
			v.mark(); 
			v.succ().forEach(w -> {
				w.pred().add(v);
				if (!w.isMarked())
					DFS(w);
			});
			postorder.add(v);
		}
		System.out.print(".");
	}	
	private void initDoms() {
		preorder.forEach(v -> {
			System.out.print(".");
			v.findIsemidom();
			System.out.print(".");
		});
		preorder.forEach(v -> {
			System.out.print(".");
			v.findIdom();
			System.out.print(".");
		});
	}
	public void initChildren() {
		preorder.forEach(v -> {
			System.out.print(".");
			v.idom().children().add(v);
			System.out.print(".");
		});
	}
	
	public Set<Var> vars() {
		if (vars==null)
			vars = vars(entry);
		return vars;
	}
	private static Set<Var> vars (Vertex v) {
		Set<Var> res = new LinkedHashSet<Var>();
		res.addAll(v.vars());
		v.succ().forEach(w -> res.addAll(vars(w)));
		return res;
	}
	public Set<Vertex> definitionVertex(Var p) {
		return varDef.get(p);
	}
	private Vertex entry;
	private Set<Vertex> preorder = new LinkedHashSet<Vertex>();
	private Set<Vertex> postorder = new LinkedHashSet<Vertex>();
	private Set<Var> vars;
	private Map<Var, Set<Vertex>> varDef = new HashMap<Var, Set<Vertex>>();
	
	public static CFG createTestCFG1() {
		CFG tstcfg = new CFG();
		Vertex []v = new Vertex [4];
		for (int i = 0; i < 4; i++) v[i] = new Vertex();
		v[0].join(v[1]).join(v[2]);			//	   ->0--1--3->
		v[1].join(v[3]);					//		  \   /
		v[2].join(v[3]); 					//		    2
		
		Var x = new Var("x");
		Var y = new Var("y");
		Var z = new Var("z");
		Var w = new Var("w");
//		System.out.println("=====Init test CFG=====");
		v[0].addStmt(new Statement(x, new Const(5)));
		v[0].addStmt(new Statement(x, new BinOp(x, new Const(3), "-")));
		v[0].addStmt(new Statement(null, new CondOp(x, new Const(3), "<")));	// "x<3?"
//		System.out.println(v[0]);
		v[1].addStmt(new Statement(y, new BinOp(x, new Const(2), "*")));
		v[1].addStmt(new Statement(w, y));
//		System.out.println(v[1]);
		v[2].addStmt(new Statement(y, new BinOp(x, new Const(3), "-")));
//		System.out.println(v[2]);
		v[3].addStmt(new Statement(w, new BinOp(x, y, "-")));
		v[3].addStmt(new Statement(z, new BinOp(x, y, "+")));
//		System.out.println(v[3]);
		tstcfg.entry = v[0];
		return tstcfg;
	}
}
