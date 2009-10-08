/*
 * (c) �C�K���V�q���A�L
 */

package vavi.games.puyopuyo;


/**
 * PuyoPuyo.
 * <p>
 * �R���s���[�^�͊�{�I��3�A���ȏサ�����Ȃ��悤�ɂ��Ă���܂��B
 * �������A�t�B�[���h��̂Ղ�̐�����萔�𒴂���ƂՂ�����X�ɏ����Ă������[�h�ɐ؂�ւ��A
 * �Ղ�̐�����萔�������ƒʏ�̃��[�h�ɖ߂�܂��B
 * </p><p>
 * �R���s���[�^�͌v��I�ɂՂ��ς�ł���Ƃ����킯�ł͂Ȃ��A�s��������΂�����ł��B
 * Next�Ղ���Q�Ƃ�����A�e�X�̗��Ƃ����ɂ��ăp�����[�^���v�Z���A�p�����[�^�̗D�揇�ʂ��痎�Ƃ��������肵�܂��B
 * �p�����[�^�͑S����15��ނ���A���ꂼ��̗��Ƃ����ɑ΂��ăp�����[�^��D�揇�ʂ̍������ɔ�r���Ă����A
 * �ŏ��ɁA����p�����[�^����������傫���Ȃ��������̗p����悤�ɂ��Ă��܂��B
 * </p><p>
 * �R���s���[�^�͂Ղ�̐ςݕ��Ɍv�搫���Ȃ����߁A���Ղ͈��|�I�ɐl�Ԃ��L���ɂȂ�܂��B
 * �t�ɁA�Ղ悪�t�B�[���h��ɗ��܂��Ă���ƁA�v�Z�ɋ����R���s���[�^���L���ɂȂ�܂��B
 * ���������āA�R���s���[�^�ɏ����߂ɂ́A���ՂɂՂ���v��I�ɐς݁A�R���s���[�^���������傫�ȘA�������������邱�Ƃł��B
 * 4�A�����d�|�������2�A���Ń`�}�`�}�U�����Ă����Ώ��Ă܂��B�����B
 * </p>
 *
 * @author �C�K���V�q���A�L
 * @see "http://www7.plala.or.jp/isaragi/Lisu-ca/java/puyo/"
 */
public class PuyoPuyo {

    /** */
    public interface View {
        /** */
        void repaint();
        /** */
        void playClip(int i);
        /** */
        void play(int folder, int cn);
        /** */
        void stopClips();
    }

    /** */
    private View view;

    /** */
    public void setView(View view) {
        this.view = view;
    }

    /** */
    public static class Stage {
        Stage(int playersCount) {
            this.playersCount = playersCount;
            games = new PuyoPuyo[playersCount];
            disturbCounts = new int[playersCount];
            rankings = new int[playersCount];
            gameFlags = new int[playersCount];
        }

        /** */
        public void init() {
            leftPos = 2;
            lows = 14;
            columns = 6;
            ranking = playersCount;
            for (int i = 0; i < playersCount; i++) {
                disturbCounts[i] = 0;
                rankings[i] = 0;
                gameFlags[i] = 0;
            }
            // Next�Ղ�
            if (puyoFlag == 1) {
                int puyolist_count = (PuyoPuyo.colors.length - 1) * 50;
                puyoList = new int[puyolist_count];
                for (int i = 0; i < puyolist_count; i++) {
                    puyoList[i] = (i % (PuyoPuyo.colors.length - 1)) + 2;
                }
                for (int i = 0; i < puyolist_count; i++) {
                    int a = (int) (Math.random() * puyolist_count);
                    int b = (int) (Math.random() * puyolist_count);
                    int c = puyoList[a];
                    puyoList[a] = puyoList[b];
                    puyoList[b] = c;
                }
            }
        }

        /** �v���C�l�� */
        int playersCount;

        PuyoPuyo[] games;

        /** 1: �����Ղ�L�� */
        int set;
        /** */
        int soundFlag;
        /** 1: �Ղ效, else: �����_�� */
        int puyoFlag;
        /** */
        int skipFlag = 0;
        /** 1: ��܂ŗ��Ƃ� */
        int sss = 1;
        /** */
        int ef1 = 0;
        /** */
        int ef2 = 0;
        /** */
        int many = 0;

        /** �Ղ�̗����Ă���ʒu (0 ~ 5) �� 0 or 5 ���Ǝ������[�h�ɕs����� */
        int leftPos;
        /** �t�B�[���h�̏c�� */
        int lows;
        /** �t�B�[���h�̉��� */
        int columns;
        /** ���� */
        int ranking;

        /** ������ܐ��J�E���g */
        int[] disturbCounts;
        /** �Q�[���������肷��z�� */
        int[] gameFlags;
        /** ���ʗp�z�� */
        int[] rankings;
        /** */
        int[] puyoList;
    }

    /** �F */
    private static final String[] colors = {
        "gray", "red", "yellow", "blue", "green", "purple"
    };

    /** */
    private Stage stage;

    /** */
    private Thread autoFallTask;
    /** */
    private Thread otherTask;

    /** */
    private int id;
    /** */
    private Grid gridObject;
    /** */
    private int puyoListSize;

// ����e�X�g�p
String x;
//int[] cc = new int[5];
//int cc_sum;

    /** �I�[�g���[�h�֘A */
    private int mtime;
    /** �I�[�g���[�h�ł̉�]���� */
    private int rotateDirection;
    /** �I�[�g���[�h�ł̉�]�̎d�� */
    private int autoRotate;
    /** �I�[�g���[�h�ł̗�ԍ� */
    private int autoLow;

    /** @public �Ղ�i���o�[ */
    int puyo1;
    /** @public */
    int puyo2;
    /** @public */
    int npuyo1;
    /** @public */
    int npuyo2;
    /** @public */
    int nnpuyo1;
    /** @public */
    int nnpuyo2;
    /** �� */
    private int randomDisturbPos;
    /** �X���b�h�J�E���g */
    private int fallThreadCount;
    /** */
    private int eraseNumber;

    /** �A���� */
    private int connectCount;
    /** ��]���x�� */
    private int rotationPosition;
    /** @public */
    String overMessage;
    /** �X���[�v����s���� */
    private String sleepMode;
    /** �X���[�v�p���� */
    private int sleepTime;

    // �A���֘A

    /** �A���� */
    int chainCount;
    /** ���΂Ղ攭�΂ɂ��A���� */
    private int ignitionChainCount;
    /** ����܂ł̍ő�A������ێ� */
    private int maxChainCount;
    /** @public �A�����b�Z�[�W */
    String message;
    /** �A���̐��̃t�H���_ */
    private int folder;

    // �Ղ�֘A�̃X�s�[�h

    /** @public ���� */
    static final int FallSpeed = 1500;
    /** ���E�ړ� */
    private static final int MoveSpeed = 50;
    /** ��] */
    private static final int RotaSpeed = 100;

    /** @public ���肷�邽�߂̑҂��Ă邩���� */
    int waitFlag;
    /** �A������\�������邩���� */
    private int chainFlag;
    /** ���܂邩�ǂ������� */
    private int notBuryFlag;
    /** �A������3�ȏ�̉ӏ����������������� */
//    private int connect3Flag;
    /** �������������� */
//    private int tearFlag;
    /** @public �I�[�g���[�h������ */
    int autoFlag;
    /** �I�[�g���[�h�Ŕ��蒆������ */
    private int judgeFlag;
    /** �I�[�g���[�h�Ŕ��΂Ղ攻�蒆������ */
    private int ignitionJudgeFlag;

    /** ������� */
    private int disturbCount;
    /** */
    private int tempDisturbCount;
    /** */
    private int damDisturbCount;
    /** */
    private int disturbRest;
    /** */
    private int disturbRate;
    /** */
    private int disturbFlag;
    /** */
    private int disturbHeight;
    /** */
    private int damDisturbHeight;
    /** */
    private int disturbHeightRest;

    // ���_�pA-B-C-D

    /** @public */
    int score;
    /** */
    private int dScore;
    /** */
    private int A;
    /** */
    private static final int[] B = {
        0, 4, 20, 24, 32,
        48, 96, 160, 240,
        320, 480, 600, 700,
        800, 900, 999
    };
    /** */
    private static final int[] C = {
        0, 2, 3, 4,
        5, 6, 7, 10
    };
    /** */
    private int cMax;
    /** */
    private static final int[] D = {
        0, 3, 6, 12, 24
    };
    /** */
    private int dSum;
    /** score */
    private int bcd;

    // �e��z��

