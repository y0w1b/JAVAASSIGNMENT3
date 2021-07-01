// GameBoard: 游戏棋盘
// 用于储存和打印当前棋盘状态、注册AI、根据输入运行游戏
public class GameBoard {
    // 当前棋盘状态，0为空，1为玩家一棋子，2为玩家二/电脑棋子
    private final int[][] boardStatus = new int[16][16];
    // 注册的GameAI,用于通知变化
    private SimpleGameAI ai;

    // 创建时预览棋盘
    public GameBoard() {
        this.printCurBoard();
        ai = null;
    }

    // 通过玩家输入的行数列数运行游戏，会有以下结果：
    // 1. 若输入位置已有棋子，返回-1并提示错误原因
    // 2. 若输入位置没有棋子，通知GameAI进行下一步操作
    public int nextStep(int row, int col) {
        if (boardStatus[row][col] == 0) {
            return this.ai.update(row, col);
        }
        else {
            System.out.println("错误：当前位置已有棋子！");
            return -1;
        }
    }

    // 返回当前棋盘状态
    public int[][] getBoardStatus() {
        return this.boardStatus;
    }

    // 注册AI
    public void registerAI(SimpleGameAI ai) {
        this.ai = ai;
        this.printCurBoard();
    }

    // 打印当前棋盘，空位置打印‘+‘，玩家一棋子则打印'O'，玩家二棋子则打印'X'
    public void printCurBoard() {
        System.out.print("  ");
        for (int i = 0; i < 16; i++) {
            System.out.print(Integer.toHexString(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < 16; i++) {
            System.out.print(Integer.toHexString(i) + " ");
            for (int j = 0; j < 16; j++) {
                if (boardStatus[i][j] == 2) {
                    System.out.print("X ");
                }
                else if(boardStatus[i][j] == 1) {
                    System.out.print("O ");
                }
                else {
                    System.out.print("+ ");
                }
            }
            System.out.println();
        }
    }
}
