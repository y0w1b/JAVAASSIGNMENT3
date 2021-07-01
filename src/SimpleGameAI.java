// SimpleGameAI: 简易游戏AI或基础AI
// 为PVP模式设计，储存所观察的棋盘对象和当前玩家，并在记录新的一步棋后判定胜负
public class SimpleGameAI {
    // 所观察的棋盘对象
    protected GameBoard gameStatus;
    // 当前游戏玩家，1为玩家一，二为玩家二
    protected int currentPlayer;

    // 初始化玩家和游戏棋盘
    public SimpleGameAI(GameBoard gameStatus) {
        this.gameStatus = gameStatus;
        currentPlayer = 1;
    }

    // 由GameBoard通知开始更新棋盘
    public int update(int row, int column) {
        // 更新棋盘并判定胜负，返回的数字为判定结果 0, 1, 2
        int result = checkWinning(row, column);
        // 打印更新后的棋盘
        gameStatus.printCurBoard();
        // 根据checkWinning的判定结果继续下一步
        // 0：无人胜利，打印玩家所下位置并切换玩家
        // 1：玩家一胜利
        // 2：玩家二胜利
        switch (result){
            case 0:
                if(currentPlayer == 1)
                    System.out.println("玩家一下在了 第" + row + "行" + "第" + column + "列");
                else
                    System.out.println("玩家二下在了 第" + row + "行" + "第" + column + "列");
                this.setCurrentPlayer();
                return 0;
            case 1:
                System.out.println("游戏结束，玩家1获得胜利！");
                return 1;
            case 2:
                System.out.println("游戏结束，玩家2获得胜利！");
                return 2;
            default:
                return 0;
        }
    }

    // 返回当前玩家
    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    // 切换玩家
    private void setCurrentPlayer() {
        if (currentPlayer == 1) currentPlayer = 2;
        else currentPlayer = 1;
    }

    // 根据输入行列数更新棋盘，并判定胜负
    // 因为达成胜利的棋（即将五个或以上的己方棋子连成一线）必定是最新的一步棋，
    // 所谓胜负判定根据最新的一步棋向上下、左右、左上右下、左下右上四条路线进行探索，若其中一条有五子以上相连，则判定当前玩家胜利。
    // 若判定玩家胜利，返回当前玩家（1或2），反之则返回0

    private int checkWinning(int row, int column) {
        // 更新棋盘
        int[][] status = gameStatus.getBoardStatus();
        status[row][column] = currentPlayer;

        // 向左右方向探索己方棋子，每探索到一个则进行记录，没探索到则直接跳出循环
        int result = 1;
        for(int i = row + 1; i < 16 && status[i][column] == currentPlayer; i++) {
            result++;
        }
        for(int i = row - 1; i >= 0 && status[i][column] == currentPlayer; i--) {
            result++;
        }
        // 若探索到的己方棋子大于或等于5，返回胜利玩家编号；反之初始化记录开始下一路线探索。
        if (result >= 5) return currentPlayer;
        else result = 1;

        // 向上下方向探索己方棋子，每探索到一个则进行记录，没探索到则直接跳出循环
        for(int i = column + 1; i < 16 && status[row][i] == currentPlayer; i++) {
            result++;
        }
        for(int i = column - 1; i >= 0 && status[row][i] == currentPlayer; i--) {
            result++;
        }
        // 若探索到的己方棋子大于或等于5，返回胜利玩家编号；反之初始化记录开始下一路线探索。
        if (result >= 5) return currentPlayer;
        else result = 1;

        // 向左上右下方向探索己方棋子，每探索到一个则进行记录，没探索到则直接跳出循环
        for(int i = row + 1, j = column + 1; j < 16 && i < 16 && status[i][j] == currentPlayer; i++, j++) {
            result++;
        }
        for(int i = row - 1, j = column - 1; i >= 0 && j >= 0 && status[i][j] == currentPlayer; i--, j--) {
            result++;
        }
        // 若探索到的己方棋子大于或等于5，返回胜利玩家编号；反之初始化记录开始最后路线探索。
        if (result >= 5) return currentPlayer;
        else result = 1;

        // 向左下右上方向探索己方棋子，每探索到一个则进行记录，没探索到则直接跳出循环
        for(int i = row + 1, j = column - 1; j >= 0 && i < 16 && status[i][j] == currentPlayer; i++, j--) {
            result++;
        }
        for(int i = row - 1, j = column + 1; i >= 0 && j < 16 && status[i][j] == currentPlayer; i--, j++) {
            result++;
        }
        // 若探索到的己方棋子大于或等于5，返回胜利玩家编号；反之则返回0（已探索完全部路线）
        if (result >= 5) return currentPlayer;

        return 0;
    }

}