    /** @public �}�X�� */
    int[][] grid;
    /** �_�~�[�̃}�X�ڐς񂾒��� */
    private int[][] dummyGrid;
    /** �A���I���� */
    private int[][] preDummyGrid;
    /** �A�������x���p */
    private int[][] connectionLabel;
    /** �ǂꂭ�炢�������̂� */
    private int[][] fallLabel;
    /** �A�����x�� */
    private int[][] chainLabel;
    /** @public */
    int[][] lastChainLabel;
    /** */
    private int[][] preChainLabel;
    /** ���΂Ղ惉�x�� */
    private int[][] ignitionLabel2;
    /** @public */
    int[][] lastIgnitionLabel2;
    /** ��ɐF�Ղ悪���邩 */
    private int[][] colorList;
    /** �A�����ۑ��p */
    private int[][] connectionNumber;
    /** �����Ă����Ղ�̍��W */
    int[][] pos = new int[2][2];
    /** ���ꂼ��̐F�Ղ�̏������� */
    private int[] erasedNumber = new int[colors.length - 1];
    /** �A���񐔕ۑ��p */
    private int[] chainNumber = new int[5];
    /** �F���Ƃɏ����Ղ搔��ۑ�����p (�ꎞ�I��) */
    private int[] erasedPuyoList = new int[colors.length - 1];
    /** �����p�^�[���D�揇�� */
    private int[] params = new int[20];
    private int[] tempParams = new int[params.length];
    private int[] paramLabels = new int[params.length];
    private String[] paramList = new String[params.length];

    // �A�����b�Z�[�W
    private static final String[] voice1 = {
        "���Ă�", "������ȁ[", "��������", "��Ō���",
        "�ӂɂ႟�`", "���ĂĂĂĂ�", "���킟�����`"
    };

    private static final String[] voice2 = {
        "������", "�t�@�C���[", "�A�C�X�X�g�[��", "�_�C�A�L���[�g",
        "�u���C���_���h", "���グ��", "�΂悦�`��"
    };

    private String[] voice;

    /** �R���X�g���N�^ */
    PuyoPuyo(Stage stage, int id) {

        this.stage = stage;

        // �X���b�h
        autoFallTask = new AutoFallTask();
        otherTask = new OtherTask();

        grid = new int[stage.lows][stage.columns];
        dummyGrid = new int[stage.lows][stage.columns];
        preDummyGrid = new int[stage.lows][stage.columns];
        connectionLabel = new int[stage.lows][stage.columns];
        fallLabel = new int[stage.lows][stage.columns];
        chainLabel = new int[stage.lows][stage.columns];
        lastChainLabel = new int[stage.lows][stage.columns];
        preChainLabel = new int[stage.lows][stage.columns];
        ignitionLabel2 = new int[stage.lows][stage.columns];
        lastIgnitionLabel2 = new int[stage.lows][stage.columns];
        colorList = new int[stage.columns][colors.length - 1];
        connectionNumber = new int[stage.lows][stage.columns];

        //
        this.id = id;
        if (id == 0) {
            // �v���C���[
            voice = voice2;
            folder = 1;
        } else {
            // �R���s���[�^�[
            voice = voice1;
            folder = 2;
        }
        // Grid�I�u�W�F�N�g����
        gridObject = new Grid();
        // ������
        init();
    }

    /** ������ */
    void init() {

        // �ϐ�
        if (id == 0) {
            autoFlag = 0;
        } else {
            autoFlag = 1;
        }
// �e�X�g�p
x = "";
//for (int i = 0; i < cc.length; i++) {
//    cc[i] = 0;
//}
//cc_sum = 0;
        // �I�[�g���[�h
        rotateDirection = 1; // �I�[�g���[�h�ł̉�]����
        autoRotate = 0; // �I�[�g���[�h�ł̉�]�̎d��
        autoLow = 0; // �I�[�g���[�h�ł̗�ԍ�
        // �F�X
        fallThreadCount = 0; // �X���b�h�J�E���g
        connectCount = 0; // �A����
        rotationPosition = 0; // ��]���x��
        overMessage = "";
        chainCount = 0; // �A����
        ignitionChainCount = 0; // ���΂Ղ攭�΂ɂ��A����
        maxChainCount = 0; // ����܂ł̍ő�A������ێ�
//        message = ""; // �A�����b�Z�[�W
        // Flag
        waitFlag = 0; // �҂��Ă邩����
        chainFlag = 0; // �A������\�������邩����
        notBuryFlag = 0; // ���܂邩�ǂ�������
//        connect3Flag = 0; // �A������3�ȏ�̉ӏ�����������������
//        tearFlag = 0; // ��������������
        judgeFlag = 0; // �I�[�g���[�h�Ŕ��蒆������
        ignitionJudgeFlag = 0; // �I�[�g���[�h�Ŕ��΂Ղ攻�蒆������
        // �������
        disturbCount = 0;
        tempDisturbCount = 0;
        damDisturbCount = 0;
        disturbRest = 0;
        disturbRate = 120;
        disturbFlag = 0;
        // ���_
        score = 0;
        dScore = 0;
        A = 0;
        cMax = 0;
        dSum = 0;
        // �z��
        for (int i = 0; i < stage.lows; i++) {
            for (int j = 0; j < stage.columns; j++) {
                grid[i][j] = 0; // �}�X��
                dummyGrid[i][j] = 0; // �_�~�[�̃}�X��
                preDummyGrid[i][j] = 0; // �_�~�[�̃}�X��2
                fallLabel[i][j] = 0; // �ǂꂭ�炢�������̂�
                chainLabel[i][j] = 0; // �A�����x��
                lastChainLabel[i][j] = 0; // �A�����x��
                preChainLabel[i][j] = 0;
                connectionLabel[i][j] = 0; // �A�������x���p
                connectionNumber[i][j] = 0; // �A�����ۑ��p
                ignitionLabel2[i][j] = 0;
                lastIgnitionLabel2[i][j] = 0;
            }
        }
        for (int i = 0; i < colors.length - 1; i++) {
            erasedPuyoList[i] = 0; // �F���Ƃɏ����Ղ搔��ۑ�����p�i�ꎞ�I�Ɂj
            erasedNumber[i] = 0; // ���ꂼ��̐F�Ղ�̏�������
        }
        // �A���񐔕ۑ��p
        for (int i = 0; i < 5; i++) {
            chainNumber[i] = 0;
        }
        // ����
        for (int i = 0; i < stage.playersCount; i++) {
            stage.rankings[i] = 0;
        }
        for (int i = 0; i < params.length; i++) {
            params[i] = 0;
            tempParams[i] = 0;
            paramLabels[i] = 0;
        }
        for (int i = 0; i < colors.length - 1; i++) {
            for (int j = 0; j < stage.columns; j++) {
                colorList[j][i] = 0;
            }
        }

        // �Ղ搶��
        puyoListSize = -1;
        makePuyo1();
        // Grid�N���X������
        gridObject.init();
    }

    // �Q�[���{��

    /** setTimeout()�Ɏ����悤�Ȃ��� */
    void sleep(int time, String mode) {
        sleepTime = time;
        sleepMode = mode;
        otherTask = null;
        otherTask = new OtherTask();
        otherTask.start();
    }

    /** �Q�[���X�^�[�g */
    void start() {
        stage.gameFlags[id] = 1;
        // �Ղ�̍��W�����Z�b�g
        pos[0][0] = 0;
        pos[0][1] = stage.leftPos;
        pos[1][0] = 1;
        pos[1][1] = stage.leftPos;
        // NEXT2�Ղ搶�����\��
        makePuyo2();
        view.repaint();
        // ������
        autoFall();
        if (autoFlag == 1) {
            sleep(300, "AutoMove");
        }
    }

    /** �Q�[���I�[�o�[ */
    void gameOver() {
        // �t���O���I�t��
        stage.gameFlags[id] = 0;
        autoFlag = 0;
        // �摜������
        for (int i = 0; i < stage.lows; i++) {
            for (int j = 0; j < stage.columns; j++) {
                grid[i][j] = 0;
            }
        }
        // ���ʕt��
        stage.rankings[id] = stage.ranking;
        if (stage.playersCount == 1) {
            if (stage.soundFlag == 1) {
                view.stopClips();
                view.playClip(1);
            }
            overMessage = "�����܂�";
        } else if (stage.playersCount == 2) {
            if (stage.ranking == 1) {
                overMessage = "����";
            }
            if (stage.ranking == 2) {
                overMessage = "����";
            }
        } else {
            overMessage = stage.rankings[id] + "��";
        }
        stage.ranking--;
        // 2�ʂ����肵����
        if (stage.ranking == 1) {
            for (int i = 0; i < stage.playersCount; i++) {
                if (stage.rankings[i] == 0) {
                    stage.games[i].gameOver();
                }
            }
            if (stage.soundFlag == 1) {
                view.stopClips();
                view.playClip(1);
            }
        }
        // �\��
        view.repaint();
    }

