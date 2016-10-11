package ssa_builder_n_simple_cfg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class SSA_Builder {
	public CFG buildSSAform(CFG cfg) {
//		CFG ssa = cfg.copy();
		CFG ssa = cfg;
		cfg.initFull();
		System.out.println(ssa);
		
		phiFuncAllocation(ssa);
		ssa.vars().forEach(x -> renameVar(ssa, x));

		
		return ssa;
	}
	
	private void phiFuncAllocation(CFG ssa) {
		System.out.println("Starting SSA features initialization");
		
		System.out.print("[S-map]: Starting S-map initialization");
		Map<Var, Set<Vertex>> S = initS(ssa);
		System.out.println("S-map initialization done");
		System.out.println("[S-map]:\t");
		S.forEach((p, s) -> {
			System.out.print("\t["+p+"]:\t");
			s.forEach(v -> System.out.print(v.uid()+"  "));
			System.out.println();
		});
		System.out.println();
		
		System.out.print("[J-map]: Starting J-map initialization");
		Map<Var, Set<Vertex>> J = initJ(ssa, S);
		System.out.println("J-map initialization done");
		System.out.println("[J-map]:");
		J.forEach((k,v) -> {
			System.out.print("\t["+k+"]:\t");
			v.forEach(vert -> System.out.print(vert.uid()+"  "));
			System.out.println();
		});
				
		System.out.println("SSA features initialization done");
		
		
		System.out.println("Adding phi-function for all variables");
		Set<Var> vars = ssa.vars(); 
		vars.forEach(p -> {
			Set<Vertex> j = J.get(p);
			j.forEach(v -> {
				System.out.print(".");
				v.addPhiFunc(p);
				System.out.print(".");
			});
		});
		System.out.println("\nAll phi-function are added");
	}
	
	private Map<Var, Set<Vertex>> initS(CFG ssa) {
		Set<Var> vars = ssa.vars(); 
		Map<Var, Set<Vertex>> S = new HashMap<Var, Set<Vertex>>();
		vars.forEach(p -> {
			System.out.print(".");
			S.put(p, ssa.definitionVertex(p));
			System.out.print(".");
		});
		return S;
	}
	
	private Map<Var, Set<Vertex>> initJ(CFG ssa,Map<Var, Set<Vertex>> S) {
		Set<Var> vars = ssa.vars(); 		
		Map<Var, Set<Vertex>> J = new HashMap<Var, Set<Vertex>>();
		vars.forEach(p -> {
			System.out.print(".");
			J.put(p, ssa.getDFP_Set(S.get(p)));
			System.out.print(".");
		});
		return J;
	}
	
	public void renameVar(CFG ssa, Var p) {
		int counter = 0;
		Stack<Integer> stack = new Stack<Integer> ();
		stack.push(null);
		traverse(ssa.entry(), p, counter, stack);
	}
	
	private void traverse(Vertex v, Var p, int counter, Stack<Integer> stack)	{
		for(Statement s : v.stmts()) {
			if (!s.isPhi())
				for(Var p1 : s.rhs().vars()) // s.rhs Ц права€ часть, s.lhs Ц лева€ часть
					if (p1.equals(p)) 
						p1.newVersion(stack.peek()); 		//заменить p на pi, где i = stack.Top();
			if (s.lhs()!=null && s.lhs().equals(p)) {
				p.newVersion(counter);		//заменить p на pi, где i=counter;
				stack.push(counter);
				counter++;
			}
		} /* first loop */
		v.succ().forEach(w -> {
			int j = w.whichPred(v); /* пор€дковый номер v в массиве предшественников v1 */
			w.phis().forEach(phiStmt -> {
				PhiFunc phi = (PhiFunc)phiStmt.rhs();
				if (phiStmt.lhs()==p) {
					List<Var> args = phi.args();  
					Var[] params = args.toArray(new Var[args.size()]);

					if (stack.empty() || stack.size()<2)
						params[j].newVersion(-1);		//ветка, по которой нет определений дл€ ф-функции, но ф-функци€ стоит
					else
						params[j].newVersion(stack.peek());	//! заменить j-ый операнд p в phi.rhs на pi, где i=stack.top();
				}
			});
		}); /* second loop */
		for(Vertex w : v.children())
			traverse(w, p, counter, stack); /* рекурсивный вызов */
		v.stmts().forEach(stmt -> {
			if (stmt.lhs() == p)
				stack.pop();
		});
	}
}

class TestSSA {
	public static void main(String []args) {
		Vertex.initNull();
		System.out.println(Vertex.Null);
		CFG tstCFG1 = CFG.createTestCFG1();
		//tstCFG1.initFull();
		SSA_Builder ssaBuilder = new SSA_Builder();
		CFG ssa = ssaBuilder.buildSSAform(tstCFG1);
		System.out.println(ssa);
	}
}