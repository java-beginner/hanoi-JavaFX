package application;

import hanoi.Hanoi;
import hanoi.Process;
import hanoi.Tower;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AppController {

    private static final int PADDING_TOP    = 50;
    private static final int PADDING_LEFT   = 50;
    private static final int PADDING_BOTTOM = 50;
    private static final int BASE_RECTANGLE_WIDTH  = 100;
    private static final int BASE_RECTANGLE_HEIGHT = 20;
    private static final int UNIT_OF_WIDTH_OF_SHAVE = 6;
    private static final int DURATION_MILLIS = 500;

    private Rectangle[] rectanges;

    private Hanoi hanoi;

    @FXML Pane targetPane;

    @FXML Button buttonInit;
    @FXML Button buttonMove;

    @FXML ComboBox<Integer> combBoxNumOfDisk;

    Rectangle[] baseRectangles = {
            new Rectangle(BASE_RECTANGLE_WIDTH, BASE_RECTANGLE_HEIGHT),
            new Rectangle(BASE_RECTANGLE_WIDTH, BASE_RECTANGLE_HEIGHT),
            new Rectangle(BASE_RECTANGLE_WIDTH, BASE_RECTANGLE_HEIGHT)
    };

    private int numOfDisks;

    @FXML
    private void init(ActionEvent event) {

        targetPane.getChildren().clear();

        numOfDisks = combBoxNumOfDisk.getSelectionModel().getSelectedItem().intValue();

        rectanges = new Rectangle[numOfDisks];
        for (int i = 0; i < numOfDisks; i++) {
            rectanges[i] = new Rectangle(BASE_RECTANGLE_WIDTH - 2 * UNIT_OF_WIDTH_OF_SHAVE * (i + 1), BASE_RECTANGLE_HEIGHT);
        }

        hanoi = new Hanoi(rectanges);

        for (int i = 0 ; i < rectanges.length; i++) {
            Rectangle r = rectanges[i];
            switch (i) {
            case 0:
                r.setFill(Color.CYAN);
                break;
            case 1:
                r.setFill(Color.RED);
                break;
            case 2:
                r.setFill(Color.BLUE);
                break;
            case 3:
                r.setFill(Color.DARKCYAN);
                break;
            default:
                break;
            }
            r.setLayoutX(PADDING_LEFT + (i + 1) * UNIT_OF_WIDTH_OF_SHAVE);
            r.setLayoutY(targetPane.getHeight() - BASE_RECTANGLE_HEIGHT - PADDING_BOTTOM - (i + 1) * r.getHeight());
            targetPane.getChildren().add(r);


        }

        // 土台
        for (int i = 0; i < baseRectangles.length; i++) {
            Rectangle r = baseRectangles[i];
            if (!targetPane.getChildren().contains(r)) {

                r.setX((BASE_RECTANGLE_WIDTH + PADDING_LEFT) * i + PADDING_LEFT);
                r.setY(targetPane.getHeight() - r.getHeight() - PADDING_BOTTOM);

                r.setFill(Color.BLACK);
                targetPane.getChildren().add(r);
            }
        }

        buttonInit.setDisable(false);
        buttonMove.setDisable(false);
    }

    @FXML
    private void move(ActionEvent event) {

        // 活性制御
        buttonInit.setDisable(true);
        buttonMove.setDisable(true);
        combBoxNumOfDisk.setDisable(true);

        Process p = hanoi.move();

        // 移動先タワーと移動した長方形を取得
        Tower towerMoveTo = hanoi.getTowers()[p.getMoveTo()];
        Rectangle rectangleMoveTo = towerMoveTo.getStep().getFirst();

        // 縦にアニメーション
        TranslateTransition t1 = new TranslateTransition(new Duration(DURATION_MILLIS), rectangleMoveTo);
        t1.setByY(-rectangleMoveTo.getLayoutY() + PADDING_TOP);

        // 移動対象の長方形を識別
        int indexOfRectangle = 0;
        for (int i = 0; i < rectanges.length; i++) {
            if (rectanges[i] == rectangleMoveTo) {
                indexOfRectangle = i;
                break;
            }
        }

        // 横へ移動する距離の算出
        double distX = 0;
        switch (p.getMoveFrom() - p.getMoveTo()) {
        case 1:
            // 左側へ1移動する場合
            distX = -rectangleMoveTo.getWidth()
                    - UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1)
                    - PADDING_LEFT
                    - UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1);
            break;
        case -1:
            // 右側へ1移動する場合
            distX = rectangleMoveTo.getWidth()
                    + UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1)
                    + PADDING_LEFT
                    + UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1);
            break;
        case 2:
            // 左側へ2移動する場合
            distX = -rectangleMoveTo.getWidth()
                    - UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1)
                    - PADDING_LEFT
                    - BASE_RECTANGLE_WIDTH
                    - PADDING_LEFT
                    - UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1);
            break;
        case -2:
            // 右側へ2移動する場合
            distX = rectangleMoveTo.getWidth()
                    + UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1)
                    + PADDING_LEFT
                    + BASE_RECTANGLE_WIDTH
                    + PADDING_LEFT
                    + UNIT_OF_WIDTH_OF_SHAVE * (indexOfRectangle + 1);
            break;
        }

        // 横に移動
        TranslateTransition t2 = new TranslateTransition(new Duration(DURATION_MILLIS), rectangleMoveTo);
        t2.setByX(distX);

        // 下に移動
        Tower towerMoveFrom = hanoi.getTowers()[p.getMoveFrom()];
        double distY = 0;
        switch (towerMoveTo.getStep().size() - towerMoveFrom.getStep().size() + 1) {
        case -1:
            distY = targetPane.getHeight() - PADDING_TOP - PADDING_BOTTOM - BASE_RECTANGLE_HEIGHT * 2;
            break;
        case 0:
            distY = targetPane.getHeight() - PADDING_TOP - PADDING_BOTTOM - BASE_RECTANGLE_HEIGHT * 2;
            break;
        case 1:
            distY = rectangleMoveTo.getLayoutY() - PADDING_TOP + BASE_RECTANGLE_HEIGHT;
            break;
        case 2:
            distY = rectangleMoveTo.getLayoutY() - PADDING_TOP;
            break;
        case 3:
            distY = rectangleMoveTo.getLayoutY() - PADDING_TOP - BASE_RECTANGLE_HEIGHT;
            break;
        case 4:
            distY = rectangleMoveTo.getLayoutY() - PADDING_TOP - BASE_RECTANGLE_HEIGHT * 2;
            break;
        case 5:
            distY = rectangleMoveTo.getLayoutY() - PADDING_TOP - BASE_RECTANGLE_HEIGHT * 3;
            break;
        default:
            break;
        }

        TranslateTransition t3 = new TranslateTransition(new Duration(DURATION_MILLIS), rectangleMoveTo);
        t3.setByY(distY);

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(t1, t2, t3);
        sequentialTransition.onFinishedProperty().set(e -> {
            // 活性制御
            buttonInit.setDisable(false);
            combBoxNumOfDisk.setDisable(false);
            if (hanoi.getTowerLast().getStep().size() == numOfDisks) {
                // ラストの位置に全て移動した場合、「移動」ボタンを非活性にする。
                buttonMove.setDisable(true);
            } else {
                buttonMove.setDisable(false);
            }
        });
        sequentialTransition.play();

    }

}
