
public class OriginalInstructions {

    private static Instruction[] instance;

    public OriginalInstructions(StepInstruction stepBlock,
                                TurnInstruction turnBlock,
                                PaintInstruction paintBlueBlock,
                                PaintInstruction paintGreenBlock,
                                PaintInstruction paintRedBlock,
                                PaintInstruction paintLightBlock,
                                PaintInstruction paintDarkBlock) {
        instance = new Instruction[]{stepBlock, turnBlock, paintBlueBlock, paintGreenBlock, paintRedBlock
        , paintLightBlock, paintDarkBlock};
    }

    public static Instruction[] getInstance() {
        return instance;
    }
}