    /** �Ղ�ςݏグ */
    void stack() {
        // ������i�Ղ悪���̏ꍇ�j
        if (pos[0][1] != pos[1][1]) {
//            tearFlag = 1;
            while (pos[0][0] + 1 < stage.lows && grid[pos[0][0] + 1][pos[0][1]] == 0) {
                pos[0][0]++;
            }
            while (pos[1][0] + 1 < stage.lows && grid[pos[1][0] + 1][pos[1][1]] == 0) {
                pos[1][0]++;
            }
        }
        // ���Ղ�ςݏグ
        grid[pos[0][0]][pos[0][1]] = puyo1;
        grid[pos[1][0]][pos[1][1]] = puyo2;
        // grid�̒l���R�s�[
        gridObject.copy1();
        // �\��
        if (stage.soundFlag == 1) {
            view.playClip(4);
        }
        view.repaint();
        chainCount = 0;
        if (stage.skipFlag == 1) {
            sleep(0, "Chain");
        } else {
            sleep(300, "Chain");
        }
    }

    /** �A���n���� */
    void chain() {
        gridObject.erase();
        // dummyGrid�̒l���R�s�[
        gridObject.copy2();
        if (chainFlag == 0) {
            // �����ĂȂ�
            if (chainCount >= 1) {
                chainNumber[Math.min(chainCount, 5) - 1]++;
            }
            next();
        } else {
            // ������

            // �A��
            chainCount++;
            int cn = Math.min(chainCount, voice.length);
            message = voice[cn - 1];
            if (stage.soundFlag == 1) {
                view.play(folder, cn);
            }
            if (maxChainCount < chainCount) {
                maxChainCount = chainCount;
            }
            // ���_�v�Z
            score();
            // ���Ƃ�
            if (stage.skipFlag == 1) {
                sleep(0, "Fall");
            } else {
                if (stage.soundFlag == 1) {
                    sleep(500, "Fall");
                } else {
                    sleep(300, "Fall");
                }
            }
            view.repaint();
        }
    }

//int maxdef = 0;

    /** �A���I���㏈�� */
    void next() {
// �e�X�g�\��
//if (chainCount >= 1) {
//    cc[Math.min(chainCount, 5) - 1]++;
//    cc_sum++;
//}
//maxdef = Math.max(maxdef, cc[2] - cc[0]);
//x = "mny=" + many + ",cc=" + cc[0] + "," + cc[1] + "," + cc[2] + "," + cc[3] + "," + cc[4] + ",md=" + maxdef;
//view.repaint();
        // ���Z�b�g
        pos[0][0] = 0;
        pos[0][1] = stage.leftPos;
        pos[1][0] = 1;
        pos[1][1] = stage.leftPos;
        rotationPosition = 0;
        chainCount = 0;
        message = "";
        // �Q�[���I�[�o�[
        if (grid[2][2] != 0 && stage.gameFlags[id] == 1) {
            if (disturbFlag == 1) {
                sleep(1500, "Over");
            } else {
                sleep(500, "Over");
            }
        } else {
            // �������
            disturb();
            if (grid[2][2] != 0 && stage.gameFlags[id] == 1) { // �Q�[���I�[�o�[
                if (disturbFlag == 1) {
                    sleep(1500, "Over");
                } else {
                    sleep(500, "Over");
                }
            } else { // ���Ƃ�
                if (disturbFlag == 1) {
                    sleep(1000, "AutoFall");
                } else {
                    sleep(300, "AutoFall");
                }
            }
        }
    }

    /** connectionLabel �����Z�b�g */
    void resetConnectionLabel() {
        for (int i = 0; i < stage.lows; i++) {
            for (int j = 0; j < stage.columns; j++) {
                connectionLabel[i][j] = 0;
            }
        }
    }

    /** ������� */
    void disturb() {
        // ����������������܂̐�
        disturbCount += tempDisturbCount;
        tempDisturbCount = 0;
        if (disturbCount > stage.disturbCounts[id]) {
            // �����̔���������������ܐ����A�X�g�b�N�̂�����ܐ���葽��
            disturbCount -= stage.disturbCounts[id];
            for (int i = 0; i < stage.playersCount; i++) {
                if (i != id && stage.gameFlags[i] == 1) {
                    stage.disturbCounts[i] += disturbCount;
                }
            }
            stage.disturbCounts[id] = 0;
        } else {
            // �����̔���������������ܐ����A�X�g�b�N�̂�����ܐ��ȉ�
            stage.disturbCounts[id] -= disturbCount;
        }
        // ���Z�b�g
        disturbCount = 0;
        // ������܂��~�点��
        disturbFlag = 0;
        if (stage.disturbCounts[id] > 0) {
            disturbFlag = 1;
            if (stage.disturbCounts[id] >= 6) {
                // �X�g�b�N�̂�����܂�6�ȏ�
                while (stage.disturbCounts[id] > 30) {
                    damDisturbCount++;
                    stage.disturbCounts[id]--;
                }
                disturbHeight = (stage.disturbCounts[id] - stage.disturbCounts[id] % stage.columns) / stage.columns;
                disturbHeightRest = stage.disturbCounts[id] % stage.columns;
                for (int j = 0; j < stage.columns; j++) {
                    damDisturbHeight = disturbHeight;
                    for (int i = stage.lows - 1; i >= 0; i--) {
                        if (grid[i][j] == 0 && damDisturbHeight > 0) {
                            stage.disturbCounts[id]--;
                            damDisturbHeight--;
                            grid[i][j] = 1;
                            if (damDisturbHeight == 0) {
                                break;
                            }
                        }
                    }
                    if (damDisturbHeight > 0) {
                        stage.disturbCounts[id] -= damDisturbHeight;
                    }
                }
            } else {
                // �X�g�b�N�̂�����܂�6����
                disturbHeightRest = stage.disturbCounts[id];
            }
            if (disturbHeightRest > 0) {
                int[] disturbList = new int[stage.columns];
                for (int a = 0; a < stage.columns; a++) {
                    disturbList[a] = 0;
                }
                while (stage.disturbCounts[id] > 0) {
                    randomDisturbPos = (int) (Math.random() * stage.columns);
                    if (disturbList[randomDisturbPos] == 1) {
                        continue;
                    } else {
                        disturbList[randomDisturbPos] = 1;
                        for (int i = stage.lows - 1; i >= 0; i--) {
                            if (grid[i][randomDisturbPos] == 0) {
                                stage.disturbCounts[id]--;
                                grid[i][randomDisturbPos] = 1;
                                break;
                            }
                        }
                    }
                }
            }
            if (damDisturbCount > 0) {
                stage.disturbCounts[id] = damDisturbCount;
                damDisturbCount = 0;
            }
            view.repaint();
            if (stage.soundFlag == 1) {
                view.playClip(5);
            }
        }
    }

    /** ���_�v�Z */
    void score() {
        // �A�����␳�i���_�v�Z�̂��߁j
        if (chainCount > B.length - 1) {
            chainCount = B.length - 1;
        }
        // A��C��D
        for (int i = 0; i < erasedPuyoList.length; i++) {
            // �����Ղ摍��
            A += erasedPuyoList[i];
            // ���������Ղ�
            if (cMax < erasedPuyoList[i] - 4) {
                cMax = erasedPuyoList[i] - 4;
            }
            if (cMax > C.length - 1) {
                cMax = C.length - 1;
            }
            // ���������F
            if (erasedPuyoList[i] != 0) {
                dSum++;
            }
        }
        // ���_�v�Z�iA*10*(B+C+D) = �����Ղ搔*10*(�A��+�����Ղ�+�����F)�j
        if (B[chainCount] + C[cMax] + D[dSum - 1] == 0) {
            bcd = 1;
        } else if (B[chainCount] + C[cMax] + D[dSum - 1] > 999) {
            bcd = 999;
        } else {
            bcd = B[chainCount] + C[cMax] + D[dSum - 1];
        }
        dScore = A * 10 * bcd;
        score += dScore;
        // �������
        tempDisturbCount += ((dScore + disturbRest) - (dScore + disturbRest) % disturbRate) / disturbRate;
        disturbRest = (disturbRest + dScore) % disturbRate;
        // ���Z�b�g
        A = 0;
        cMax = 0;
        dSum = 0;
        for (int i = 0; i < erasedPuyoList.length; i++) {
            erasedPuyoList[i] = 0;
        }
    }

