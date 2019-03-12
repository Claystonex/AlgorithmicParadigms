//**********************************************************
//**********************************************************

// package pathfinder.informed;

import java.util.*;

/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {
    /**
     * Returns list of steps to take from first input state to second input state
     * @param MazeState representing state where to navigate from
     * @param MazeState representing state needed to navigate to
     * @param MazeProblem representing maze to search for state in
     * @return ArrayList<String> path leading to input state
     */
    public static ArrayList<String> goFromTo(MazeState startState, MazeState endState, MazeProblem problem) {
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<>(5 , new SearchTreeNodeComparator());
        ArrayList<MazeState> frontierStates = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        Map<String, MazeState> transitions;
        ArrayList<MazeState> visitedStates = new ArrayList<>();
        SearchTreeNode currentNode;
        boolean lookingForGoal = problem.isGoal(endState);

        int heuristic = (lookingForGoal) ? problem.distanceToGoal( startState ) : problem.distance( startState , endState );
        frontier.add( new SearchTreeNode( startState, null, null, heuristic) );
        frontierStates.add( frontier.peek().state );

        int i = 0;
        while (true) {

            currentNode = frontier.poll();
            visitedStates.add(currentNode.state);

            boolean finished = (lookingForGoal) ? problem.isGoal(currentNode.state) : currentNode.state.equals(endState);
            if ( finished ) {
                return buildPath(currentNode);
            }

            transitions = problem.getTransitions(currentNode.state);
            for (Map.Entry<String, MazeState> action : transitions.entrySet()) {
                heuristic = (lookingForGoal) ? problem.costFromInitial(action.getValue()) + problem.distanceToGoal(action.getValue()) : problem.costFromInitial(action.getValue()) + problem.distance( action.getValue() , endState );
                SearchTreeNode newNode = new SearchTreeNode(action.getValue(), action.getKey(), currentNode, heuristic);
                if ( !visitedStates.contains(newNode.state) && !frontierStates.contains(newNode.state)) {
                    frontier.add( newNode );
                    frontierStates.add( newNode.state );
                }
            }
        }
    }

    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     *
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...], or null if there is no solution
     */
    public static ArrayList<String> solve(MazeProblem problem) {
        ArrayList<String> keyPath;
        ArrayList<String> goalPath;
        try {
            keyPath = goFromTo( problem.INITIAL_STATE, problem.KEY_STATE, problem );
            goalPath = goFromTo( problem.KEY_STATE, problem.GOAL_STATES.get(0), problem);
            keyPath.addAll(goalPath);
            return keyPath;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<String> buildPath(SearchTreeNode root) {
        Stack<String> temp = new Stack<>();
        ArrayList<String> result = new ArrayList<>();
        while (root.parent != null) {
            temp.add(root.action);
            root = root.parent;
        }
        while (!temp.isEmpty()) {
            result.add(temp.pop());
        }
        return result;
    }

    public static void main(String args[]) {
        String[] maze = {
            "XXXXXXX",
            "XI.M..X",
            "X.....X",
            "XKX.XGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        System.out.println(Pathfinder.solve(prob));

        String[] maze1 = {
            "XXXXXXX",
            "XI....X",
            "X.....X",
            "X.XKXGX",
            "XXXXXXX"
        };
        MazeProblem prob1 = new MazeProblem(maze1);
        System.out.println(Pathfinder.solve(prob1));

        String[] maze2 = {
            "XXXXXXX",
            "XI.G..X",
            "X.MMMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob2 = new MazeProblem(maze2);
        System.out.println(Pathfinder.solve(prob2));

        String[] maze3 = {
            "XXXXXXX",
            "X.....X",
            "X.GIMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob3 = new MazeProblem(maze3);
        System.out.println(Pathfinder.solve(prob3));

        String[] maze4 = {
            "XXXXXXX",
            "X.....X",
            "X.GIMGX",
            "X.X.X.X",
            "XXXXXXX"
        };
        MazeProblem prob4 = new MazeProblem(maze4);
        System.out.println(Pathfinder.solve(prob4));

        String[] maze5 = {
            "XXXXXXX",
            "XK.X..X",
            "X.XIXGX",
            "X.X.X.X",
            "XXXXXXX"
        };
        MazeProblem prob5 = new MazeProblem(maze5);
        System.out.println(Pathfinder.solve(prob5));
        // System.out.println( "Distance from (2,1) to (5,1): " + prob.distance( new MazeState(2,1), new MazeState(5,1)));
        // System.out.println( "Distance from (1,2) to (5,1): " + prob.distance( new MazeState(1,2), new MazeState(5,1)));

        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(5 , new SearchTreeNodeComparator());
        SearchTreeNode a = new SearchTreeNode(new MazeState(2,1), "D", null, 4);
        frontier.add( new SearchTreeNode(new MazeState(1,1), "D", null, 4));
        frontier.add( new SearchTreeNode(new MazeState(2,1), "D", null, 4));
        frontier.add( new SearchTreeNode(new MazeState(1,2), "D", null, 6));
        frontier.add( new SearchTreeNode(new MazeState(3,1), "D", null, 4));
        frontier.add( new SearchTreeNode(new MazeState(2,2), "D", null, 6));
        frontier.add( new SearchTreeNode(new MazeState(4,1), "D", null, 4));
        frontier.add( new SearchTreeNode(new MazeState(3,2), "D", null, 6));
        frontier.add( new SearchTreeNode(new MazeState(2,1), "D", null, 4) );
        frontier.add( new SearchTreeNode(new MazeState(5,1), "D", null, 4));
        frontier.add( new SearchTreeNode(new MazeState(4,2), "D", null, 6));
        // System.out.println( Pathfinder.contains( frontier , new SearchTreeNode(new MazeState(2,1), "D", null, 4) ));
        // for ( int i = 0; i < 9; i++ ) {
        //     System.out.println( "Peek: " + frontier.peek() + " Removed: " + frontier.poll() );
        // }
        // //
        // PriorityQueue<String> s = new PriorityQueue<>();
        // s.add("hey");
        // s.add("there");
        // s.add("sire");
        // System.out.println( s.contains("sire"));
        // for ( int i = 0; i < 9; i++ ) {
        //     System.out.println( "Removed: " + frontier.remove());
        // }
        //
        // SearchTreeNodeComparator s = new SearchTreeNodeComparator();
        // System.out.println(s.equals(  new SearchTreeNode(new MazeState(4,2), "D", null, 6),  new SearchTreeNode(new MazeState(4,2), "D", null, 6)));

    }
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode{

    MazeState state;
    String action;
    SearchTreeNode parent;
    int cost;

    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     *
     * @param state  The MazeState (col, row) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */
    SearchTreeNode(MazeState state, String action, SearchTreeNode parent, int cost) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "(" + this.cost + " , " + "(" + this.state.col + "," + this.state.row + ") )";
    }

    public boolean equals(SearchTreeNode s) {
        return s.state.equals(this.state);
    }

    public MazeState getState() {
        return this.state;
    }
}

class SearchTreeNodeComparator implements Comparator<SearchTreeNode> {
    // Overriding compare()method of Comparator
    // for ascending order of cost
    public int compare(SearchTreeNode s1, SearchTreeNode s2) {
        if (s1.cost < s2.cost) {
            return -1;
        } else if (s1.cost > s2.cost) {
            return 1;
        }
        return 0;
    }
}
