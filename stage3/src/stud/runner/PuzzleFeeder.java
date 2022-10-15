package stud.runner;

import core.problem.Problem;
import core.runner.EngineFeeder;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.problem.Npuzzle.Position;
import stud.problem.Npuzzle.PuzzleFinding;
import stud.queue.Zobrist;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Description:
 *
 * @date:2022/10/3 19:31
 * @author:Karthus77
 */
public class PuzzleFeeder extends EngineFeeder {
    @Override
    public ArrayList<Problem> getProblems(ArrayList<String> problemLines) {
        ArrayList<Problem> problems = new ArrayList<>();
        //读取问题
        for (int p = 0; p < problemLines.size(); p++) {
            String puzzles = problemLines.get(p);
              PuzzleFinding puzzleFinding=getPuzzle(puzzles);
              problems.add(puzzleFinding);
            }
            return problems;
    }
    private PuzzleFinding getPuzzle(String problemLine) {
        String[] puzzles = problemLine.split(" ");
        int size = Integer.parseInt(puzzles[0]);
        int[] state = new int[size * size];
        int[] goal = new int[size * size];
        for (int i = 1; i <= size * size; i++) {
            state[i - 1] = Integer.parseInt(puzzles[i]);
            goal[i - 1] = Integer.parseInt(puzzles[i + size * size]);
        }
        return new PuzzleFinding(new Position(size, state, true),new Position(size, goal, false));

    }
        @Override
        public Predictor getPredictor (HeuristicType type){
            return Position.predictor(type);
        }
}
