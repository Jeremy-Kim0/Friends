package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<String> path= new ArrayList<String>();
		if(g==null) {
			return null;
		}
		if(p1==null) {
			return null;
		}
		if(p2==null) {
			return null;
		}
		if(g.map.get(p1)==g.map.get(p2)) { //same word/location
			return null;
		}
		int num_Chain=g.members.length;

		boolean[] visited = new boolean[num_Chain];
		for(int x=0;x<num_Chain;x++) {
			visited[x]=false;
		}
		Person[] visitedP = new Person[num_Chain];
		Queue<Person> adj = new Queue<Person>();
		int ptr=g.map.get(p1);
		adj.enqueue(g.members[ptr]);
		visited[ptr]=true;
		visitedP[ptr]=g.members[ptr];
		while(!adj.isEmpty()) {
			Person curr=adj.dequeue();
			Friend neigh= curr.first;
			visited[g.map.get(curr.name)]=true;
			if(neigh==null) {
				return null;
			}
			while (neigh != null) {
				int index=neigh.fnum;
				if (visited[index] == false) {
					visitedP[index] = curr; 
					visited[index] = true;
					adj.enqueue(g.members[index]);	
					Person toA=g.members[index];
					if (toA.name.equals(p2)) {
						curr= g.members[index];	
					while (!(p1.equals(curr.name))) {
						path.add(0,curr.name);
						curr = visitedP[g.map.get(curr.name)];
					}
					path.add(0, p1);
					return path;
				}
			}
			neigh = neigh.next;
		}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		if(g==null || school==null) {
			return null;
		}

		ArrayList<ArrayList<String>> temp= new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		Person first= g.members[0];
		ArrayList<ArrayList<String>> result= BFS(g, temp, first, visited, school);
		if(result.isEmpty()) {
			return null;
		}
		for(int x=0; x<result.size();x++) {
			if(result.get(x).isEmpty()) {
				result.remove(x);
				x=0;
			}
		}
		return result;
		
	}
	private static ArrayList<ArrayList<String>> BFS(Graph g, ArrayList<ArrayList<String>> cliques, Person first, boolean[] visited, String school){
		
		Queue<Person> adj = new Queue<Person>();
		adj.enqueue(first);
		ArrayList<String> finalList = new ArrayList<String>();
		String name= first.name;
		if(visited[g.map.get(name)]==false) {
			visited[g.map.get(name)] = true;
		}
		
		if (first.school == null || !(school.equals(first.school))) {
			adj.dequeue();
			for (int x = 0; x < visited.length; x++) {
				if (visited[x] == false) {
					return BFS(g, cliques, g.members[x], visited, school);
				}
			}
		}
		Friend neigh;
		Person curr = new Person();
		
		while (adj.isEmpty() == false) {
			
			curr = adj.dequeue();
			neigh = curr.first;
			finalList.add(curr.name);

			while (neigh!= null) {
				int indexN=neigh.fnum;
				if(visited[indexN] == false) {
					if(g.members[indexN].school!=null) {
						if (g.members[indexN].school.equals(school)) {
							adj.enqueue(g.members[indexN]);
						}
					}
					visited[indexN] = true;
				}
				neigh=neigh.next;
			}
		}
		
		//This will take out the empty list at the end
		cliques.add(finalList);
		
		for (int x = 0; x < visited.length; x++) {
			if (visited[x] == false) {
				return BFS(g, cliques, g.members[x], visited, school);
			}
		}
		return cliques;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
public static ArrayList<String> connectors(Graph g) {
		
		if (g == null) {
			return null;
		}
		int num_Chain=g.members.length;
		
		ArrayList<String> connector = new ArrayList<String>();
		ArrayList<String> prev = new ArrayList<String>();

		boolean[] visited = new boolean[num_Chain];
		int[] numDFS= new int[num_Chain];
		
		int[] before = new int[num_Chain];
		int[] temp= new int[] {0,0};
		
		int counter=0;
		while(counter<g.members.length) {
			boolean val=visited[counter];
			if (!val) {
				connector = DFS(connector, g, g.members[counter], visited, temp, numDFS, before, prev, true);
			}
			counter++;
		}
		return connector;
	}

	private static ArrayList<String> DFS(ArrayList<String> connector, Graph g, Person toAdd, boolean[] visited, int[] count, int[] numDFS, int[] back, ArrayList<String> backward, boolean started){
		
		Friend neigh = toAdd.first;
		visited[g.map.get(toAdd.name)] = true;
				
		numDFS[g.map.get(toAdd.name)] = count[0];
		back[g.map.get(toAdd.name)] = count[1];
		
		while (neigh != null) {
			
			if (visited[neigh.fnum] == false) {
				
				int val1=count[0];
				count[0]=val1+1;
				int val2=count[1];
				count[1]=val2+1;				
				connector = DFS(connector, g, g.members[neigh.fnum], visited, count, numDFS, back, backward, false);
				String name=toAdd.name;
				if (numDFS[g.map.get(name)] <= back[neigh.fnum]) {
					boolean poss1= (!(connector.contains(name)) && backward.contains(name));
					boolean poss2= (!connector.contains(name) && started == false);
					if (poss1|poss2) {
						connector.add(0,name);
					}
				}
				else {
					int first = back[g.map.get(name)];
					int second = back[neigh.fnum];
					
					if (first > second) {
						back[g.map.get(toAdd.name)] = second;

					}
					else {
						back[g.map.get(toAdd.name)] = first;
					} 
				}		
			backward.add(name);
			}
			else {
				
				if (back[g.map.get(toAdd.name)] < numDFS[neigh.fnum]) {
					back[g.map.get(toAdd.name)] = back[g.map.get(toAdd.name)];
				}
				else {
					back[g.map.get(toAdd.name)] = numDFS[neigh.fnum];
				}
			}
			neigh = neigh.next;
		}
		return connector;
	}
	
}