    /** �Ղ��]���� */
    void rotate(int code) {
        if (code == 1) {
            // ���v���
            if (rotationPosition == 0 && pos[0][1] + 1 < stage.columns) {
                ajustRotation(1, 1, 1);
            } else if (rotationPosition == 1 && pos[0][0] + 1 < stage.lows) {
                ajustRotation(1, -1, 1);
            } else if (rotationPosition == 2 && pos[0][1] > 0) {
                ajustRotation(-1, -1, 1);
            } else if (rotationPosition == 3 && pos[0][0] > 0) {
                ajustRotation(-1, 1, 1);
            }
        } else if (code == 2) {
            // �����v���
            if (rotationPosition == 0 && pos[0][1] > 0) {
                ajustRotation(1, -1, -1);
            } else if (rotationPosition == 3 && pos[0][0] + 1 < stage.lows) {
                ajustRotation(1, 1, -1);
            } else if (rotationPosition == 2 && pos[0][1] + 1 < stage.columns) {
                ajustRotation(-1, 1, -1);
            } else if (rotationPosition == 1 && pos[0][0] > 0) {
                ajustRotation(-1, -1, -1);
            }
        }
        view.repaint();
    }

    /** �Ղ��]�̍ۂ̍��W�̐��� */
    void ajustRotation(int i, int j, int k) {
        if (grid[pos[0][0] + i][pos[0][1] + j] == 0) {
            if (stage.soundFlag == 1) {
                view.playClip(3);
            }
            pos[0][0] += i;
            pos[0][1] += j;
            rotationPosition += k;
            if (rotationPosition < 0) {
                rotationPosition += 4;
            }
            rotationPosition %= 4;
        }
    }

    /** �Ղ��ړ� */
    void up() {
        if (pos[0][0] > 1 && pos[1][0] > 1) {
            pos[0][0]--;
            pos[1][0]--;
            if (stage.soundFlag == 1) {
                view.playClip(2);
            }
        }
        view.repaint();
    }

    /** �Ղ�E�ړ� */
    void right() {
        if (!(((pos[0][1] == stage.columns - 1) || (pos[0][1] < stage.columns - 1 && grid[pos[0][0]][pos[0][1] + 1] != 0)) ||
              ((pos[1][1] == stage.columns - 1) || (pos[1][1] < stage.columns - 1 && grid[pos[1][0]][pos[1][1] + 1] != 0)))) {
            pos[0][1]++;
            pos[1][1]++;
            if (stage.soundFlag == 1) {
                view.playClip(2);
            }
        }
        view.repaint();
    }

    /** �Ղ捶�ړ� */
    void left() {
        if (!(((pos[0][1] == 0) || (pos[0][1] - 1 >= 0 && grid[pos[0][0]][pos[0][1] - 1] != 0)) ||
              ((pos[1][1] == 0) || (pos[1][1] - 1 >= 0 && grid[pos[1][0]][pos[1][1] - 1] != 0)))) {
            pos[0][1]--;
            pos[1][1]--;
            if (stage.soundFlag == 1) {
                view.playClip(2);
            }
        }
        view.repaint();
    }

    /** �Ղ扺�ړ� */
    void down() {
        if (!(((pos[0][0] + 1 == stage.lows) || (pos[0][0] + 1 < stage.lows && grid[pos[0][0] + 1][pos[0][1]] != 0)) ||
              ((pos[1][0] + 1 == stage.lows) || (pos[1][0] + 1 < stage.lows && grid[pos[1][0] + 1][pos[1][1]] != 0)))) {
            // ���Ɉړ�����
            pos[0][0]++;
            pos[1][0]++;
            if (stage.soundFlag == 1) {
                view.playClip(2);
            }
            view.repaint();
        } else {
            // ���Ɉړ��ł��Ȃ��̂ŁA�ςݏグ��
            waitFlag = 1;
            stack();
        }
    }

    /** �Ղ��C�ɉ��ړ� */
    void bottom() {
        if (pos[0][1] != pos[1][1]) {
            // �Ղ悪���̂Ƃ�
            while (pos[0][0] + 1 < stage.lows && grid[pos[0][0] + 1][pos[0][1]] == 0) {
                pos[0][0]++;
            }
            while (pos[1][0] + 1 < stage.lows && grid[pos[1][0] + 1][pos[1][1]] == 0) {
                pos[1][0]++;
            }
        } else {
            // �Ղ悪�c�̂Ƃ�
            while (pos[0][0] + 1 < stage.lows &&
                   pos[1][0] + 1 < stage.lows &&
                   grid[pos[0][0] + 1][pos[0][1]] == 0 &&
                   grid[pos[1][0] + 1][pos[1][1]] == 0) {
                for (int i = 0; i < 2; i++) {
                    pos[i][0]++;
                }
            }
        }
        grid[pos[0][0]][pos[0][1]] = puyo1;
        grid[pos[1][0]][pos[1][1]] = puyo2;
        view.repaint();
        waitFlag = 1;
        stack();
    }

    /** �Ղ掩������ */
    void autoFall() {
        if (!(((pos[0][0] + 1 == stage.lows) ||
               (pos[0][0] + 1 < stage.lows && grid[pos[0][0] + 1][pos[0][1]] != 0)) ||
              ((pos[1][0] + 1 == stage.lows) ||
               (pos[1][0] + 1 < stage.lows && grid[pos[1][0] + 1][pos[1][1]] != 0)))) {
            // ���Ɉړ��ł���
            pos[0][0]++;
            pos[1][0]++;
            view.repaint();
            // �X���b�h
            if (stage.gameFlags[id] == 1) {
                autoFallTask = null;
                autoFallTask = new AutoFallTask();
                autoFallTask.start();
            }
        } else if (waitFlag == 0) {
            // ���Ɉړ��ł��Ȃ��̂ŁA�ςݏグ��
            waitFlag = 1;
            stack();
        }
    }

