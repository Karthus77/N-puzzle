package stud.runner;

import core.problem.Problem;
import core.runner.EngineFeeder;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.problem.Npuzzle.Position;
import stud.problem.Npuzzle.PuzzleFinding;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Description:
 *
 * @date:2022/10/3 19:31
 * @author:Karthus77
 */
public class PuzzleFeeder extends EngineFeeder {
    private static int[][] Zobrist;  //棋盘每个位置对应的随机数
    public static int[][] getZobrist() {
        return Zobrist;
    }
    public static void setZobrist(int[][] zobrist) {
        Zobrist = zobrist;
    }
    @Override
    public ArrayList<Problem> getProblems(ArrayList<String> problemLines) {
        ArrayList<Problem> problems = new ArrayList<>();
        //读取问题
        for (int i = 0; i < problemLines.size(); i++) {
            Position initialState = new Position();
            String[] puzzles = problemLines.get(i).split(" ");
            int size = Integer.parseInt(puzzles[0]);
            initialState.setSize(size);
            int[][] copy = new int[size][size];
            int cnt = 1;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    copy[x][y] = Integer.parseInt(puzzles[cnt]);
                    cnt++;
                }
            }
            initialState.setPuzzle(copy);
            Position goal = new Position();
            goal.setSize(size);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    copy[x][y] = Integer.parseInt(puzzles[cnt]);
                    cnt++;
                }
            }
                goal.setPuzzle(copy);
                PuzzleFinding problem = new PuzzleFinding(initialState, goal);
                problems.add(problem);
            }
            SecureRandom rand = new SecureRandom();
            Zobrist = new int[16][];
            for(int i=0;i<16;i++){
                Zobrist[i]= new int[16];
            for(int j=0;j<16;j++){
                Zobrist[i][j]=rand.nextInt();
            }
        }
            return problems;
    }
        @Override
        public Predictor getPredictor (HeuristicType type){
            return Position.predictor(type);
        }
}