    /** �Ղ搶��1  (���[�h����̂�) */
    void makePuyo1() {
        // NEXT�����NEXT2�Ղ�𐶐�
        if (stage.puyoFlag == 1) {
            npuyo1 = getCopiedyPuyo();
            npuyo2 = getCopiedyPuyo();
            nnpuyo1 = getCopiedyPuyo();
            nnpuyo2 = getCopiedyPuyo();
        } else {
            npuyo1 = getRandomPuyo();
            npuyo2 = getRandomPuyo();
            nnpuyo1 = getRandomPuyo();
            nnpuyo2 = getRandomPuyo();
        }
        // ������
        if (stage.set == 1) {
            // 7�A��
            npuyo1 = 5;
            final int[][] initialGrid = {
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 },
                { 3, 0, 0, 0, 0, 0 },
                { 2, 0, 0, 0, 0, 0 },
                { 5, 0, 0, 0, 0, 0 },
                { 5, 0, 0, 0, 0, 0 },
                { 5, 4, 5, 6, 2, 0 },
                { 2, 3, 4, 5, 6, 2 },
                { 2, 3, 4, 5, 6, 2 },
                { 2, 3, 4, 5, 6, 2 }
            };
            // ������
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    grid[i][j] = initialGrid[i][j];
                }
            }
        }
    }

    /** �Ղ搶��2 (�\���Ղ�𐄈ڂ�������) */
    void makePuyo2() {
        // �Ղ搄��
        puyo1 = npuyo1;
        puyo2 = npuyo2;
        npuyo1 = nnpuyo1;
        npuyo2 = nnpuyo2;
        // NEXT2�Ղ�𐶐�
        if (stage.puyoFlag == 1) {
            nnpuyo1 = getCopiedyPuyo();
            nnpuyo2 = getCopiedyPuyo();
        } else {
            nnpuyo1 = getRandomPuyo();
            nnpuyo2 = getRandomPuyo();
        }
    }

    /** �p�ӂ��Ă������Ղ��Ԃ� */
    int getCopiedyPuyo() {
        puyoListSize++;
        if (puyoListSize == stage.puyoList.length) {
            puyoListSize = 0;
        }
        return stage.puyoList[puyoListSize];
    }

    /** �Ղ搶���p�̗�����Ԃ� */
    int getRandomPuyo() {
        return (int) (Math.random() * (colors.length - 1)) + 2;
    }

    // �I�[�g���[�h�֘A

    /** ���C�� */
    void autoMove() {
        // ���Z�b�g1
        judgeFlag = 1;
        mtime = 0;
        autoRotate = 0;
        autoLow = 0;
        rotateDirection = 1;
        for (int k = 0; k < params.length; k++) {
            params[k] = 0;
        }
        // grid�̒l���R�s�[
        gridObject.copy1();
        // �A���S���Y��������
        int puyon = gridObject.getPuyoNum();
        if (puyon > 36) { // (int) (width * height / 3)
            stage.ef1 = 1;
            stage.ef2 = 1;
            stage.many = 1;
        } else if (stage.many == 0 || (stage.many == 1 && puyon < 24)) {
//            stage.ef1 = (int) (Math.random() * 4);
//            stage.ef2 = 1;
            stage.ef1 = 0;
            stage.ef2 = 0;
            stage.many = 0;
        }
        // �p�����[�^�擾�̏��Ԃ�����
        if (stage.many == 0) {
            paramList[2] = "ignition_sum"; // ���΂Ղ惉�x���̍��v
            paramList[3] = "ignition_count1"; // ���΂Ղ�i�A������2�ȏ�j�̌�
            paramList[4] = "chain_sum"; // �A�����x���̍��v
            paramList[5] = "ignition_connect_sum"; // ���΂Ղ�i�A������2�ȏ�j�̘A�����̍��v
            paramList[6] = "connect_increment1"; // ���Ό�̘A�����̑����̍��v1
            paramList[7] = "wall_count"; // �ǂ̌�
            paramList[8] = "connect_sum"; // �ŏI��Ԃ̘A����
            paramList[9] = "connect_increment2"; // ���Ό�̘A�����̑����̍��v2
            paramList[10] = "ignition_count2"; // ���΂Ղ�i�A������1�j�̌��i�A�����̍��v�j
            paramList[11] = "connect_bury_count"; // �ǂꂾ���̘A������傫���ł������ȉӏ������܂��Ă��邩
            paramList[12] = "bury_count"; // �ǂꂾ���̂Ղ悪���܂��Ă��邩
            paramList[13] = "dist"; // ���F�Ղ�܂ł̋���
            paramList[14] = "h_point"; // �Ⴓ
        } else {
            paramList[2] = "wall_count"; // �ǂ̌�
            paramList[3] = "ignition_sum"; // ���΂Ղ惉�x���̍��v
            paramList[4] = "chain_sum"; // �A�����x���̍��v
            paramList[5] = "connect_sum"; // �ŏI��Ԃ̘A����
            paramList[6] = "ignition_count1"; // ���΂Ղ�i�A������2�ȏ�j�̌�
            paramList[7] = "ignition_connect_sum"; // ���΂Ղ�i�A������2�ȏ�j�̘A�����̍��v
            paramList[8] = "ignition_count2"; // ���΂Ղ�i�A������1�j�̌��i�A�����̍��v�j
            paramList[9] = "connect_increment1"; // ���Ό�̘A�����̑����̍��v1
            paramList[10] = "connect_increment2"; // ���Ό�̘A�����̑����̍��v2
            paramList[11] = "connect_bury_count"; // �ǂꂾ���̘A������傫���ł������ȉӏ������܂��Ă��邩
            paramList[12] = "bury_count"; // �ǂꂾ���̂Ղ悪���܂��Ă��邩
            paramList[13] = "dist"; // ���F�Ղ�܂ł̋���
            paramList[14] = "h_point"; // �Ⴓ
        }
        // �e�X�̗�̍������擾
        int[] heightlist = gridObject.getHeight();
        // �ǂ��擾
        int leftWall = -1;
        int rightWall = stage.columns;

//        for (int j = stage.leftPos - 1; j >= 0; j--) {
//            if (dummyGrid[2][j] != 0) {
//                left_wall = j;
//                break;
//            }
//        }
//        for (int j = stage.leftPos + 1; j < width; j++) {
//            if (dummyGrid[2][j] != 0) {
//                right_wall = j;
//                break;
//            }
//        }

        // ��]�������肵�Ĕ���
        for (int j = leftWall + 1; j < rightWall; j++) {
            if (heightlist[j] >= 3) {
                // ���̂܂�
                judgePart(heightlist[j] - 1, j, puyo2, heightlist[j] - 2, j, puyo1, 0);
                if (j + 1 < rightWall && heightlist[j + 1] >= 3) {
                    // �E��]
                    judgePart(heightlist[j] - 1, j, puyo2, heightlist[j + 1] - 1, j + 1, puyo1, 1);
                }
                if (j - 1 >= leftWall + 1 && heightlist[j - 1] >= 3) {
                    // ����]
                    judgePart(heightlist[j] - 1, j, puyo2, heightlist[j - 1] - 1, j - 1, puyo1, 2);
                }
                if (heightlist[j] >= 4) {
                    // 2��]
                    judgePart(heightlist[j] - 1, j, puyo1, heightlist[j] - 2, j, puyo2, 3);
                }
            }
        }
        // ��Ԃ̃|�C���g�ɗ��Ƃ�
        mtime = autoLow - stage.leftPos;
        if (autoRotate == 2 || (autoRotate == 3 && mtime >= 0)) {
            rotateDirection = 2;
        }
        autoMove2();
        // ���Z�b�g
        judgeFlag = 0;
// �e�X�g�\��
//x = "";
//for (int i = 0; i < 15; i++) {
//    x += params[i] + ",";
//}
//view.repaint();
    }

    /** �ړ����� */
    void autoMove2() {
        if (stage.skipFlag == 1) {
            if (mtime > 0) { // �E
                sleep(0, "Right");
            } else if (mtime < 0) { // ��
                sleep(0, "Left");
            } else { // ���̂܂�
                sleep(0, "Rotate");
            }
        } else {
            if (mtime > 0) { // �E
                sleep(MoveSpeed, "Right");
            } else if (mtime < 0) { // ��
                sleep(MoveSpeed, "Left");
            } else { // ���̂܂�
                sleep(RotaSpeed + MoveSpeed, "Rotate");
            }
        }
    }

    /** �E�ړ� */
    void autoMoveRight() {
        right();
        mtime--;
        if (stage.skipFlag == 1) {
            if (mtime > 0) {
                sleep(0, "Right");
            } else {
                sleep(0, "Rotate");
            }
        } else {
            if (mtime > 0) {
                sleep(MoveSpeed, "Right");
            } else {
                sleep(RotaSpeed + MoveSpeed, "Rotate");
            }
        }
    }

    /** ���ړ� */
    void autoMoveLeft() {
        left();
        mtime++;
        if (stage.skipFlag == 1) {
            if (mtime < 0) {
                sleep(0, "Left");
            } else {
                sleep(0, "Rotate");
            }
        } else {
            if (mtime < 0) {
                sleep(MoveSpeed, "Left");
            } else {
                sleep(RotaSpeed + MoveSpeed, "Rotate");
            }
        }
    }

    /** �����p�^�[������ */
    void judgePart(int i, int j, int g, int i2, int j2, int g2, int r) {

        // �t�B�[���h�ɂ���Ղ�Ɨ��Ƃ����g�Ղ�̃R�s�[���c���Ă��������x�������Z�b�g
        for (int k = 0; k < stage.lows; k++) {
            for (int l = 0; l < stage.columns; l++) {
                dummyGrid[k][l] = grid[k][l];
                chainLabel[k][l] = 0;
                preChainLabel[k][l] = 0;
                ignitionLabel2[k][l] = 0;
            }
        }
        // ���Ղ��ς�
        dummyGrid[i][j] = g;
        dummyGrid[i2][j2] = g2;
        gridObject.copy1(dummyGrid);
        // �ϐ�������
        eraseNumber = 0;
        chainFlag = 0;
        int nonStackFlag = 0;
        // param�֘A�z������Z�b�g
        for (int k = 0; k < params.length; k++) {
            paramLabels[k] = 0;
        }
        // �A�����Ə����Ղ摍��
        tempParams[0] = getChainCount();
        tempParams[1] = eraseNumber;
        // �A����̃t�B�[���h�ɂ���Ղ�̃R�s�[���c���Ă���
        for (int k = 0; k < stage.lows; k++) {
            for (int l = 0; l < stage.columns; l++) {
                preDummyGrid[k][l] = dummyGrid[k][l];
            }
        }
        // �����Ȃ��Ȃ�A�����ς�łȂ�������
        if (tempParams[0] == 0) {
            int nh = (stage.lows / 3) + 2;
            if (gridObject.getPuyoNum() < 36 && (i < nh || i2 < nh)) {
                nonStackFlag = 1;
            }
        }
        // �X�V����K�v�����邩����
        if (nonStackFlag == 0 &&
            dummyGrid[2][2] == 0 &&
            ((tempParams[0] == 1 && stage.ef1 == 1) ||
             (tempParams[0] == 2 && stage.ef2 == 1) ||
             (tempParams[0] != 1 && tempParams[0] != 2))) {

            // �p�����[�^���擾���Ă����Ȃ����r
            for (int p = 0; p < params.length; p++) {
                if (paramLabels[p] == 0) {
                    getParam(p);
                }
                if (tempParams[p] < params[p]) {
                    // �l���������Ȃ�_����
                    break;
                } else if (tempParams[p] > params[p]) {
                    // �l���傫���Ȃ�X�V

                    // �c��̃p�����[�^�����߂�
                    for (int k = p + 1; k < params.length; k++) {
                        if (paramLabels[k] == 0) {
                            getParam(k);
                        }
                    }
                    // �l����
                    for (int k = 0; k < params.length; k++) {
                        params[k] = tempParams[k];
                    }
                    // �A�����x��
                    for (int k = 0; k < stage.lows; k++) {
                        for (int l = 0; l < stage.columns; l++) {
                            lastChainLabel[k][l] = chainLabel[k][l];
                            lastIgnitionLabel2[k][l] = ignitionLabel2[k][l];
                        }
                    }
                    // �p�^�[�����X�V
                    autoRotate = r;
                    autoLow = j;
                    // ������
                    break;
                }
            }
        }
    }

    /** �p�����[�^�擾 */
    void getParam(int p) {

        if (paramList[p] == "ignition_connect_sum" ||
            paramList[p] == "ignition_count1" ||
            paramList[p] == "ignition_count2" ||
            paramList[p] == "connect_increment1" ||
            paramList[p] == "connect_increment2" ||
            paramList[p] == "ignition_sum" ||
            paramList[p] == "chain_sum") {
            // ���΂Ղ�֘A

            // �z��ɒl����
            int[] array = gridObject.judgeIgnition();
            int ignition_connect_sum = array[0];
            int ignition_count1 = array[1];
            int ignition_count2 = array[2];
            int connect_increment1 = array[3];
            int connect_increment2 = array[4];
            int ignition_sum = array[5];
            // �A�����x���̍��v���Z�o
            int chain_sum = 0;
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    chain_sum += chainLabel[i][j];
                    preChainLabel[i][j] = 0; // ���Z�b�g
                }
            }
            // temp_param���X�V
            for (int i = 0; i < params.length; i++) {
                int flag = 0;
                if (paramList[i] == "ignition_connect_sum") {
                    tempParams[i] = ignition_connect_sum;
                } else if (paramList[i] == "ignition_count1") {
                    tempParams[i] = ignition_count1;
                } else if (paramList[i] == "ignition_count2") {
                    tempParams[i] = ignition_count2;
                } else if (paramList[i] == "connect_increment1") {
                    tempParams[i] = connect_increment1;
                } else if (paramList[i] == "connect_increment2") {
                    tempParams[i] = connect_increment2;
                } else if (paramList[i] == "ignition_sum") {
                    tempParams[i] = ignition_sum;
                } else if (paramList[i] == "chain_sum") {
                    tempParams[i] = chain_sum;
                } else {
                    flag = 1;
                }
                if (flag == 0) {
                    paramLabels[i] = 1;
                }
            }
        } else if (paramList[p] == "wall_count") {
            // �ǂɂȂ��Ă����̌� (�[������) ���v�Z
            tempParams[p] = 0;
            for (int j = 1; j < stage.columns - 1; j++) {
                if (dummyGrid[2][j] != 0) {
                    tempParams[p]++;
                }
            }
            tempParams[p] *= -1;
        } else if (paramList[p] == "connect_sum") {
            // �A�����̍��v���v�Z
            tempParams[p] = gridObject.getConnectionSum();
        } else if (paramList[p] == "connect_bury_count") {
            // �A������傫���ł������ȉӏ����������܂��Ă��邩
            tempParams[p] = 0;
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    tempParams[p] += gridObject.judgeEraseStack(i, j, dummyGrid[i][j]);
                }
            }
            tempParams[p] *= -1;
        } else if (paramList[p] == "bury_count") {
            // �S�Ă̂Ղ�ɂ��Ė��܂��Ă��邩�ǂ�������
            tempParams[p] = 0;
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    if (gridObject.judgeStack(i, j) == 0) {
                        tempParams[p]++;
                    }
                }
            }
            tempParams[p] *= -1;
        } else if (paramList[p] == "dist") {
            // ���̗�ɂ��铯�F�Ղ�܂ł̋����̍ő�l���Z�o
            tempParams[p] = gridObject.getDistance();
            tempParams[p] *= -1;
        } else if (paramList[p] == "h_point") {
            // �e�X�̗�̍������擾�����ꂼ��̂Ղ�̍����̍��v���Z�o
            tempParams[p] = 0;
            int[] heightlist = gridObject.getHeight();
            for (int i = 0; i < stage.columns; i++) {
                for (int j = heightlist[i]; j < stage.lows; j++) {
                    tempParams[p] += j;
                }
            }
        }
    }

    // �A���n����2
    int getChainCount() {
        int count = 0;
        gridObject.erase();
        while (chainFlag == 1) {
            count++;
            gridObject.fall();
            chainFlag = 0;
            gridObject.erase();
        }
        return count;
    }

    /**
     * Grid�N���X
     */
    class Grid {
        /** ������ */
        void init() {
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    dummyGrid[i][j] = 0;
                }
            }
        }

        /** grid�̒l���R�s�[ (���������Ȃ�) */
        void copy1() {
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    dummyGrid[i][j] = grid[i][j];
                }
            }
        }

        /** grid�̒l���R�s�[ (���������) */
        void copy1(int[][] g) {
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    dummyGrid[i][j] = g[i][j];
                }
            }
        }

        /** dummy_grid�̒l���R�s�[ */
        void copy2() {
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    grid[i][j] = dummyGrid[i][j];
                }
            }
        }

        /** �Ղ�1�̘A�������擾 (���C��) */
        int getConnection(int i2, int j2) {
            int i = i2;
            int j = j2;
            connectCount = 0;
            getConnection2(i, j);
            resetConnectionLabel();
            return connectCount;
        }

        /** �Ղ�1�̘A�������擾 (�ċN�Ăяo����) */
        private void getConnection2(int i2, int j2) {
            int i = i2;
            int j = j2;
            if (dummyGrid[i][j] > 1 && connectionLabel[i][j] != 1) {
                connectionLabel[i][j] = 1;
                connectCount++;
                if (i - 1 >= 0 && dummyGrid[i][j] == dummyGrid[i - 1][j]) {
                    getConnection2(i - 1, j);
                }
                if (i + 1 <= stage.lows - 1 && dummyGrid[i][j] == dummyGrid[i + 1][j]) {
                    getConnection2(i + 1, j);
                }
                if (j - 1 >= 0 && dummyGrid[i][j] == dummyGrid[i][j - 1]) {
                    getConnection2(i, j - 1);
                }
                if (j + 1 <= stage.columns - 1 && dummyGrid[i][j] == dummyGrid[i][j + 1]) {
                    getConnection2(i, j + 1);
                }
            }
        }

        /** �ςݏオ���Ă����̂𗎂Ƃ� */
        void fall() {
            int di;
            for (int i = stage.lows - 2; i >= 0; i--) {
                for (int j = stage.columns - 1; j >= 0; j--) {
                    if (dummyGrid[i][j] != 0) {
                        di = i;
                        while (di + 1 < stage.lows && dummyGrid[di + 1][j] == 0) {
                            dummyGrid[di + 1][j] = dummyGrid[di][j];
                            dummyGrid[di][j] = 0;
                            di++;
                            // �I�[�g���[�h���蒆
                            if (ignitionJudgeFlag == 1) {
                                fallLabel[di][j] = fallLabel[di - 1][j] + 1;
                                fallLabel[di - 1][j] = 0;
                            }
                        }
                    }
                }
            }
            // ���ۂ̏ꍇ
            if (judgeFlag == 0) {
                copy2();
                view.repaint();
                if (stage.skipFlag == 1) {
                    sleep(0, "Chain");
                } else {
                    if (stage.soundFlag == 1) {
                        sleep(500, "Chain");
                    } else {
                        sleep(300, "Chain");
                    }
                }
            }
        }

        /** �A������4�ȏ�̂Ղ������ */
        void erase() {
            chainFlag = 0;
            // �S�Ă̂Ղ�̘A�������v�Z
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    connectionNumber[i][j] = getConnection(i, j);
                }
            }
            // �S�Ă̂Ղ�ɂ��Ĕ���
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    // �A������4�ȏ�Ȃ�
                    if (connectionNumber[i][j] >= 4) {
                        chainFlag = 1;
                        if (judgeFlag == 0) {
                            // ���ۂɏ����ꍇ
                            erasedPuyoList[dummyGrid[i][j] - 2]++; // �F���Ƃɏ����Ղ搔��ۑ�����p�i�ꎞ�I�Ɂj
                            erasedNumber[dummyGrid[i][j] - 2]++; // ���ꂼ��̐F�Ղ�̏����������v���X
                        } else if (ignitionJudgeFlag == 0) {
                            // �I�[�g���[�h�ŁA���΂Ղ攻������Ă��Ȃ��ꍇ
                            eraseNumber++;
                        }
                        // ����
                        dummyGrid[i][j] = 0;
                        fallLabel[i][j] = 0;
                        if (i > 0 && dummyGrid[i - 1][j] == 1) {
                            dummyGrid[i - 1][j] = 0;
                            fallLabel[i - 1][j] = 0;
                        }
                        if (j + 1 < stage.columns && dummyGrid[i][j + 1] == 1) {
                            dummyGrid[i][j + 1] = 0;
                            fallLabel[i][j + 1] = 0;
                        }
                        if (i + 1 < stage.lows && dummyGrid[i + 1][j] == 1) {
                            dummyGrid[i + 1][j] = 0;
                            fallLabel[i + 1][j] = 0;
                        }
                        if (j > 0 && dummyGrid[i][j - 1] == 1) {
                            dummyGrid[i][j - 1] = 0;
                            fallLabel[i][j - 1] = 0;
                        }
                    }
                }
            }
        }

        /** ���΂Ղ攻�� */
        int[] judgeIgnition() {
            // �ϐ�������
            ignitionJudgeFlag = 1;
            // �ϐ���`
            int[] array = {
                0, 0, 0, 0, 0, 0
            };
            // �S�Ă̂Ղ�ɂ��Ĕ���
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    // �ϐ�������
                    ignitionChainCount = 1;
                    int connectSum1 = 0;
                    int connectSum2 = 0;
                    int connect = getConnection(i, j);
                    // �F�Ղ�ɂ��Ă̂ݔ���i���Γ_�ɂՂ��u����ꍇ�j
                    if (dummyGrid[i][j] > 1 && judgeIgnitionPoint(i, j) == 1) {
                        // �A������܂��̂Ղ�
                        if (preChainLabel[i][j] == 0) {
                            // �Y���Ղ�A�y�т���ƘA�������Ղ�������ĘA������
                            erase2(i, j, dummyGrid[i][j]);
                            fall();
                            ignitionChainCount = getChainCount() + 1;
                            // �A���I����̘A�����̍��v���Z�o
                            connectSum2 = getConnectionSum();
                            // �A���Ɏg�����Ղ�Ƀ��x����t���Č��̈ʒu�ɖ߂�
                            myRet();
                            // �A���Ɏg��Ȃ������Ղ�̘A���J�n�O�̘A�����̍��v���Z�o
                            connectSum1 = getConnectionSum();
                        }
                        // �Ղ��fallLabel��߂�
                        for (int k = 0; k < stage.lows; k++) {
                            for (int l = 0; l < stage.columns; l++) {
                                dummyGrid[k][l] = preDummyGrid[k][l];
                                fallLabel[k][l] = 0;
                            }
                        }
                        if (ignitionChainCount >= 2) { // �A�������Ȃ甭�΂Ղ�
                            // �A�����x����t����
                            attacheLabel(i, j, ignitionChainCount - 1, chainLabel[i][j]);
                            resetConnectionLabel();
                            // �A������2�ȏ�̔��΂Ղ�
                            if (connect > 1) {
                                array[0] += connect * connect; // �A�����̍��v
                                array[1] += connect; // ��
                            }
                            // �A������1�̔��΂Ղ�
                            else {
//                                array[0]++; // �A�����̍��v
                                array[2]++; // ��
                            }
                            // �A�����������Ă����Ȃ�A�ϐ��ɒl����
                            if (connectSum2 > connectSum1) {
                                array[3] += (connectSum2 - connectSum1) * connect;
                            }
                        } else { // �A�����ĂȂ���
                            // �A�����������Ă����Ȃ�A�ϐ��ɒl����
                            if (connectSum2 > connectSum1) {
                                array[4] += connectSum2 - connectSum1;
                            }
                        }
                    }
                }
            }
            // ���΂Ղ惉�x���̍��v���Z�o
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    if (ignitionLabel2[i][j] > 1) {
                        array[5] += ignitionLabel2[i][j];
                    }
                }
            }
            // �����܂�
            ignitionJudgeFlag = 0;
            return array;
        }

        /** �Ղ�ɘA�����x����t���� */
        void attacheLabel(int i2, int j2, int l, int c) {
            int i = i2;
            int j = j2;
            if (dummyGrid[i][j] > 1 && connectionLabel[i][j] != 1) {
                connectionLabel[i][j] = 1;
                ignitionLabel2[i][j] = l;
                chainLabel[i][j] = c;
                if (i - 1 >= 0 && dummyGrid[i][j] == dummyGrid[i - 1][j]) {
                    attacheLabel(i - 1, j, l, c);
                }
                if (i + 1 <= stage.lows - 1 && dummyGrid[i][j] == dummyGrid[i + 1][j]) {
                    attacheLabel(i + 1, j, l, c);
                }
                if (j - 1 >= 0 && dummyGrid[i][j] == dummyGrid[i][j - 1]) {
                    attacheLabel(i, j - 1, l, c);
                }
                if (j + 1 <= stage.columns - 1 && dummyGrid[i][j] == dummyGrid[i][j + 1]) {
                    attacheLabel(i, j + 1, l, c);
                }
            }
        }

        /** �A���Ɏg���Ղ�Ƀ��x����t���� */
        void myRet() {
            // �������Ղ�����̍����ɖ߂�
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    if (fallLabel[i][j] > 0) {
                        dummyGrid[i - fallLabel[i][j]][j] = dummyGrid[i][j];
                        dummyGrid[i][j] = 0;
                        fallLabel[i][j] = 0;
                    }
                }
            }
            // ���x���t��
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    if (preDummyGrid[i][j] != dummyGrid[i][j] && ignitionChainCount >= 2) {
                        chainLabel[i][j] += ignitionChainCount;
                    }
                }
            }
        }

        int rarara = 0;

        /** ���Γ_�ɂՂ��u���邩���� (���C��) */
        int judgeIgnitionPoint(int i2, int j2) {
            int i = i2;
            int j = j2;
            rarara = 0;
//            if (i2 + 1 < height && dummyGrid[i2 + 1][j2] == 0) {
//                rarara = 0;
//            }
            myJudgeIgnitionPlace2(i, j);
            resetConnectionLabel();
            return rarara;
        }

        /** ���Γ_�ɂՂ��u���邩���� (�ċN�Ăяo����) */
        private void myJudgeIgnitionPlace2(int i, int j) {
            if (connectionLabel[i][j] != 1) {
                connectionLabel[i][j] = 1;
                rarara = getIgnitionPointLabel(i, j);
                if (rarara == 0) {
                    if (i - 1 >= 0 && dummyGrid[i][j] == dummyGrid[i - 1][j]) {
                        // ��
                        myJudgeIgnitionPlace2(i - 1, j);
                    }
                    if (i + 1 <= stage.lows - 1 && dummyGrid[i][j] == dummyGrid[i + 1][j]) {
                        // ��
                        myJudgeIgnitionPlace2(i + 1, j);
                    }
                    if (j - 1 >= 0 && dummyGrid[i][j] == dummyGrid[i][j - 1]) {
                        // ��
                        myJudgeIgnitionPlace2(i, j - 1);
                    }
                    if (j + 1 <= stage.columns - 1 && dummyGrid[i][j] == dummyGrid[i][j + 1]) {
                        // �E
                        myJudgeIgnitionPlace2(i, j + 1);
                    }
                }
            }
        }

        /** ���Γ_�ɂՂ��u���邩���� (�{��) */
        int getIgnitionPointLabel(int i3, int j3) {
            int flag = 0;
            int i = i3;
            int j = j3;
            if (i - 1 >= 0 && dummyGrid[i - 1][j] == 0) {
                // ��ɂՂ悪�Ȃ�
                flag = 1;
            } else if ((j - 1 >= 0 && dummyGrid[i][j - 1] == 0) &&
                      !(i + 2 < stage.lows && dummyGrid[i + 2][j - 1] == 0)) {
                // ���ɂՂ悪�Ȃ�
                flag = 1;
            } else if ((j + 1 < stage.columns && dummyGrid[i][j + 1] == 0) &&
                      !(i + 2 < stage.lows && dummyGrid[i + 2][j + 1] == 0)) {
                // �E�ɂՂ悪�Ȃ�
                flag = 1;
            }
            // �Ԃ�
            return flag;
        }

        /** �A�������Ղ�Q�����܂���Ă��邩���� (���C��) */
        int judgeStack(int i2, int j2) {
            int i = i2;
            int j = j2;
            notBuryFlag = 0;
            judgeStack2(i, j);
            resetConnectionLabel();
            return notBuryFlag;
        }

        /** �A�������Ղ�Q�����܂��Ă��邩���� (�ċN�Ăяo����) */
        private void judgeStack2(int i, int j) {
            if (connectionLabel[i][j] != 1) {
                connectionLabel[i][j] = 1;
                if (i - 1 >= 0) { // ��
                    if (dummyGrid[i][j] == dummyGrid[i - 1][j]) {
                        judgeStack2(i - 1, j);
                    } else if (dummyGrid[i - 1][j] == 0) {
                        notBuryFlag = 1;
                    }
                }
                if (i + 1 <= stage.lows - 1) { // ��
                    if (dummyGrid[i][j] == dummyGrid[i + 1][j]) {
                        judgeStack2(i + 1, j);
                    } else if (dummyGrid[i + 1][j] == 0) {
                        notBuryFlag = 1;
                    }
                }
                if (j - 1 >= 0) { // ��
                    if (dummyGrid[i][j] == dummyGrid[i][j - 1]) {
                        judgeStack2(i, j - 1);
                    } else if (dummyGrid[i][j - 1] == 0) {
                        notBuryFlag = 1;
                    }
                }
                if (j + 1 <= stage.columns - 1) { // �E
                    if (dummyGrid[i][j] == dummyGrid[i][j + 1]) {
                        judgeStack2(i, j + 1);
                    } else if (dummyGrid[i][j + 1] == 0) {
                        notBuryFlag = 1;
                    }
                }
            }
        }

        /** �A������傫���ł������ȉӏ������̂Ղ�Ŗ��܂��Ă��邩���� (���C��) */
        int judgeEraseStack(int i, int j, int g) {
            int count = 0;
            int[] array = new int[colors.length];
            for (int k = 0; k < colors.length; k++) {
                array[k] = 0;
            }
            // �F�Ղ�̂�
            if (g > 1) {
                if (i + 1 <= stage.lows - 1 && dummyGrid[i + 1][j] > 1) {
                    // �E
                    array[dummyGrid[i + 1][j] - 2] += judgeEraseStack2(i + 1, j, g);
                }
                if (j - 1 >= 0 && dummyGrid[i][j - 1] > 1) {
                    // ��
                    array[dummyGrid[i][j - 1] - 2] += judgeEraseStack2(i, j - 1, g);
                }
                if (j + 1 <= stage.columns - 1 && dummyGrid[i][j + 1] > 1) {
                    // ��
                    array[dummyGrid[i][j + 1] - 2] += judgeEraseStack2(i, j + 1, g);
                }
            }
            // ������
            for (int k = 0; k < colors.length; k++) {
                if (array[k] >= 2) {
                    count++;
                }
            }
            // �߂�
            return count;
        }

        /** �A������傫���ł������ȉӏ������̂Ղ�Ŗ��܂��Ă��邩���� (�ċN�Ăяo����) */
        private int judgeEraseStack2(int i2, int j2, int g2) {
            int i = i2;
            int j = j2;
            int g = g2;
            int count = 0;
            if (dummyGrid[i][j] != 1 && dummyGrid[i][j] != g) {
                count++;
            }
            return count;
        }

        /** ���̗�ɂ��铯�F�Ղ�܂ł̉��̋����̍ő�l���Z�o */
        int getDistance() {
            int dist = 0;
            int dist1 = 0;
            int dist2 = 0;
            // ���
            for (int j = 0; j < stage.columns; j++) {
                for (int i = 0; i < stage.lows; i++) {
                    if (dummyGrid[i][j] > 1) {
                        colorList[j][dummyGrid[i][j] - 2] = 1;
                    }
                }
            }
            // ��邼
            for (int i = 0; i < colors.length - 1; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    if (colorList[j][i] == 1) {
                        dist1 = 0;
                        dist2 = 0;
                        // ����
                        for (int l = j - 1; l >= 0; l--) {
                            if (colorList[l][i] == 1) {
                                dist1 = j - l;
                            }
                        }
                        // �E��
                        for (int l = j + 1; l < stage.columns; l++) {
                            if (colorList[l][i] == 1) {
                                dist2 = l - j;
                            }
                        }
                        dist += Math.max(dist1, dist2);
                    }
                }
            }
            // ���Z�b�g
            for (int i = 0; i < colors.length - 1; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    colorList[j][i] = 0;
                }
            }
            // �Ԃ�
            return dist;
        }

        /** �e�X�̗�̍������擾 */
        int[] getHeight() {
            int[] heightList = new int[stage.columns];
            for (int j = 0; j < stage.columns; j++) {
                heightList[j] = stage.lows;
                for (int i = 0; i < stage.lows; i++) {
                    if (dummyGrid[i][j] > 0) {
                        heightList[j] = i;
                        break;
                    }
                }
            }
            return heightList;
        }

        /** �t�B�[���h�ɂ���Ղ�̌��𐔂��� */
        int getPuyoNum() {
            int count = 0;
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    if (dummyGrid[i][j] > 0) {
                        count++;
                    }
                }
            }
            return count;
        }

        /** �A�����̍��v���Z�o */
        int getConnectionSum() {
            int connectionSum = 0;
            for (int i = 0; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    connectionSum += getConnection(i, j);
                }
            }
            return connectionSum;
        }

        /**
         * �Y���Ղ�y�т���ƘA�������Ղ������
         * ������܂Ղ�ɂ��Ă͌��
         */
        void erase2(int i2, int j2, int g2) {
            int i = i2;
            int j = j2;
            int g = g2;
            dummyGrid[i][j] = 0;
            preChainLabel[i][j] = 1;
            if (i - 1 >= 0 && dummyGrid[i - 1][j] == g) { // ��
                erase2(i - 1, j, g);
            }
            if (i + 1 <= stage.lows - 1 && dummyGrid[i + 1][j] == g) { // ��
                erase2(i + 1, j, g);
            }
            if (j - 1 >= 0 && dummyGrid[i][j - 1] == g) { // ��
                erase2(i, j - 1, g);
            }
            if (j + 1 <= stage.columns - 1 && dummyGrid[i][j + 1] == g) { // �E
                erase2(i, j + 1, g);
            }
        }
    }

    /**
     * myAutoFall�𑼂̃X���b�h�Ƃ͓Ɨ��ɌĂяo�����߂̃X���b�h
     */
    class AutoFallTask extends Thread {
        public void run() {
            fallThreadCount++;
            if (fallThreadCount == 1) {
                try { Thread.sleep(FallSpeed); } catch (InterruptedException e) {}
                fallThreadCount--;
                if (waitFlag == 0) {
                    autoFall();
                }
            } else {
                fallThreadCount--;
            }
        }
    }

    /**
     * autoFall�ȊO���Ăяo�����߂̃X���b�h
     */
    class OtherTask extends Thread {
        public void run() {
            // �~�߂�
            try { Thread.sleep(sleepTime); } catch (InterruptedException e) {}
            if (stage.gameFlags[id] == 1) {
                // �A���I���㏈��
                if (sleepMode.equals("AutoFall")) {
                    makePuyo2(); // �Ղ搶��
                    waitFlag = 0; // �{�^�����͎�t�J�n
                    if (fallThreadCount == 0) {
                        // ���Ƃ�
                        autoFall();
                    }
                    try { Thread.sleep(300); } catch (InterruptedException e) {}
                    if (stage.gameFlags[id] == 1 && autoFlag == 1) {
                        // �I�[�g���[�h�N��
                        autoMove();
                    }
                // ��
                } else if (sleepMode.equals("AutoMove")) { // �I�[�g���[�h(�Q�[���X�^�[�g���̂�)
                    autoMove();
                } else if (sleepMode.equals("Stack")) { // �Ղ�ςݏグ
                    stack();
                } else if (sleepMode.equals("Chain")) { // �A������
                    chain();
                } else if (sleepMode.equals("Fall")) { // �A��������
                    gridObject.fall();
                } else if (sleepMode.equals("Right")) { // �E�����ړ�
                    autoMoveRight();
                } else if (sleepMode.equals("Left")) { // �������ړ�
                    autoMoveLeft();
                } else if (sleepMode.equals("Stop")) { // �X�g�b�v
                    waitFlag = 0;
                } else if (sleepMode.equals("Over")) { // �Q�[���I�[�o�[
                    gameOver();
                } else if (sleepMode.equals("Rotate")) { // �I�[�g���[�h��]
                    // �~�߂�
                    try { Thread.sleep(sleepTime); } catch (InterruptedException e) {}
                    if (stage.gameFlags[id] == 1) {
                        if (autoRotate == 0) { // ���̂܂�
                            if (stage.sss == 1) {
                                bottom();
                            }
                        } else { // ��
                            // �Ƃ肠������]
                            rotate(rotateDirection);
                            view.repaint();
                            // 2��]�̏ꍇ
                            if (autoRotate == 3) {
                                try { Thread.sleep(sleepTime); } catch (InterruptedException e) {}
                                if (stage.gameFlags[id] == 1) {
                                    rotate(rotateDirection);
                                    view.repaint();
                                }
                            }
                            // �~�߂�
                            try { Thread.sleep(sleepTime); } catch (InterruptedException e) {}
                            // ���Ƃ�
                            if (stage.gameFlags[id] == 1 && stage.sss == 1) {
                                bottom();
                            }
                        }
                    }
                }
            }
        }
    }
}

/* */